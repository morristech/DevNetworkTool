package app.deadmc.devnetworktool.fragments.ping

import java.util.ArrayList

import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.PingPageView
import app.deadmc.devnetworktool.modules.PingStructure
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.PingPagePresenter
import app.deadmc.devnetworktool.presenters.PingPresenter

/**
 * Created by Feren on 14.01.2017.
 */
abstract class BasePingFragment : BaseFragment(), PingPageView {

    var pingPresenter:PingPresenter? = null

    abstract fun getPresenter():PingPagePresenter
    override abstract fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean)
    override abstract fun refreshFragment(arrayList: ArrayList<PingStructure>)
}
