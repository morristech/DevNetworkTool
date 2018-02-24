package app.deadmc.devnetworktool.models

import app.deadmc.devnetworktool.extensions.asyncSave
import app.deadmc.devnetworktool.helpers.getDateTimeFromTimestamp
import app.deadmc.devnetworktool.interfaces.model.BaseModel
import com.orm.SugarRecord
import java.io.Serializable

class ConnectionHistory : SugarRecord, Serializable, BaseModel {

    var port: Int = 0
    var lastUsage: Long = 0
    var ipAddress: String = ""
    var name: String = ""
    var lastUsageTime: String = ""
        get() = getDateTimeFromTimestamp(lastUsage)
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
        this.lastUsage = System.currentTimeMillis()
        asyncSave()
    }

    override fun getUniqueId(): String {
        return id.toString()
    }


}
