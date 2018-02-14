package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.interfaces.views.RestHistoryView
import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.launch

@InjectViewState
class RestHistoryPresenter : BasePresenter<RestHistoryView>() {
    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        Log.e(TAG,"loadRestHistory")
        //viewState.loadRequestHistory(restRequestHistory)
    }

    fun deleteItem(element: RestRequestHistory) = launch{
        element.delete()
    }
}