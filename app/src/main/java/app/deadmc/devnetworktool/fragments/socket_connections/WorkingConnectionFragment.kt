package app.deadmc.devnetworktool.fragments.socket_connections

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.MainActivity
import app.deadmc.devnetworktool.interfaces.views.WorkingConnectionView
import app.deadmc.devnetworktool.models.JsonInput
import app.deadmc.devnetworktool.models.MessageHistory
import app.deadmc.devnetworktool.models.ReceivedMessage
import app.deadmc.devnetworktool.presenters.WorkingConnectionPresenter
import kotlinx.android.synthetic.main.fragment_working_connection.*
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_working_connection.view.*
import java.io.Serializable
import java.util.ArrayList
import android.widget.Toast
import app.deadmc.devnetworktool.adapters.JsonInputsAdapter
import app.deadmc.devnetworktool.adapters.ReceivedMessagesAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.helpers.getTimeFromTimestamp
import app.deadmc.devnetworktool.models.ConnectionHistory
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class WorkingConnectionFragment : BaseFragment(), WorkingConnectionView {

    @InjectPresenter
    lateinit var workingConnectionPresenter: WorkingConnectionPresenter

    private var jsonInputArrayList: ArrayList<JsonInput> = JsonInput.createJsonInputsList(1)
    private var receivedMessageArrayList: ArrayList<ReceivedMessage> = ArrayList()
    private lateinit var jsonInputsAdapter: JsonInputsAdapter
    private lateinit var receivedMessagesAdapter: ReceivedMessagesAdapter
    private var errorResulted = true
    private var baseDragViewHeight:Int = 0

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
            jsonInputArrayList.add(JsonInput("", ""))
            jsonInputsAdapter.notifyItemInserted(jsonInputArrayList.size - 1)
        }

        myFragmentView.slidingLayout.visibility = View.GONE
        myFragmentView.errorRelativeLayout.visibility = View.GONE
        myFragmentView.loadingRelativeLayout.visibility = View.VISIBLE
        myFragmentView.slidingLayout.dragView.slidingView.imageViewArrow.rotation = 180f

        myFragmentView.slidingLayout.dragView.slidingView.postDelayed({
            baseDragViewHeight = myFragmentView.slidingLayout.dragView.slidingView.height
        },120)


        myFragmentView.slidingLayout.addPanelSlideListener(object:SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                myFragmentView.slidingLayout.dragView.slidingView.imageViewArrow.rotation = 180f*slideOffset+180f
                val width = myFragmentView.slidingLayout.dragView.slidingView.width
                if (slideOffset >= 0.9) {
                    val opacity = ((1f - slideOffset) * 10f)

                    val height = Math.round(baseDragViewHeight.toFloat() * opacity)
                    myFragmentView.slidingLayout.dragView.slidingView.layoutParams =
                            LinearLayout.LayoutParams(width, height)
                    myFragmentView.slidingLayout.dragView.slidingView.alpha = opacity
                }

                if (slideOffset < 0.9) {
                    myFragmentView.slidingLayout.dragView.slidingView.layoutParams =
                            LinearLayout.LayoutParams(width, baseDragViewHeight)
                    myFragmentView.slidingLayout.dragView.slidingView.alpha = 1.0f
                }
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                Log.e(TAG,"onPanelStateChanged $previousState | $newState")
            }
        })
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString()
        if (!message.isEmpty())
            workingConnectionPresenter.sendMessage(message)
        else
            Toast.makeText(context, R.string.string_is_empty, Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerViewInputs() {
        jsonInputsAdapter = JsonInputsAdapter(jsonInputArrayList, myFragmentView.messageEditText)
        myFragmentView.recyclerViewInputs.adapter = jsonInputsAdapter
        myFragmentView.recyclerViewInputs.layoutManager = LinearLayoutManager(context)
        myFragmentView.recyclerViewInputs.isNestedScrollingEnabled = true
    }

    private fun initRecyclerViewMessages() {
        receivedMessagesAdapter = object : ReceivedMessagesAdapter(context,receivedMessageArrayList) {
            override fun onDeleteItem(element: ReceivedMessage) {
            }
        }
        myFragmentView.recyclerViewMessages.adapter = receivedMessagesAdapter
        myFragmentView.recyclerViewMessages.layoutManager = LinearLayoutManager(context)
        workingConnectionPresenter.fillReceivedMessageList()
    }

    override fun fillReceivedMessageList(arrayListMessageHistory: ArrayList<MessageHistory>) {
        for (messageHistory in arrayListMessageHistory) {
            receivedMessageArrayList.add(ReceivedMessage(messageHistory.message,
                    getTimeFromTimestamp(messageHistory.timeAdded), messageHistory.id!!, messageHistory.isFromServer))
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