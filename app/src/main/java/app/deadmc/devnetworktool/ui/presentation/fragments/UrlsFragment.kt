package app.deadmc.devnetworktool.ui.presentation.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.adapters.UrlHistoryAdapter
import app.deadmc.devnetworktool.ui.presentation.views.ConnectionsView
import app.deadmc.devnetworktool.data.models.ConnectionHistory
import app.deadmc.devnetworktool.extensions.hideKeyboard
import app.deadmc.devnetworktool.ui.presentation.activities.MainActivity
import app.deadmc.devnetworktool.ui.presentation.presenters.ConnectionsPresenter
import kotlinx.android.synthetic.main.dialog_add_url.view.*
import kotlinx.android.synthetic.main.fragment_history_of_connections.view.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*
import java.util.*

abstract class UrlsFragment : BaseFragment(), ConnectionsView {
    var alertDialog: AlertDialog? = null
    lateinit var alertView: View
    lateinit var connectionHistoryAdapter: UrlHistoryAdapter

    override abstract fun getPresenter() : ConnectionsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_history_of_connections, container, false) ?: View(context)
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
        connectionHistoryAdapter = object : UrlHistoryAdapter(arrayListConnectionHistory) {
            override fun onClickItem(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().openNextFragment(mainActivity.mainPresenter,connectionHistory)
            }

            override fun onDeleteItem(connectionHistory: ConnectionHistory) {
                getPresenter().deleteConnectionHistory(connectionHistory)
                if (connectionHistoryAdapter.itemCount == 0)
                    showEmpty()
            }

            override fun onEditItem(connectionHistory: ConnectionHistory, position: Int) {
                getPresenter().showDialogForEdit(connectionHistory,position)
            }
        }

        myFragmentView.recyclerViewHistory.adapter = connectionHistoryAdapter
    }

    override fun fillRecyclerView(list:List<ConnectionHistory>) {
        activity?.runOnUiThread {
            val arrayListConnectionHistory = ArrayList(list)
            if (!arrayListConnectionHistory.isEmpty()) {
                showView()
                connectionHistoryAdapter.addAll(arrayListConnectionHistory)
            } else {
                showEmpty()
            }
        }
    }

    override fun showDialogForCreate() {
        val alertDialogBuilder = AlertDialog.Builder(context!!, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.dialog_add_url, null)
        alertDialogBuilder.setView(alertView)
        fillDialogVariables(getPresenter().currentConnectionHistory)
        alertDialogBuilder.setPositiveButton(R.string.add, { _, _ ->
            activity?.hideKeyboard()
            addConnectionHistory()
            getPresenter().hideDialog()
        })
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    override fun showDialogForEdit(connectionHistory: ConnectionHistory, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context!!, R.style.AppTheme_Dialog_Alert)
        alertView = onGetLayoutInflater(null).inflate(R.layout.dialog_add_url, null)
        alertDialogBuilder.setView(alertView)
        fillDialogVariables(connectionHistory)

        alertDialogBuilder.setPositiveButton(R.string.edit) { _, _ ->
            activity?.hideKeyboard()
            connectionHistory.ipAddress = alertView.urlEditText.text.toString()
            connectionHistory.name = alertView.urlEditText.text.toString()
            connectionHistory.port = 80
            getPresenter().saveConnectionHistory(connectionHistory)
            connectionHistoryAdapter.notifyItemChanged(position)
            getPresenter().hideDialog()
        }
        alertDialogBuilder.setOnDismissListener {
            activity?.hideKeyboard()
            getPresenter().hideDialog()
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    override fun hideDialog() {
        activity?.hideKeyboard()
        alertDialog?.dismiss()
        getPresenter().currentConnectionHistory = ConnectionHistory()
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
        alertView.urlEditText.setText(connectionHistory.ipAddress)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getPresenter().currentConnectionHistory = collectConnectionHistory()
    }

    abstract fun collectConnectionHistory(): ConnectionHistory
}