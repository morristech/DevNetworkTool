package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestHistoryEvent
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.extensions.asyncDelete
import app.deadmc.devnetworktool.extensions.deferredFindById
import app.deadmc.devnetworktool.extensions.deferredSelectAll
import app.deadmc.devnetworktool.extensions.deferredSelectDesc
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.interfaces.views.RestHistoryView
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference

@InjectViewState
class RestHistoryPresenter : BasePresenter<RestHistoryView>() {

    override fun initObserver() {
        RxBus.subscribe {
            if (it is RestRequestEvent) {
                Log.e(TAG,"initObserver $it")
                addElementById(it.id)
            }
        }
    }

    fun addElementById(id:Long) {
        val ref = viewState.asReference()
        launch(UI) {
            safe {
                val restRequestHistory = deferredFindById(RestRequestHistory::class.java, id).await()
                Log.e(TAG, "restRequestHistory $id $restRequestHistory")
                ref().addItem(restRequestHistory)
            }
        }
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        RxBus.post(RestHistoryEvent(restRequestHistory))
    }

    fun deleteItem(element: RestRequestHistory) = launch{
        element.asyncDelete()
    }

    fun fillRecyclerView() {
        launch {
            val list = deferredSelectAll(RestRequestHistory::class.java).await()
            viewState.fillRecyclerView(list)
        }
    }

}