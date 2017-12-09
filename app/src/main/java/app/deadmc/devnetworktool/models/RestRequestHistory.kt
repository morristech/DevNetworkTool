package app.deadmc.devnetworktool.models

import app.deadmc.devnetworktool.interfaces.model.BaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orm.SugarRecord

import java.lang.reflect.Type
import java.util.ArrayList

class RestRequestHistory : SugarRecord,BaseModel {
    var timeLastUsage: Long = System.currentTimeMillis()
    var url: String = ""
    var method: String = ""
    private var headers: String = ""
    private var requests: String = ""

    constructor()

    constructor(url: String, method: String, headers: ArrayList<KeyValueModel>, requests: ArrayList<KeyValueModel>) {
        this.url = url
        this.method = method
        this.headers = Gson().toJson(headers)
        this.requests = Gson().toJson(requests)
    }

    fun getHeaders(): ArrayList<KeyValueModel> {
        return getArrayList(headers)
    }

    fun getRequests(): ArrayList<KeyValueModel> {
        return getArrayList(requests)
    }

    override fun getUniqueId(): String {
        return id.toString()
    }

    private fun getArrayList(stringValue: String): ArrayList<KeyValueModel> {
        if (stringValue.isEmpty()) return ArrayList()
        val type = object : TypeToken<ArrayList<KeyValueModel>>() {

        }.type
        val gson = Gson()
        return gson.fromJson(stringValue, type)
    }


}
