package com.j.ming.socket.server

import com.j.ming.socket.util.SocketUtil
import com.j.ming.socket.config.SocketConfig
import com.j.ming.socket.util.Logger
import com.j.ming.socket.util.doAsync
import java.io.IOException
import java.net.ServerSocket

/**
 * Created by sunny on 17-11-17.
 */
object Server {
    private val serverSocketMap = HashMap<Int, ServerSocket>()
    private val callbackMap = HashMap<Int, SocketUtil.SocketCallback>()
    private val savePathMap = HashMap<Int, String>()

    fun startListen(callback: SocketUtil.SocketCallback, listenPort: Int = SocketConfig.FileListenPort,
                    serverSocketStrategy: ServerSocketStrategy = ServerSocketImpl(), savePath: String = "") {
        (serverSocketMap[listenPort]?.let {
            if (it.isClosed) {
                return@let ServerSocket(listenPort)
            } else {  //之前已经在这个端口监听了，只是替换回调对象
                callbackMap[listenPort] = callback
                savePathMap[listenPort] = savePath
                return
            }
        } ?: ServerSocket(listenPort))
                .let {
                    println("begin")
                    serverSocketMap[listenPort] = it
                    callbackMap[listenPort] = callback
                    savePathMap[listenPort] = savePath
                    while (!it.isClosed) {
                        try {
                            Logger.i("--------------------accepting---------------------")
                            val socket = it.accept()
                            Logger.i("--------------------accept success!-------------------")
                            /**
                             * change to another thread to deal the request
                             * This thread still comeback to listen the request from client
                             */
                            doAsync {
                                callbackMap[listenPort]?.let { cb ->
                                    savePathMap[listenPort]?.let { sp ->
                                        serverSocketStrategy.service(socket, cb, savePath = sp)
                                    }
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
    }

    fun stop(listenPort: Int = SocketConfig.FileListenPort) {
        serverSocketMap[listenPort]?.close()
    }

    fun stopAll() {
        serverSocketMap.forEach { _, v ->
            v.close()
        }
    }

}