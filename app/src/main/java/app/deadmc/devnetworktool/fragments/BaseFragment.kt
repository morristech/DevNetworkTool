package app.deadmc.devnetworktool.fragments

import android.support.v4.app.Fragment
import android.view.View

import com.arellomobile.mvp.MvpAppCompatFragment

import app.deadmc.devnetworktool.activities.MainActivity


open class BaseFragment : MvpAppCompatFragment() {

    protected lateinit var myFragmentView: View
    protected lateinit var mainActivity: MainActivity
    val TAG = javaClass.simpleName

    fun checkActivityIsFinishing():Boolean {
        if (activity.isFinishing)
            return true
        return false
    }

    override fun toString(): String {
        return this.javaClass.simpleName
    }



}
