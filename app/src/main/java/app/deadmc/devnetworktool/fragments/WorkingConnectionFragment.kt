package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.adapters.JsonInputsAdapter
import app.deadmc.devnetworktool.adapters.ReceivedMessagesAdapter
import app.deadmc.devnetworktool.clients.BaseAbstractClient
import app.deadmc.devnetworktool.helpers.DateTimeHelper
import app.deadmc.devnetworktool.interfaces.WorkingConnectionView
import app.deadmc.devnetworktool.modules.JsonInput
import app.deadmc.devnetworktool.modules.MessageHistory
import app.deadmc.devnetworktool.modules.ReceivedMessage
import app.deadmc.devnetworktool.presenters.MainPresenter
import app.deadmc.devnetworktool.presenters.WorkingConnectionPresenter
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import app.deadmc.devnetworktool.system.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.fragment_working_connection.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_working_connection.*
import kotlinx.android.synthetic.main.fragment_working_connection.view.*
import java.io.Serializable
import java.util.ArrayList
import android.R.attr.defaultValue
import android.widget.Toast
import app.deadmc.devnetworktool.modules.ConnectionHistory


class WorkingConnectionFragment : BaseFragment(), WorkingConnectionView {

    @InjectPresenter
    lateinit var workingConnectionPresenter: WorkingConnectionPresenter

    private var jsonInputArrayList: ArrayList<JsonInput>? = null
    private var receivedMessageArrayList: ArrayList<ReceivedMessage> = ReceivedMessage.createReceivedMessageList(0)
    private lateinit var jsonInputsAdapter: JsonInputsAdapter
    private lateinit var receivedMessagesAdapter: ReceivedMessagesAdapter
    private var errorResulted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workingConnectionPresenter.currentConnectionHistory = this.arguments.getSerializable("connection_history") as ConnectionHistory
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_working_connection, container, false)
        mainActivity = activity as MainActivity
        initElements()
        mainActivity.workingConnectionsPresenter = workingConnectionPresenter
        mainActivity.mainPresenter.doBindService(workingConnectionPresenter.currentConnectionHistory)
        return myFragmentView
    }

    fun initElements() {
        initRecyclerViewInputs()
        initRecyclerViewMessages()
        myFragmentView.buttonSend.setOnClickListener { sendMessage() }
        myFragmentView.buttonAdd.setOnClickListener {
            jsonInputArrayList?.add(JsonInput("", ""))
            jsonInputsAdapter.notifyItemInserted(jsonInputArrayList!!.size - 1)
        }

        myFragmentView.slidingLayout.visibility = View.GONE
        myFragmentView.errorRelativeLayout.visibility = View.GONE
        myFragmentView.loadingRelativeLayout.visibility = View.VISIBLE
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString()
        if (message.length > 0)
            workingConnectionPresenter.sendMessage(message)
        else
            Toast.makeText(context, R.string.string_is_empty, Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerViewInputs() {
        jsonInputArrayList = JsonInput.createJsonInputsList(1)
        jsonInputsAdapter = JsonInputsAdapter(context, jsonInputArrayList, myFragmentView.messageEditText)
        myFragmentView.recyclerViewInputs.adapter = jsonInputsAdapter
        myFragmentView.recyclerViewInputs.layoutManager = LinearLayoutManager(context)
        val callback = SimpleItemTouchHelperCallback(jsonInputsAdapter, true)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView( myFragmentView.recyclerViewInputs)
    }

    private fun initRecyclerViewMessages() {
        receivedMessageArrayList = ReceivedMessage.createReceivedMessageList(0)
        receivedMessagesAdapter = ReceivedMessagesAdapter(context, receivedMessageArrayList, this)
        myFragmentView.recyclerViewMessages.adapter = receivedMessagesAdapter
        myFragmentView.recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        val callback = SimpleItemTouchHelperCallback(receivedMessagesAdapter, false)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(myFragmentView.recyclerViewMessages)
        myFragmentView.recyclerViewMessages.addItemDecoration(SimpleDividerItemDecoration(context))
        workingConnectionPresenter.fillReceivedMessageList()
    }

    override fun fillReceivedMessageList(arrayListMessageHistory: ArrayList<MessageHistory>) {
        for (messageHistory in arrayListMessageHistory) {
            receivedMessageArrayList.add(ReceivedMessage(messageHistory.message,
                    DateTimeHelper.getTimeFromTimestamp(messageHistory.timeAdded), messageHistory.id!!, messageHistory.isFromServer))
            receivedMessagesAdapter.notifyItemInserted(receivedMessageArrayList.size - 1)
        }
    }

    override fun errorCallback() {
        Log.e("error", "callback")
        myFragmentView.post {
            errorResulted = true
            myFragmentView.slidingLayout.visibility = View.GONE
            myFragmentView.loadingRelativeLayout.visibility = View.GONE
            myFragmentView.errorRelativeLayout.visibility = View.VISIBLE
        }
    }

    override fun successfulCallback() {
        Log.e("success", "callback")
        myFragmentView.post {
            errorResulted = false
            myFragmentView.slidingLayout.visibility = View.VISIBLE
            myFragmentView.loadingRelativeLayout.visibility = View.GONE
            myFragmentView.errorRelativeLayout.visibility = View.GONE
        }
    }

    override fun addLineToAdapter(receivedMessage: ReceivedMessage) {
        try {
            activity.runOnUiThread {
                receivedMessageArrayList.add(receivedMessage)
                receivedMessagesAdapter.notifyItemInserted(receivedMessageArrayList.size - 1)
            }
        } catch (e: NullPointerException) {
        }
    }

    companion object {
        fun getInstance(serializable: Serializable): WorkingConnectionFragment {
            val fragment = WorkingConnectionFragment()

            val args = Bundle()
            args.putSerializable("connection_history", serializable)

            fragment.arguments = args

            return fragment
        }
    }

}