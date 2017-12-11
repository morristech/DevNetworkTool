package app.deadmc.devnetworktool.models

import app.deadmc.devnetworktool.interfaces.model.BaseModel
import java.util.*

class JsonInput(var key: String, var value: String) : BaseModel {
    var id = Random().nextLong()

    override fun getUniqueId(): String {
        return id.toString()
    }

    companion object {

        private val lastContactId = 0

        fun createJsonInputsList(num: Int): ArrayList<JsonInput> {
            val contacts = ArrayList<JsonInput>()

            for (i in 1..num) {
                contacts.add(JsonInput("", ""))
            }

            return contacts
        }
    }
}
