package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.ConnectionHistory
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.io.Serializable

@StateStrategyType(SkipStrategy::class)
interface MainActivityView : MvpView {
    fun runFragmentDependsOnClickedItem(item: Int)
    fun runFragmentDependsOnId(id: Int)
    fun runFragmentDependsOnId(id: Int, serializable: Serializable)
    fun runFragmentDefault()
    fun showDialogExitConnection()
    fun hideDialogExitConnection()
    fun doBindService(connectionHistory: ConnectionHistory?)
    fun doUnbindService()
    fun stopService()
    fun setCustomTitle(stringId: Int)
    fun setCustomTitle(title: String)
}