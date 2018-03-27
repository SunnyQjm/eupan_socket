package com.j.ming.socket.util



object Logger{
    fun i(info: String){
        println(info)
    }

    fun e(info: String, e: Exception){
        println(info)
        e.printStackTrace()
    }

    fun e(e: Exception, info: String){
        println(info)
        e.printStackTrace()
    }
}