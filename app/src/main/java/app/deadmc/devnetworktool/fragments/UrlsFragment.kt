package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.adapters.UrlHistoryAdapter
import app.deadmc.devnetworktool.interfaces.views.ConnectionsView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import kotlinx.android.synthetic.main.add_url_layout.view.*
import kotlinx.android.synthetic.main.fragment_history_of_connections.view.*
import java.util.ArrayList

abstract class UrlsFragment : BaseFragment(), ConnectionsView {
    var alertDialog: AlertDialog? = null
    lateinit var alertView: View
    lateinit var connectionHistoryAdapter:UrlHistoryAdapter

    abstract fun getPresenter() : ConnectionsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myFragmentView = inflater?.inflate(R.layout.fragment_history_of_connections, container, false) ?: View(context)
        mainActivity = activity as MainActivity
        initElements()
        return myFragmentView
    }

    fun initElements() {
        myFragmentView.floatingActionButton.setOnClickListener { getPresenter().showDialogForCreate() }
        myFragmentView.recyclerViewHistory.setHasFixedSize(true)
        myFragmentView.recyclerViewHistory.layoutManager = LinearLayoutManager(context)
        getPresenter().fillRecyclerView()
    }



    override fun fillRecyclerView(list:List<ConnectionHistory>) {
        val arrayListConnectionHistory = ArrayList(list)
        connectionHistoryAdapter = object : UrlHistoryAdapter(arrayListConnectionHistory) {
            override fun onClickItem(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().openNextFragment(mainActivity.mainPresenter,connectionHistory)
            }

            override fun onDeleteItem(connectionHistory: ConnectionHistory) {
                getPresenter().deleteConnectionHistory(connectionHistory)
            }

            override fun onEditItem(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().showDialogForEdit(connectionHistory,position)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = connectionHistoryAdapter
        connectionHistoryAdapter.notifyDataSetChanged()
    }

    override fun showDialogForCreate() {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.add_url_layout, null)
        alertDialogBuilder.setView(alertView)
        fillDialogVariables(getPresenter().currentConnectionHistory)
        alertDialogBuilder.setPositiveButton(R.string.add, { _, _ ->
            addConnectionHistory()
            getPresenter().hideDialog()
        })
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    override fun showDialogForEdit(connectionHistory: ConnectionHistory, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.add_url_layout, null)
        alertDialogBuilder.setView(alertView)
        fillDialogVariables(connectionHistory)

        alertDialogBuilder.setPositiveButton(R.string.edit) { _, _ ->
            connectionHistory.ipAddress = alertView.urlEditText.text.toString()
            connectionHistory.name = alertView.urlEditText.text.toString()
            connectionHistory.port = 80
            getPresenter().saveConnectionHistory(connectionHistory)
            connectionHistoryAdapter.notifyItemChanged(position)
            getPresenter().hideDialog()
        }
        alertDialogBuilder.setOnDismissListener {
            getPresenter().hideDialog()
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    override fun hideDialog() {
        alertDialog?.dismiss()
        getPresenter().currentConnectionHistory = ConnectionHistory()
    }


    fun addConnectionHistory() {
        val connectionHistory = collectConnectionHistory()
        getPresenter().saveConnectionHistory(connectionHistory)
        connectionHistoryAdapter.addItem(connectionHistory)
    }


    private fun fillDialogVariables(connectionHistory: ConnectionHistory) {
        alertView.urlEditText.setText(connectionHistory.ipAddress)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        getPresenter().currentConnectionHistory = collectConnectionHistory()
    }

    abstract fun collectConnectionHistory(): ConnectionHistory
}