package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.events.PingEvent
import app.deadmc.devnetworktool.events.PingPageChangedEvent
import app.deadmc.devnetworktool.interfaces.views.PingView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import java.util.*

@InjectViewState
class PingPresenter : BasePresenter<PingView>() {

    var currentUrl = ""
    var currentPosition:Int = 0
    var pingStructureArrayList: ArrayList<PingStructure> = ArrayList()

    override fun initObserver() {
        compositeDisposable.add(RxBus.subscribe {
            if (it is PingEvent)
                addMessage(it.pingStructure)

            if (it is PingPageChangedEvent)
                currentPosition = it.pagePosition
        })
    }

    private fun addMessage(pingStructure: PingStructure) {
        //pingStructureArrayList.add(pingStructure)
        viewState.addPingStructure(pingStructure)
    }
}