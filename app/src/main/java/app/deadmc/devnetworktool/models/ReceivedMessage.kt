package app.deadmc.devnetworktool.models

import app.deadmc.devnetworktool.interfaces.model.BaseModel

class ReceivedMessage(val text: String, val time: String, val id: Long, val isFromServer: Boolean) : BaseModel {
    override fun getUniqueId(): String {
        return id.toString()
    }
}
