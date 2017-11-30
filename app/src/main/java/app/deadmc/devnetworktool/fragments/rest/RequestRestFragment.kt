package app.deadmc.devnetworktool.fragments.rest

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.EditText
import android.widget.Spinner

import java.util.ArrayList


import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.KeyValueAdapter
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.helpers.AllHeaders
import app.deadmc.devnetworktool.interfaces.RestView
import app.deadmc.devnetworktool.modules.KeyValueModel
import app.deadmc.devnetworktool.modules.ResponseDev
import app.deadmc.devnetworktool.modules.RestRequestHistory
import app.deadmc.devnetworktool.presenters.RestPresenter
import app.deadmc.devnetworktool.system.ItemTouchCallback
import app.deadmc.devnetworktool.system.SimpleDividerItemDecoration
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_rest_request.view.*

class RequestRestFragment : BaseFragment(), RestView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var restPresenter: RestPresenter

    private var methodSpinner: Spinner? = null
    private lateinit var keyValueAdapterHeaders: KeyValueAdapter
    private lateinit var keyValueAdapterRequest: KeyValueAdapter

    //for dialog
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var alertView: View? = null
    private var editTextKey: EditText? = null
    private var editTextValue: EditText? = null
    private var keySpinner: Spinner? = null
    private var valueSpinner: Spinner? = null
    private var keyParamsList: ArrayList<String> = ArrayList()
    private var valueParamsList: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest_request, container, false)
        initElements()
        return myFragmentView
    }

    fun initElements() {
        //super.initElements();
        initButtons()
        initEditText()
        initSpinner()
        initHeadersRecyclerView()
        initRequestRecyclerView()
        initSwipe()
    }

    fun setRequestHistory(restRequestHistory: RestRequestHistory) {
        /*
        currentUrl = restRequestHistory.url
        currentMethod = restRequestHistory.method
        headersArrayList = restRequestHistory.headers
        requestArrayList = restRequestHistory.requests
        initElements()
        */
    }

    private fun initButtons() {
        myFragmentView.sendButton.setOnClickListener {
            try {
                restPresenter.currentUrl = myFragmentView.urlEditText.text.toString()
                restPresenter.sendRequest()
                val restRequestHistory = RestRequestHistory(restPresenter.currentUrl, restPresenter.currentUrl, restPresenter.headersArrayList, restPresenter.requestArrayList)
                restRequestHistory.save()
            } catch (e: Exception) {
            }
        }

        myFragmentView.addHeaderButton.setOnClickListener { initDialogForHeader() }

        myFragmentView.addHeaderButton.setOnClickListener { initDialogForRequest() }
    }

    private fun initEditText() {
        if (!restPresenter.currentUrl.contains("http")) {
            restPresenter.currentUrl = "http://" + restPresenter.currentUrl
        }
        myFragmentView.urlEditText.setText(restPresenter.currentUrl)
    }

    private fun initSpinner() {
        methodSpinner = myFragmentView.findViewById<View>(R.id.materialSpinner) as Spinner
        methodSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                restPresenter.currentMethod = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun initHeadersRecyclerView() {
        initRecyclerView(myFragmentView.headersRecyclerView)
        keyValueAdapterHeaders = object : KeyValueAdapter(context, restPresenter.headersArrayList) {
            override fun onLongPress(keyValueModel: KeyValueModel, position: Int) {
                initDialogForEditHeader(keyValueModel, position)
            }
        }

        myFragmentView.headersRecyclerView.adapter = keyValueAdapterHeaders

    }

    private fun initRequestRecyclerView() {
        initRecyclerView(myFragmentView.requestRecyclerView)
        keyValueAdapterRequest = object : KeyValueAdapter(context, restPresenter.requestArrayList) {
            override fun onLongPress(keyValueModel: KeyValueModel, position: Int) {
                initDialogForEditRequest(keyValueModel, position)
            }
        }
        myFragmentView.requestRecyclerView.adapter = keyValueAdapterRequest
    }

    private fun initRecyclerView(recyclerView: RecyclerView?) {
        //recyclerView.setHasFixedSize(true);
        recyclerView!!.addItemDecoration(SimpleDividerItemDecoration(activity))
        val layoutManager = LinearLayoutManager(context)
        layoutManager.isAutoMeasureEnabled = true
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun initSwipe() {
        val itemTouchHelperHeader = ItemTouchHelper(object : ItemTouchCallback(activity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                keyValueAdapterHeaders.removeItem(position)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        })
        itemTouchHelperHeader.attachToRecyclerView(myFragmentView.headersRecyclerView)

        val itemTouchHelperRequest = ItemTouchHelper(object : ItemTouchCallback(activity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                keyValueAdapterRequest.removeItem(position)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        })
        itemTouchHelperRequest.attachToRecyclerView(myFragmentView.requestRecyclerView)
    }


    ///////////////////////////////////////////////////Dialog Part

    /**
     * Dialog where you can add new header
     */
    private fun initDialogForHeader() {
        initDialogVariablesHeader()
        alertDialogBuilder!!.setPositiveButton(R.string.add) { dialog, which ->
            val keyValueModel = KeyValueModel()
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterHeaders.addItem(keyValueModel)
            dialog.dismiss()
        }
        alertDialogBuilder!!.show()

    }

    private fun initDialogForEditHeader(keyValueModel: KeyValueModel, position: Int) {

        initDialogVariablesHeader()
        fillDialogVariables(keyValueModel, true)
        alertDialogBuilder!!.setPositiveButton(R.string.edit) { dialog, which ->
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterHeaders!!.notifyItemChanged(position)
            dialog.dismiss()
        }
        alertDialogBuilder!!.show()
    }


    /**
     * Dialog where you can add new request
     */
    private fun initDialogForRequest() {
        initDialogVariablesRequest()

        alertDialogBuilder!!.setPositiveButton(R.string.add) { dialog, which ->
            val keyValueModel = KeyValueModel()
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterRequest!!.addItem(keyValueModel)
            dialog.dismiss()
        }
        alertDialogBuilder!!.show()
    }

    private fun initDialogForEditRequest(keyValueModel: KeyValueModel, position: Int) {

        initDialogVariablesRequest()
        fillDialogVariables(keyValueModel, false)
        alertDialogBuilder!!.setPositiveButton(R.string.edit) { dialog, which ->
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterRequest!!.notifyItemChanged(position)
            dialog.dismiss()
        }
        alertDialogBuilder!!.show()

    }


    /**
     * Init all views in dialog
     */
    private fun initDialogVariablesHeader() {
        alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = activity.layoutInflater.inflate(R.layout.add_key_value_layout_header, null)
        alertDialogBuilder!!.setView(alertView)
        editTextKey = alertView!!.findViewById<View>(R.id.editTextKey) as EditText
        editTextValue = alertView!!.findViewById<View>(R.id.editTextValue) as EditText
        keySpinner = alertView!!.findViewById<View>(R.id.materialSpinnerHeaderKey) as Spinner
        valueSpinner = alertView!!.findViewById<View>(R.id.materialSpinnerHeaderValue) as Spinner
        setDialogSpinnerKey()
    }

    /**
     * Init all views in dialog
     */
    private fun initDialogVariablesRequest() {
        alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = activity.layoutInflater.inflate(R.layout.add_key_value_layout_request, null)
        alertDialogBuilder!!.setView(alertView)
        editTextKey = alertView!!.findViewById<View>(R.id.editTextKey) as EditText
        editTextValue = alertView!!.findViewById<View>(R.id.editTextValue) as EditText
    }

    private fun fillDialogVariables(keyValueModel: KeyValueModel, hasSpinners: Boolean) {
        editTextKey!!.setText(keyValueModel.key)
        editTextValue!!.setText(keyValueModel.value)
        if (hasSpinners) {
            val keyIndex = keyParamsList!!.indexOf(keyValueModel.key)
            if (keyIndex > 0) {
                keySpinner!!.setSelection(keyIndex)
                valueSpinner!!.visibility = View.VISIBLE
                setDialogSpinnerValue(keyValueModel.key)
                val valueIndex = valueParamsList!!.indexOf(keyValueModel.value)
                if (valueIndex > 0) {
                    valueSpinner!!.setSelection(valueIndex)
                } else {
                    valueSpinner!!.setSelection(0)
                }

            } else {
                keySpinner!!.setSelection(0)
                valueSpinner!!.visibility = View.GONE
            }
        }
    }

    private fun setDialogSpinnerKey() {
        keyParamsList = ArrayList()
        keyParamsList!!.add(getString(R.string.custom_header))
        keyParamsList!!.addAll(AllHeaders.getHeadersHashmap().keys)
        val keySpinnerAdapter = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, keyParamsList!!)
        keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        keySpinner!!.visibility = View.VISIBLE
        keySpinner!!.adapter = keySpinnerAdapter
        keySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if (item == getString(R.string.custom_header)) {
                    editTextKey!!.setText("")
                    valueSpinner!!.visibility = View.GONE
                } else {
                    editTextKey!!.setText(item)
                    setDialogSpinnerValue(item)
                }
                editTextValue!!.setText("")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setDialogSpinnerValue(key: String) {
        valueParamsList = ArrayList()
        valueParamsList!!.add(getString(R.string.custom_value))
        valueParamsList!!.addAll(AllHeaders.getHeadersHashmap()[key] as ArrayList<String>)
        if (valueParamsList!!.size == 1) {
            valueSpinner!!.visibility = View.GONE
            return
        }
        val valueSpinnerAdapter = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, valueParamsList!!)
        valueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        valueSpinner!!.visibility = View.VISIBLE
        valueSpinner!!.adapter = valueSpinnerAdapter
        valueSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if (item == getString(R.string.custom_value)) {
                    editTextValue!!.setText("")
                } else {
                    editTextValue!!.setText(item)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun setResponse(responseDev: ResponseDev) {

    }
}
