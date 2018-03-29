package com.j.ming.socket.config

/**
 * Created by Sunny on 2017/4/21 0021.
 */
object SocketConfig {
    const val FileListenPort = 3001
    const val FORWARD_ITEM_LISTEN_PORT = 3002
    const val singleTaskPort = 3003

    const val RegisterListenPort = 3005
    const val SimpleTextListenPort = 3006


    internal val SERVER_ADD_FORWARD_ITEM_ACTION = "SERVER_ADD_FORWARD_ITEM_ACTION"
    internal val SERVER_DELETE_FORWARD_ITEM_ACTION = "SERVER_DELETE_FORWARD_ITEM_ACTION"
    internal val SERVER_UPDATE_FORWARD_ITEM_ACTION = "SERVER_UPDATE_FORWARD_ITEM_ACTION"
    internal val SERVER_FORWARD_ITEM_ACTION = "SERVER_FORWARD_ITEM_ACTION"
}
