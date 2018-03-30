package com.j.ming.socket.client

import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.config.SocketConfig
import com.j.ming.socket.model.DeviceInfo
import java.io.File
import java.net.InetAddress
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
object Client {
    private val strategy: ClientSocketStrategy = ClientSocketImpl()

    fun sendText(ip: String, info: String, port: Int = SocketConfig.SimpleTextListenPort) {
        try {
            Socket(ip, port).let {
                strategy.sendText(it, info)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendFile(path: String, ip: String, callback: SocketUtil.SocketCallback, port: Int = SocketConfig.FileListenPort,
                 sendName: String? = null) {
        try {
            val socket = Socket(ip, port)
            val f = File(path)
            if (!f.exists()) {
                callback.onError(null, Exception("File not exist: ${f.absolutePath}"))
                return
            }
            strategy.sendSingleFile(socket, f, callback, sendName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun register(ip: String, deviceInfo: DeviceInfo, registerCallback: (success: Boolean) -> Unit,
                 port: Int = SocketConfig.RegisterListenPort) {
        try {
            Socket(ip, port).let {
                strategy.register(it, deviceInfo, registerCallback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun sendControlCommand(ip: String, command: Int, port: Int = SocketConfig.FileListenPort,
                           sendControlCommandCallback: (success: Boolean) -> Unit = {}) {
        try {
            Socket(ip, port).let {
                strategy.sendControlCommand(it, command, sendControlCommandCallback)
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }


}