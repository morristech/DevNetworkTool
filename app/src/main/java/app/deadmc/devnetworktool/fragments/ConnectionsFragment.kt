package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.adapters.ConnectionHistoryAdapter
import app.deadmc.devnetworktool.constants.DevConsts
import app.deadmc.devnetworktool.helpers.CheckHelper
import app.deadmc.devnetworktool.interfaces.ConnectionsView
import app.deadmc.devnetworktool.modules.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import app.deadmc.devnetworktool.presenters.TcpConnectionsPresenter
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.add_connection_layout.view.*
import kotlinx.android.synthetic.main.fragment_history_of_connections.view.*
import java.util.ArrayList

abstract class ConnectionsFragment : BaseFragment(),ConnectionsView {
    var alertDialog: AlertDialog? = null
    lateinit var alertView: View
    lateinit var connectionHistoryAdapter: ConnectionHistoryAdapter

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
        connectionHistoryAdapter = object : ConnectionHistoryAdapter(arrayListConnectionHistory) {
            override fun onClickItem(connectionHistory: ConnectionHistory, position: Int) {
                Log.e("onClickItem","connectionHistory "+connectionHistory.ipAddress)
                getPresenter().openWorkingConnectionFragment(mainActivity.mainPresenter,connectionHistory)
            }

            override fun removeItemCallback(connectionHistory: ConnectionHistory) {
                getPresenter().deleteConnectionHistory(connectionHistory)
            }

            override fun editItemCallback(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().showDialogForEdit(connectionHistory,position)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = connectionHistoryAdapter
        myFragmentView.recyclerViewHistory.addItemDecoration(SimpleDividerItemDecoration(activity))
        connectionHistoryAdapter.notifyDataSetChanged()
    }

    override fun showDialogForCreate() {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.add_connection_layout, null)
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
        alertView = onGetLayoutInflater(null).inflate(R.layout.add_connection_layout, null)
        alertDialogBuilder.setView(alertView)
        fillDialogVariables(connectionHistory)

        alertDialogBuilder.setPositiveButton(R.string.edit) { _, _ ->
            connectionHistory.ipAddress = alertView.editTextIpAddress.text.toString()
            connectionHistory.name = alertView.editTextName.text.toString()
            connectionHistory.port = if (alertView.editTextPort.text.toString().isEmpty()) 0 else Integer.parseInt(alertView.editTextPort.text.toString())
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
        alertView.editTextIpAddress.setText(connectionHistory.ipAddress)
        alertView.editTextName.setText(connectionHistory.name)
        if (connectionHistory.port == 0)
            alertView.editTextPort.setText("")
        else
            alertView.editTextPort.setText(connectionHistory.port.toString())
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        getPresenter().currentConnectionHistory = collectConnectionHistory()
    }

    abstract fun collectConnectionHistory(): ConnectionHistory
}