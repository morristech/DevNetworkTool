package app.deadmc.devnetworktool.models

import com.orm.SugarRecord

import java.io.Serializable

import app.deadmc.devnetworktool.interfaces.model.BaseModel

class ConnectionHistory : SugarRecord, Serializable, BaseModel {

    var port: Int = 0
    var lastUsage: Int = 0
    var ipAddress: String = ""
    var name: String = ""
    var lastUsageTime: String = ""
    var type: String = ""

    val isEmpty: Boolean
        get() = ipAddress.isEmpty()


    constructor()

    constructor(name: String, ipAddress: String, port: Int, type: String) {
        this.name = name
        this.port = port
        this.ipAddress = ipAddress
        this.type = type
        setLastUsageDefault()
    }

    fun setLastUsageDefault() {
        val epoch = System.currentTimeMillis() / 1000
        this.lastUsage = epoch.toInt()
    }

    override fun getUniqueId(): String {
        return id.toString()
    }


}
