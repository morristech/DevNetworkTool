package app.deadmc.devnetworktool.fragments.ping

import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.PingView

/**
 * Created by Feren on 14.01.2017.
 */
abstract class BasePingFragment : BaseFragment(), PingView {

    protected var initCompleted = false

    override fun setStartButtonOn() {
    }

    override fun setStartButtonOff() {
    }

    override fun hideProgress() {
    }

    override fun showProgress() {
    }

}
