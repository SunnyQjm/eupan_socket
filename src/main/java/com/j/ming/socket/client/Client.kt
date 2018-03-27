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
object Client : ClientStrategy {
    private val strategy: ClientSocketStrategy = ClientSocketImpl()

    override fun sendText(socket: Socket, info: String) {
        try {
            strategy.sendText(socket, info)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun sendFile(path: String, ip: String, callback: SocketUtil.SocketCallback) {
        try {
            val socket = Socket(ip, SocketConfig.FileListenPort)
            val f = File(path)
            if (!f.exists()) {
                callback.onError(null, Exception("File not exist: ${f.absolutePath}"))
                return
            }
            strategy.sendSingleFile(socket, f, callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun register(address: InetAddress, deviceInfo: DeviceInfo, port: Int, registerCallback: (success: Boolean) -> Unit) {
        try{
            strategy.register(address, deviceInfo, port, registerCallback)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }



}