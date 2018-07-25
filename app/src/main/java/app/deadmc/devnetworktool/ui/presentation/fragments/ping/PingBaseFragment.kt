package app.deadmc.devnetworktool.ui.presentation.fragments.ping

import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import app.deadmc.devnetworktool.ui.presentation.views.PingView

abstract class PingBaseFragment : BaseFragment(), PingView {

    protected var initCompleted = false
}
