package app.deadmc.devnetworktool.models

import com.orm.SugarRecord

class MessageHistory : SugarRecord {
    var timeAdded: Long = 0
    var port: Int = 0
    var isFromServer: Boolean = false

    var ipAddress: String = ""
    var message: String = ""
    var type: String = ""

    constructor() {
    }

    constructor(message: String, ipAddress: String, port: Int, type: String, fromServer: Boolean) {
        this.message = message
        this.ipAddress = ipAddress
        this.port = port
        this.type = type
        this.isFromServer = fromServer
        setTimeAddedDefault()
    }

    fun setTimeAddedDefault() {
        this.timeAdded = System.currentTimeMillis()
    }


}
