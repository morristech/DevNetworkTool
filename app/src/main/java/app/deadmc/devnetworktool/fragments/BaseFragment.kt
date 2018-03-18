package app.deadmc.devnetworktool.fragments

import android.view.View
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.presenters.BasePresenter
import com.arellomobile.mvp.MvpAppCompatFragment


abstract class BaseFragment : MvpAppCompatFragment() {

    protected lateinit var myFragmentView: View
    protected lateinit var mainActivity: MainActivity
    val TAG = javaClass.simpleName

    fun checkActivityIsFinishing():Boolean {
        if (activity!!.isFinishing)
            return true
        return false
    }

    override fun onStart() {
        super.onStart()
        getPresenter().initObserver()
    }

    abstract fun getPresenter():BasePresenter<*>

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
