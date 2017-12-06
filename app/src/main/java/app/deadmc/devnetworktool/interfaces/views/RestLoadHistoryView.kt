package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 12/2/2017.
 */
interface RestLoadHistoryView : MvpView {
    fun loadRequestHistory(restRequestHistory: RestRequestHistory)
}