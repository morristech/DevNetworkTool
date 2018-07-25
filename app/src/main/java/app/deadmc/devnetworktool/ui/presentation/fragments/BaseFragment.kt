package app.deadmc.devnetworktool.ui.presentation.fragments

import android.view.View
import app.deadmc.devnetworktool.data.shared_preferences.Preferences
import app.deadmc.devnetworktool.ui.presentation.activities.MainActivity
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import com.arellomobile.mvp.MvpAppCompatFragment
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance


abstract class BaseFragment : MvpAppCompatFragment() {

    protected lateinit var myFragmentView: View
    protected lateinit var mainActivity: MainActivity
    val TAG = javaClass.simpleName
    val kodein = LateInitKodein()
    val preferences: Preferences by kodein.instance()

    fun checkActivityIsFinishing():Boolean {
        if (activity!!.isFinishing)
            return true
        return false
    }

    override fun onStart() {
        super.onStart()
        getPresenter().initObserver()
    }

    abstract fun getPresenter(): BasePresenter<*>

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
