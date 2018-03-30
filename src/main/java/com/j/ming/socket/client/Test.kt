package com.j.ming.socket.client

import com.j.ming.socket.model.TransLocalFile
import com.j.ming.socket.util.SocketUtil
import java.net.InetAddress


/**
 * Created by sunny on 18-3-27.
 */
fun main(args: Array<String>) {
//    Client.register("127.0.0.1", DeviceInfo("macAddress", "deviceName", "ipAddress"),
//            registerCallback = {
//                println("register: $it")
//            })
////    Client.sendText(InetAddress.getLocalHost().hostAddress, "sadfsadf")

    Client.sendControlCommand(InetAddress.getLocalHost().hostAddress, 3, sendControlCommandCallback = {
        println("send command: $it")
    })
//    Client.sendFile("/home/sunny/阳光电影www.ygdy8.com.第三度嫌疑人.BD.720p.日语中字.mkv", InetAddress.getLocalHost().hostAddress, object : SocketUtil.SocketCallback{
//        override fun onBegin(file: TransLocalFile) {
//            println("begin: $file")
//        }
//
//        override fun onProgress(file: TransLocalFile, progressState: SocketUtil.ProgressState) {
//            println("onProgress: $progressState")
//        }
//
//        override fun onEnd(file: TransLocalFile) {
//            println("onEnd: $file")
//        }
//
//        override fun onError(file: TransLocalFile?, e: Throwable) {
//            println("onError: $e")
//        }
//
//    }, sendName = "lalla.mkv")
}