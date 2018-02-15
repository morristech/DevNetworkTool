package app.deadmc.devnetworktool.extensions

import app.deadmc.devnetworktool.constants.PING
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.orm.SugarRecord
import com.orm.query.Select
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

fun SugarRecord.deferredSave():Deferred<Long> {
    return async {
        this@deferredSave.save()
    }
}

fun SugarRecord.asyncSave() {
    launch {
        this@asyncSave.save()
    }
}


fun SugarRecord.asyncDelete() {
    launch {
        this@asyncDelete.delete()
    }
}

fun <T> deferredFindById(type:Class<T>, id:Long):Deferred<T> {
    return async {
        SugarRecord.findById(type, id)
    }
}

fun <T> deferredSelectDesk(type: Class<T>, whereClause:String):Deferred<List<T>> {
    return async {
        Select.from(type).where(whereClause).orderBy("last_usage DESC").toList() as List<T>
    }
}
