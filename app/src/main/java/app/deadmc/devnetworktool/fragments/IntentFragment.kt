package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.interfaces.views.IntentView
import app.deadmc.devnetworktool.presenters.BasePresenter
import app.deadmc.devnetworktool.presenters.IntentPresenter
import com.arellomobile.mvp.presenter.InjectPresenter

class IntentFragment : BaseFragment(),IntentView {
    @InjectPresenter
    lateinit var presenter: IntentPresenter

    override fun getPresenter(): BasePresenter<*> {
        return presenter
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myFragmentView = inflater?.inflate(R.layout.fragment_history_of_connections, container, false) ?: View(context)
        mainActivity = activity as MainActivity
        initElements()
        return myFragmentView
    }

    fun initElements() {

    }
}
