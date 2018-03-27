package com.j.ming.socket.client

import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.config.SocketConfig
import com.j.ming.socket.model.DeviceInfo
import java.io.IOException
import java.net.InetAddress
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
interface ClientStrategy{
    /**
     * this method send a simple info to server
     * to register this device to Group
     */
    fun register(address: InetAddress, deviceInfo: DeviceInfo, port: Int = SocketConfig.FileListenPort,
                 registerCallback: (success: Boolean) -> Unit = {})

    fun sendFile(path: String, ip: String, callback: SocketUtil.SocketCallback)


    /**
     * 通过Socket发送简单字符信息
     *
     * @param socket 操作的套接字对象
     * @param info   要发送的信息
     * @throws IOException
     */
    @Throws(IOException::class)
    fun sendText(socket: Socket, info: String)
}