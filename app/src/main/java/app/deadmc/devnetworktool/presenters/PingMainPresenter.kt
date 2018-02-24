package app.deadmc.devnetworktool.presenters

import app.deadmc.devnetworktool.events.PingEvent
import app.deadmc.devnetworktool.events.PingPageChangedEvent
import app.deadmc.devnetworktool.helpers.getPing
import app.deadmc.devnetworktool.interfaces.views.PingMainView
import app.deadmc.devnetworktool.models.PingStructure
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference

@InjectViewState
class PingMainPresenter : BasePresenter<PingMainView>() {
    var currentUrl:String = ""

    var currentPage:Int = 0
        get() = field
        set(value) {
            field = value
            RxBus.post(PingPageChangedEvent(value))
        }

    @Volatile private var working = false

    fun handleClick() {
        if (working) {
            working = false
            viewState.setStartButtonOn()
            viewState.hideProgress()
        } else {
            working = true
            getPings()
            viewState.setStartButtonOff()
            viewState.showProgress()
        }

    }

    private fun getPings() {
        launch(UI) {
            while (working) {
                val rawPing = getPing(currentUrl).await()
                RxBus.post(PingEvent(PingStructure(rawPing)))
            }
        }
    }
}
