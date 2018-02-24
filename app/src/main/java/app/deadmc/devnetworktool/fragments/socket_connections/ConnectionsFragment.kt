package app.deadmc.devnetworktool.fragments.socket_connections

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
import app.deadmc.devnetworktool.extensions.hideKeyboard
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.interfaces.views.ConnectionsView
import app.deadmc.devnetworktool.models.ConnectionHistory
import app.deadmc.devnetworktool.presenters.ConnectionsPresenter
import kotlinx.android.synthetic.main.dialog_add_connection.view.*
import kotlinx.android.synthetic.main.fragment_history_of_connections.view.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*
import java.util.*

abstract class ConnectionsFragment : BaseFragment(), ConnectionsView {
    var alertDialog: AlertDialog? = null
    lateinit var alertView: View
    open lateinit var connectionHistoryAdapter: ConnectionHistoryAdapter

    override abstract fun getPresenter(): ConnectionsPresenter

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
        initRecyclerView()
        getPresenter().fillRecyclerView()
    }

    fun initRecyclerView() {
        val arrayListConnectionHistory = ArrayList<ConnectionHistory>()
        connectionHistoryAdapter = object : ConnectionHistoryAdapter(arrayListConnectionHistory) {
            override fun onClickItem(connectionHistory: ConnectionHistory, position: Int) {
                Log.e("onClickItem", "connectionHistory " + connectionHistory.ipAddress)
                getPresenter().openNextFragment(mainActivity.mainPresenter, connectionHistory)
            }

            override fun onDeleteItem(connectionHistory: ConnectionHistory) {
                getPresenter().deleteConnectionHistory(connectionHistory)
                if (this.itemCount == 0)
                    this@ConnectionsFragment.showEmpty()
            }

            override fun onEditItem(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().showDialogForEdit(connectionHistory, position)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = connectionHistoryAdapter
    }

    override fun fillRecyclerView(list: List<ConnectionHistory>) {
        val arrayListConnectionHistory = ArrayList(list)
        if (!arrayListConnectionHistory.isEmpty()) {
            showView()
            connectionHistoryAdapter.addAll(arrayListConnectionHistory)
        } else {
            showEmpty()
        }
    }

    override fun showDialogForCreate() {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.dialog_add_connection, null)
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
        alertView = onGetLayoutInflater(null).inflate(R.layout.dialog_add_connection, null)
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
        activity.hideKeyboard()
    }

    override fun showEmpty() {
        myFragmentView.recyclerViewHistory.visibility = View.GONE
        myFragmentView.emptyLayout.visibility = View.VISIBLE
    }

    override fun showView() {
        myFragmentView.recyclerViewHistory.visibility = View.VISIBLE
        myFragmentView.emptyLayout.visibility = View.GONE
    }


    fun addConnectionHistory() {
        val connectionHistory = collectConnectionHistory()
        getPresenter().saveConnectionHistory(connectionHistory)
        if (connectionHistoryAdapter.itemCount == 0)
            showView()
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