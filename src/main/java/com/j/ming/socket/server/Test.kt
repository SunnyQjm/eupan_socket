package com.j.ming.socket.server

import com.j.ming.socket.model.DeviceInfo
import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.model.TransLocalFile
import com.j.ming.socket.util.Logger
import com.j.ming.socket.util.doAsync
import java.net.InetAddress

/**
 * Created by sunny on 18-3-27.
 */
fun main(args: Array<String>) {
    doAsync {
        Server.startListen(object : SocketUtil.SocketCallback {
            override fun onBegin(file: TransLocalFile) {
                Logger.i("begin: $file")
            }

            override fun onProgress(file: TransLocalFile, progressState: SocketUtil.ProgressState) {
                Logger.i("onProgress: $progressState")
            }

            override fun onEnd(file: TransLocalFile) {
                Logger.i("onEnd: $file")
            }

            override fun onError(file: TransLocalFile?, e: Throwable) {
                Logger.i("onError: $e")
            }

            override fun onNewDeviceRegister(file: DeviceInfo) {
                Logger.i("onNewDeviceRegister: $file")
            }

            override fun onReceiveSimpleText(inetAddress: InetAddress, message: String) {
                Logger.i("onReceiveSimpleText: $message(${inetAddress.hostAddress})")
            }
        }, savePath = "/home/sunny/Videos/")
    }
    Thread.sleep(1000)
    Server.stop()
    Thread.sleep(1000)
    doAsync {
        Server.startListen(object : SocketUtil.SocketCallback {
            override fun onBegin(file: TransLocalFile) {
                Logger.i("begin: $file")
            }

            override fun onProgress(file: TransLocalFile, progressState: SocketUtil.ProgressState) {
                Logger.i("onProgress: $progressState-----")
            }

            override fun onEnd(file: TransLocalFile) {
                Logger.i("onEnd: $file")
            }

            override fun onError(file: TransLocalFile?, e: Throwable) {
                Logger.i("onError: $e")
            }

            override fun onNewDeviceRegister(file: DeviceInfo) {
                Logger.i("onNewDeviceRegister: $file")
            }

            override fun onReceiveSimpleText(inetAddress: InetAddress, message: String) {
                Logger.i("onReceiveSimpleText: $message(${inetAddress.hostAddress})")
            }
        }, savePath = "/home/sunny/Pictures/")
    }
//    Thread.sleep(10000)
//    Server.stopAll()
}