package app.deadmc.devnetworktool.data.models

import app.deadmc.devnetworktool.data.interfaces.BaseModel

class ReceivedMessage(val text: String, val time: String, val id: Long, val isFromServer: Boolean) : BaseModel {
    override fun getUniqueId(): String {
        return id.toString()
    }
}
