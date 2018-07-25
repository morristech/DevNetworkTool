package app.deadmc.devnetworktool.ui.presentation.presenters

import app.deadmc.devnetworktool.events.PingEvent
import app.deadmc.devnetworktool.events.PingPageChangedEvent
import app.deadmc.devnetworktool.utils.getPing
import app.deadmc.devnetworktool.ui.presentation.views.PingMainView
import app.deadmc.devnetworktool.data.models.PingStructure
import app.deadmc.devnetworktool.observables.RxBus
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

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
