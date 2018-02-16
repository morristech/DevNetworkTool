package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.events.RestHistoryEvent
import app.deadmc.devnetworktool.events.RestRequestEvent
import app.deadmc.devnetworktool.extensions.asyncDelete
import app.deadmc.devnetworktool.extensions.deferredFindById
import app.deadmc.devnetworktool.interfaces.views.RestHistoryView
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import com.orm.SugarRecord
import kotlinx.coroutines.experimental.launch

@InjectViewState
class RestHistoryPresenter : BasePresenter<RestHistoryView>() {

    override fun initObserver() {
        RxBus.subscribe {
            if (it is RestRequestEvent) {
                addElementById(it.id)
            }
        }
    }

    fun addElementById(id:Long) {
        launch {
            val restRequestHistory = deferredFindById(RestRequestHistory::class.java,id).await()
            viewState.addItem(restRequestHistory)
        }
    }

    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        RxBus.post(RestHistoryEvent(restRequestHistory))
    }

    fun deleteItem(element: RestRequestHistory) = launch{
        element.asyncDelete()
    }

    fun fillRecyclerView() {
        /*
        launch {
            Log.e(TAG,"coroutine")
            safe {
                val list = deferredSelectDesk(RestRequestHistory::class.java).await()
                if (list != null)
                    viewState.fillRecyclerView(list)
            }
        }


        */

        viewState.fillRecyclerView(SugarRecord.listAll(RestRequestHistory::class.java))
    }

}