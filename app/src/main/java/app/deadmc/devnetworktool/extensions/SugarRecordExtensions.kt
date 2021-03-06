package app.deadmc.devnetworktool.extensions

import com.orm.SugarRecord
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

fun SugarRecord.deferredSave(): Deferred<Long> {
    return bg {
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

fun <T> deferredFindById(type: Class<T>, id: Long): Deferred<T> {
    return bg {
        SugarRecord.findById(type, id)
    }
}

fun <T> deferredSelectDesc(type: Class<T>, whereClause: String? = null, args: String? = null): Deferred<List<T>> {
    return bg {
        //SugarRecord.find(type, whereClause, args, null, null,"lis").toList()
        SugarRecord.find(type,whereClause, if (args != null) arrayOf(args) else arrayOf(),null,"last_usage DESC",null).toList()
        //SugarRecord.find(type, whereClause, args).toList()
    }
}

fun <T> deferredSelectAll(type: Class<T>):Deferred<List<T>> {
    return bg {
        SugarRecord.listAll(type,"last_usage DESC")
    }
}
