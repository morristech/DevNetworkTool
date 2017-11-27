package app.deadmc.devnetworktool.fragments

import android.support.v4.app.Fragment
import android.view.View

import com.arellomobile.mvp.MvpAppCompatFragment

import app.deadmc.devnetworktool.activities.MainActivity

/**
 * Created by Feren on 12.01.2017.
 */
open class BaseFragment : MvpAppCompatFragment() {

    protected lateinit var myFragmentView: View
    protected lateinit var mainActivity: MainActivity

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}// Required empty public constructor
