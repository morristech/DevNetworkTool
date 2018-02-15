package app.deadmc.devnetworktool.fragments.ping

import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.PingView

abstract class PingBaseFragment : BaseFragment(), PingView {

    protected var initCompleted = false
}
