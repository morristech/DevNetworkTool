package app.deadmc.devnetworktool.ui.presentation.presenters

import android.util.Log
import app.deadmc.devnetworktool.events.RestHistoryEvent
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.extensions.asyncDelete
import app.deadmc.devnetworktool.extensions.deferredFindById
import app.deadmc.devnetworktool.extensions.deferredSelectAll
import app.deadmc.devnetworktool.utils.safe
import app.deadmc.devnetworktool.ui.presentation.views.RestHistoryView
import app.deadmc.devnetworktool.data.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

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
        async(UI) {
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
        async(UI) {
            val list = deferredSelectAll(RestRequestHistory::class.java).await()
            viewState.fillRecyclerView(list)
        }
    }

}