package com.j.ming.socket.model


/**
 * Created by Sunny on 2017/5/13 0013.
 */

class TransLocalFile private constructor(builder: Builder) {
    var md5: String? = null
    val name: String?
    val size: Long
    var path: String? = ""

    var createTime: Long = 0
    var isDone = false
    var progress = 0
    var rate: String? = null
    var fileTAG = -1
    var status: Int = STATUS_UNSPECIFIED

    init {
        md5 = builder.md5
        name = builder.name
        size = builder.size
        path = builder.path
        createTime = builder.createTime
        isDone = builder.done
        progress = builder.progress
        rate = builder.rate
        fileTAG = builder.fileTAG
        status = builder.status

    }

    class Builder {
        var name: String? = null
        var size: Long = 0
        var createTime: Long = 0
        var done: Boolean = false
        var path: String? = null
        var progress: Int = 0
        var rate: String? = null
        var fileTAG: Int = 0
        var md5: String? = null
        var status = STATUS_UNSPECIFIED

        fun name(`val`: String): Builder {
            name = `val`
            return this
        }

        fun size(`val`: Long): Builder {
            size = `val`
            return this
        }

        fun createTime(`val`: Long): Builder {
            createTime = `val`
            return this
        }

        fun done(`val`: Boolean): Builder {
            done = `val`
            return this
        }

        fun path(`val`: String): Builder {
            path = `val`
            return this
        }

        fun progress(`val`: Int): Builder {
            progress = `val`
            return this
        }

        fun rate(`val`: String): Builder {
            rate = `val`
            return this
        }

        fun fileTAG(`val`: Int): Builder {
            fileTAG = `val`
            return this
        }

        fun md5(`val`: String): Builder {
            md5 = `val`
            return this
        }

        fun status(`val`: Int): Builder{
            status = `val`
            return this
        }

        fun build(): TransLocalFile {
            return TransLocalFile(this)
        }
    }

    companion object {
        const val TAG_SEND = 0
        const val TAG_RECEIVE = 1

        const val STATUS_UNSPECIFIED = -1
        const val STATUS_UPLOADING = 1
        const val STATUS_SUSPEND = 2
        const val STATUS_CANCEL = 3
        const val STATUS_FAILED = 4
        const val STATUS_SUCCEED = 5
        const val STATUS_WAITING = 6

    }
}
