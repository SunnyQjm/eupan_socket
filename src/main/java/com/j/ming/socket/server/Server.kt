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
object Server : ServerStrategy {
    private var listenPort = SocketConfig.FileListenPort
    private var serverSocketStrategy: ServerSocketStrategy = ServerSocketImpl()
    private var serverSocket: ServerSocket? = null
    const val SAVE_PATH = ""
    override fun startListen(callback: SocketUtil.SocketCallback) {
        if(serverSocket == null || serverSocket?.isClosed == true){
            serverSocket = ServerSocket(listenPort)
        }
        serverSocket?.let {
            while(!it.isClosed){
                try {
                    Logger.i("--------------------accepting---------------------")
                    val socket = it.accept()
                    Logger.i("--------------------accept success!-------------------")
                    /**
                     * change to another thread to deal the request
                     * This thread still comeback to listen the request from client
                     */
                    doAsync {
                        serverSocketStrategy.service(socket, callback)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun stop(){
        serverSocket?.close()
    }
}