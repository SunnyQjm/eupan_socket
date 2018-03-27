package com.j.ming.socket.server

import com.j.ming.socket.util.SocketUtil
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
interface ServerSocketStrategy {
    fun service(socket: Socket, callback: SocketUtil.SocketCallback)
}