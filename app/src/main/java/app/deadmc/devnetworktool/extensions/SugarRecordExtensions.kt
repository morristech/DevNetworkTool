package app.deadmc.devnetworktool.extensions

import com.orm.SugarRecord
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

fun SugarRecord.asyncSave():Deferred<Long> {
    return async {
        this@asyncSave.save()
    }
}

fun SugarRecord.asyncDelete() {
    launch {
        this@asyncDelete.delete()
    }
}

fun <T>asyncFindById(type:Class<T>, id:Long):Deferred<T> {
    return async {
        SugarRecord.findById(type, id)
    }
}