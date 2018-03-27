package com.j.ming.socket.server

import com.j.ming.socket.config.ProtocolCode
import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.model.DeviceInfo
import com.j.ming.socket.model.TransLocalFile
import com.j.ming.socket.util.GsonUtil
import com.j.ming.socket.util.Logger
import com.j.ming.socket.util.toBean
import java.net.Socket


/**
 * Created by sunny on 17-11-17.
 */
class ServerSocketImpl : ServerSocketStrategy {

    override fun service(socket: Socket, callback: SocketUtil.SocketCallback) {
        Logger.i("service")
        var su: SocketUtil? = null
        try {
            su = SocketUtil(socket)
            val code: Int = su.readInt()
            Logger.i("code: $code")
            when (code) {
            //Client register
                ProtocolCode.CODE_REGISTER -> {
                    val json = su.readUTF()
                    Logger.i("json: $json")
                    val deviceInfo = GsonUtil.json2Bean(json, DeviceInfo::class.java)
                    deviceInfo.ipAddress = socket.inetAddress.hostAddress
                    println(deviceInfo.ipAddress)
                    deviceInfo.status = DeviceInfo.Status.CONNECTED

                    callback.onNewDeviceRegister(deviceInfo)
                }

                ProtocolCode.REQUEST_SIMPLE_TEXT -> {
                    //将对方的IP写回，以便对方获取IP
                    callback.onReceiveSimpleText(socket.inetAddress, su.readUTF())
                    su.writeUTF(socket.inetAddress.hostAddress)
                }
                ProtocolCode.REQUEST_SINGLE_FILE -> {
                    val transLocalFile = su.readUTF().toBean(TransLocalFile::class.java)
                    transLocalFile.fileTAG = TransLocalFile.TAG_RECEIVE
                    val path = "${Server.SAVE_PATH}${transLocalFile.name}"
                    transLocalFile.path = path
                    Logger.i("传输的文件名：" + transLocalFile.name)
                    Logger.i("文件的大小为：" + transLocalFile.size)
                    Logger.i("文件接收路径：$path")
                    su.readFile(transLocalFile, callback)
                    Logger.i("文件读取完毕")
                }
            }
            su.writeInt(-1)
            su.flush()
        } catch (e: Throwable) {
            callback.onError(null, e)
        } finally {
            try {
                su?.close()
            } catch (e: Exception) {
                callback.onError(null, e)
            }
        }

    }
}