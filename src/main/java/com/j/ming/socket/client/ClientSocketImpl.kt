package com.j.ming.socket.client

import com.j.ming.socket.config.ProtocolCode
import com.j.ming.socket.model.DeviceInfo
import com.j.ming.socket.model.TransLocalFile
import com.j.ming.socket.util.Logger
import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.util.eacyUse
import com.j.ming.socket.util.toJson
import java.io.File
import java.net.InetAddress
import java.net.Socket

/**
 *
 * Created by sunny on 17-11-17.
 */
class ClientSocketImpl : ClientSocketStrategy {

    override fun sendControlCommand(socket: Socket, controlCode: Int, sendControlCommandCallback: (success: Boolean) -> Unit) {
        SocketUtil(socket).eacyUse({
            writeInt(ProtocolCode.REQUEST_CODE_CONTROL)
            writeInt(controlCode)
            shutDownOutput()
            sendControlCommandCallback(true)
        }, {
            sendControlCommandCallback(false)
        })
    }


    override fun sendText(socket: Socket, info: String) {
        SocketUtil(socket).eacyUse({
            writeInt(ProtocolCode.REQUEST_SIMPLE_TEXT)
            writeUTF(info)
            Logger.i("服务器返回来的消息为: ${readUTF()}")
        })
    }


    override fun sendSingleFile(socket: Socket, file: File, callback: SocketUtil.SocketCallback, sendName: String?): Boolean {
        var result = false
        val transLocalFile = TransLocalFile.Builder()
                .name(sendName?:file.name)
                .path(file.path)
                .size(file.length())
                .fileTAG(TransLocalFile.TAG_SEND)
                .createTime(System.currentTimeMillis())
                .build()
        SocketUtil(socket).eacyUse({
            writeInt(ProtocolCode.REQUEST_SINGLE_FILE)
            writeUTF(transLocalFile.toJson())
            writeFile(transLocalFile, callback)
            //文件传输完毕之后关写一端
            shutDownOutput()
            result = true
        }, { e ->
            callback.onError(transLocalFile, e)
        })
        return result
    }

    override fun register(socket: Socket, deviceInfo: DeviceInfo,
                          registerCallback: (success: Boolean) -> Unit) {
        SocketUtil(socket).eacyUse({
            writeInt(ProtocolCode.CODE_REGISTER)
            writeUTF(deviceInfo.toJson())
            Logger.i("receive: ${readInt()}")
            registerCallback(true)
        }, {
            registerCallback(false)
        })
    }

}