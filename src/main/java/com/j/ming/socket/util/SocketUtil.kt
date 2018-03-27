package com.j.ming.socket.util

import com.j.ming.socket.model.DeviceInfo
import com.j.ming.socket.model.TransLocalFile
import java.io.*
import java.net.InetAddress
import java.net.Socket
import kotlin.properties.Delegates

fun InputStream.copyToWithCallback(out: OutputStream,
                                   callback: (buffer: ByteArray, bytesRead: Int, alreadyCopy: Long) -> Unit = { _, _, _ -> },
                                   bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        callback(buffer, bytes, bytesCopied)
        bytes = read(buffer)
    }
    return bytesCopied
}

/**
 * Created by sunny on 17-11-17.
 */
class SocketUtil(val socket: Socket) {
    private var out by Delegates.notNull<DataOutputStream>()
    private var input by Delegates.notNull<DataInputStream>()

    init {
        out = DataOutputStream(socket.getOutputStream())
        input = DataInputStream(socket.getInputStream())
    }


    fun writeInt(i: Int) {
        out.writeInt(i)
    }

    fun readInt(): Int = input.readInt()

    fun writeUTF(info: String) {
        out.writeUTF(info)
    }

    fun flush() {
        out.flush()
    }

    fun readUTF(): String = input.readUTF()


    /**
     * 读文件
     *
     * @return
     */
    @Throws(IOException::class)
    fun readFile(transLocalFile: TransLocalFile, callback: SocketCallback?) {
        val savePath = transLocalFile.path

        callback?.onBegin(transLocalFile)

        val totalSize = transLocalFile.size
        val startTime = System.currentTimeMillis()
        var preTime = startTime
        var preTransBits = 0L
        input.copyToWithCallback(File(savePath).outputStream(), { buffer, bytesRead, alreadyCopy ->

            ProgressState(totalSize, alreadyCopy).let { state ->
                if (transLocalFile.progress == state.percent())
                    return@let
                transLocalFile.isDone = state.done()
                transLocalFile.progress = state.percent()
                val span = System.currentTimeMillis() - preTime
                //没传输结束之前用来限速
                if (span < 200 && !state.done())
                    return@let
                preTime += span
                transLocalFile.rate = convertToRate(alreadyCopy - preTransBits, span)
                preTransBits = alreadyCopy
                callback?.onProgress(transLocalFile, state)
            }
        })

        transLocalFile.rate = convertToRate(totalSize, System.currentTimeMillis() - startTime)
        transLocalFile.createTime = System.currentTimeMillis()
        transLocalFile.isDone = true
        callback?.onEnd(transLocalFile)
    }

    /**
     * 写文件
     *
     * @return
     */
    @Throws(IOException::class)
    fun writeFile(transLocalFile: TransLocalFile, callback: SocketCallback?) {
        val file = File(transLocalFile.path)
        if (!file.exists() || file.isDirectory) {
            callback?.onError(transLocalFile, FileNotFoundException("文件不存在"))
            throw FileNotFoundException("文件不存在")
        }

        callback?.onBegin(transLocalFile)

        val totalSize = file.length()
        val startTime = System.currentTimeMillis()
        var preTime = startTime
        var alreadyTransBits = 0L
        var preTransBits = 0L

        File(transLocalFile.path).forEachBlock(1024 * 24) { fileBuffer, bytesRead ->
            out.write(fileBuffer, 0, bytesRead)
            alreadyTransBits += bytesRead

            ProgressState(totalSize, alreadyTransBits).let { state ->
                if (transLocalFile.progress == state.percent())
                    return@let
                transLocalFile.isDone = state.done()
                transLocalFile.progress = state.percent()
                val span = System.currentTimeMillis() - preTime
                //没传输结束之前用来限速
                if (span < 200 && !state.done())
                    return@let
                preTime += span
                transLocalFile.rate = convertToRate(alreadyTransBits - preTransBits, span)
                preTransBits = alreadyTransBits
                callback?.onProgress(transLocalFile, state)
            }

        }
        transLocalFile.rate = convertToRate(totalSize, System.currentTimeMillis() - startTime)
        transLocalFile.createTime = System.currentTimeMillis()
        transLocalFile.isDone = true
        callback?.onEnd(transLocalFile)
    }

    fun close() {
        out.close()
        input.close()
        socket.close()
    }

    data class ProgressState(val totalSize: Long, val alreadyTransBits: Long) {
        fun done(): Boolean = (totalSize == alreadyTransBits)

        fun percent(): Int = if (totalSize == 0L)
            0
        else
            Math.ceil(alreadyTransBits * 100.0 / totalSize).toInt()
    }

    interface SocketCallback {

        fun onNewDeviceRegister(file: DeviceInfo){}

        fun onReceiveSimpleText(inetAddress: InetAddress, message: String){}

        fun onBegin(file: TransLocalFile)

        fun onProgress(file: TransLocalFile, progressState: ProgressState)

        fun onEnd(file: TransLocalFile)

        fun onError(file: TransLocalFile?, e: Throwable)

    }
}

fun SocketUtil.eacyUse(block: SocketUtil.() -> Unit, errorCallback: (e: Exception) -> Unit = {}){
    val socketUtil = this
    try {
        block()
    } catch (e: Exception){
        e.printStackTrace()
        errorCallback(e)
    } finally {
        try {
            socketUtil.close()
        } catch (e: Exception){
            e.printStackTrace()
            errorCallback(e)
        }
    }
}

/**
 *
 * @param length    bit
 * @param span        ms
 * @return
 */
fun convertToRate(length: Long, span: Long): String {
    val rate = NumberUtil.persistTwo((length / span * 1000).toDouble())
    if (rate / 1024 < 1) {
        return "" + rate + "B/s"
    } else if (rate / 1024f / 1024f < 1) {
        var d = 1.0 * rate / 1024
        d = 1.0 * Math.round(d * 10) / 10
        var sd = "" + d
        val p = sd.indexOf(".")
        if (p != -1) {
            sd = sd.substring(0, p + 2)
        }
        return "" + sd + "KB/s"
    } else if (rate / 1024f / 1024f / 1024f < 1) {
        var d = 1.0 * rate / 1024.0 / 1024.0
        d = 1.0 * Math.round(d * 10) / 10
        var sd = "" + d
        val p = sd.indexOf(".")
        if (p != -1) {
            sd = sd.substring(0, p + 2)
        }
        return "" + sd + "MB/s"
    } else if (rate / 1024f / 1024f / 1024f / 1024f < 1) {
        var d = 1.0 * rate / 1024.0 / 1024.0 / 1024.0
        d = 1.0 * Math.round(d * 10) / 10
        var sd = "" + d
        val p = sd.indexOf(".")
        if (p != -1) {
            sd = sd.substring(0, p + 2)
        }
        return "" + sd + "GB/s"
    } else {
        var d = 1.0 * rate / 1024.0 / 1024.0 / 1024.0 / 1024.0
        d = 1.0 * Math.round(d * 10) / 10
        var sd = "" + d
        val p = sd.indexOf(".")
        if (p != -1) {
            sd = sd.substring(0, p + 2)
        }
        return "" + sd + "TB/s"
    }

}

