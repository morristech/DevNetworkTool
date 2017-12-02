package app.deadmc.devnetworktool.interfaces

import app.deadmc.devnetworktool.modules.RestRequestHistory
import com.arellomobile.mvp.MvpView

/**
 * Created by DEADMC on 12/2/2017.
 */
interface RestLoadHistoryView : MvpView {
    fun loadRequestHistory(restRequestHistory: RestRequestHistory)
}