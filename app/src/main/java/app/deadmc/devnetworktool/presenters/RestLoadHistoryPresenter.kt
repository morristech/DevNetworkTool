package app.deadmc.devnetworktool.presenters

import android.util.Log
import app.deadmc.devnetworktool.interfaces.RestLoadHistoryView
import app.deadmc.devnetworktool.modules.RestRequestHistory
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

/**
 * Created by DEADMC on 12/2/2017.
 */
@InjectViewState
class RestLoadHistoryPresenter : BasePresenter<RestLoadHistoryView>() {
    fun loadRestHistory(restRequestHistory: RestRequestHistory) {
        Log.e(TAG,"loadRestHistory")
        viewState.loadRequestHistory(restRequestHistory)
    }
}