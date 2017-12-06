package app.deadmc.devnetworktool.interfaces.views

import app.deadmc.devnetworktool.models.PingStructure
import com.arellomobile.mvp.MvpView
import java.util.ArrayList

/**
 * Created by DEADMC on 11/22/2017.
 */
interface PingPageView : MvpView {

    fun addPingStructure(pingStructure: PingStructure, canUpdate: Boolean)

    fun refreshFragment(arrayList: ArrayList<PingStructure>)
}