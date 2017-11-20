package app.deadmc.devnetworktool.interfaces

import app.deadmc.devnetworktool.modules.ConnectionHistory
import com.arellomobile.mvp.MvpView
import java.io.Serializable

/**
 * Created by DEADMC on 11/11/2017.
 */
interface MainActivityView : MvpView {
    fun runFragmentDependsOnClickedItem(item: Int)
    fun runFragmentDependsOnId(id: Int)
    fun runFragmentDependsOnId(item: Int, serializable: Serializable)
    fun showDialogExitConnection()
    fun hideDialogExitConnection()
    fun doBindService(connectionHistory: ConnectionHistory?)
    fun doUnbindService()
    fun stopService()
    fun setCustomTitle(stringId: Int)
    fun setCustomTitle(title: String)

}