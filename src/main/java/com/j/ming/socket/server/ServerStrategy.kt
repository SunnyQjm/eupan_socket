package com.j.ming.socket.server

import com.j.ming.socket.util.SocketUtil

/**
 * Created by sunny on 17-11-17.
 */
interface ServerStrategy{
    /**
     * this method invoke begin to listen the connect from client
     */
    fun startListen(callback: SocketUtil.SocketCallback)
}