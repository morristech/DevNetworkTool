package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.RestRequestHistory
import com.arellomobile.mvp.MvpView

interface RestHistoryView : MvpView {
    fun addItem(restRequestHistory: RestRequestHistory)
}