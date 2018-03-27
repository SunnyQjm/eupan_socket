package com.j.ming.socket.util


/**
 * Created by sunny on 17-11-16.
 */

fun doAsync(block: ()->Unit){
    Thread{
        block()
    }.start()
}