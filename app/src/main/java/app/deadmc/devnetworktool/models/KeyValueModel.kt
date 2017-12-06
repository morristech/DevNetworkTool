package app.deadmc.devnetworktool.models

import app.deadmc.devnetworktool.interfaces.model.BaseModel
import java.util.*

/**
 * Created by adanilov on 15.03.2017.
 */

class KeyValueModel : BaseModel {
    var key: String = ""
    var value: String = ""
    var id = Random().nextLong()

    override fun getUniqueId(): String {
        return id.toString()
    }
}
