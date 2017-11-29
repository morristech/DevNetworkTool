package app.deadmc.devnetworktool.fragments.ping

import java.util.ArrayList

import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.PingView
import app.deadmc.devnetworktool.modules.PingStructure

/**
 * Created by Feren on 14.01.2017.
 */
abstract class BasePingFragment : BaseFragment(), PingView {

    protected var initCompleted = false

    override fun setStartButtonOn() {
    }

    override fun setStartButtonOff() {
    }

}
