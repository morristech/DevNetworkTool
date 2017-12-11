package app.deadmc.devnetworktool.fragments.ping

import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.PingView

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
