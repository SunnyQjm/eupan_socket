package com.j.ming.socket.client

import com.j.ming.socket.model.DeviceInfo
import com.j.ming.socket.util.SocketUtil
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
interface ClientSocketStrategy{
    /**
     * this method send a simple info to Server
     * to register this device to Group
     */
    fun register(socket: Socket, deviceInfo: DeviceInfo, registerCallback: (success: Boolean) -> Unit = {})


    /**
     * 通过Socket发送简单字符信息
     *
     * @param socket 操作的套接字对象
     * @param info   要发送的信息
     * @throws IOException
     */
    @Throws(IOException::class)
    fun sendText(socket: Socket, info: String)

    /**
     * This method indicate to send a Single File to other device by socket
     * @param socket            the socket connect between your device and the destination
     * @param file              the file you want to send
     * @param callback
     * @param sendName          发送过去的文件名
     */
    fun sendSingleFile(socket: Socket, file: File, callback: SocketUtil.SocketCallback, sendName: String? = null): Boolean
}