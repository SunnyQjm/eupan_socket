package com.j.ming.socket.model

/**
 * Created by sunny on 18-3-27.
 */

data class DeviceInfo(
        val deviceAddress: String,
        val deviceName: String,
        var ipAddress: String,
        var status: Status = Status.DISCONNECTED
){

    enum class Status{
        DISCONNECTED, CONNECTED, CONNECTING
    }
}