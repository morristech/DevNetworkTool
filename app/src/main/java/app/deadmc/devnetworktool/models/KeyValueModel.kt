package app.deadmc.devnetworktool.models

import android.util.Log
import app.deadmc.devnetworktool.interfaces.model.BaseModel
import java.util.*

/**
 * Created by adanilov on 15.03.2017.
 */

class KeyValueModel : BaseModel {
    var key: String = ""
        set(value)  {
            Log.e("KeyValueModel","value $value")
            field = value
        }
    var value: String = ""
    var id = Random().nextLong()

    override fun getUniqueId(): String {
        return id.toString()
    }

    fun isEmpty():Boolean {
        return key.isEmpty() && value.isEmpty()
    }

    override fun toString(): String {
        return "key = $key , value = $value"
    }
}
