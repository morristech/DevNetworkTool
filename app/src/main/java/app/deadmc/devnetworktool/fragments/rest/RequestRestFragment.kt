package app.deadmc.devnetworktool.fragments.rest


import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.adapters.KeyValueAdapter
import app.deadmc.devnetworktool.constants.REST
import app.deadmc.devnetworktool.fragments.BaseFragment
import app.deadmc.devnetworktool.helpers.AllHeaders
import app.deadmc.devnetworktool.interfaces.views.RestDialogsView
import app.deadmc.devnetworktool.interfaces.views.RestView
import app.deadmc.devnetworktool.models.KeyValueModel
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.models.RestRequestHistory
import app.deadmc.devnetworktool.presenters.RestDialogsPresenter
import app.deadmc.devnetworktool.presenters.RestPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_rest_request.view.*
import java.util.*

class RequestRestFragment : BaseFragment(), RestView, RestDialogsView {

    @InjectPresenter(type = PresenterType.WEAK, tag = REST)
    lateinit var restPresenter: RestPresenter

    @InjectPresenter
    lateinit var restDialogsPresenter: RestDialogsPresenter

    private var methodSpinner: Spinner? = null
    private lateinit var keyValueAdapterHeaders: KeyValueAdapter
    private lateinit var keyValueAdapterRequest: KeyValueAdapter

    //for dialog
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var alertView: View? = null
    private var currentDialog: AlertDialog? = null
    private var editTextKey: EditText? = null
    private var editTextValue: EditText? = null
    private var keySpinner: Spinner? = null
    private var valueSpinner: Spinner? = null
    private var keyParamsList: ArrayList<String> = ArrayList()
    private var valueParamsList: ArrayList<String> = ArrayList()
    private var savedInstanceLaunch = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_rest_request, container, false)
        initElements()
        savedInstanceLaunch = false
        return myFragmentView
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    fun initElements() {
        //super.initElements();
        initButtons()
        initEditText()
        initSpinner()
        initRequestRecyclerView()
        initHeadersRecyclerView()
    }

    override fun loadRequestHistory(restRequestHistory: RestRequestHistory) {
        Log.e(TAG, "loadRequestHistory called " + restRequestHistory.method)
        restPresenter.currentMethod = restRequestHistory.method
        restPresenter.currentUrl = restRequestHistory.url
        restPresenter.headersArrayList = restRequestHistory.getHeaders()
        restPresenter.requestArrayList = restRequestHistory.getRequests()
        initElements()
    }

    private fun initButtons() {
        myFragmentView.sendButton.setOnClickListener {
            try {
                restPresenter.currentUrl = myFragmentView.urlEditText.text.toString()
                restPresenter.currentMethod = myFragmentView.materialSpinner.selectedItem.toString()
                restPresenter.sendRequest()
                RestRequestHistory(restPresenter.currentUrl, restPresenter.currentMethod, restPresenter.headersArrayList, restPresenter.requestArrayList).save()
            } catch (e: Exception) {
                Crashlytics.logException(e)
            }
        }

        myFragmentView.addHeaderButton.setOnClickListener { restDialogsPresenter.showDialogForHeader() }
        myFragmentView.addRequestButton.setOnClickListener { restDialogsPresenter.showDialogForRequest() }
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
        keyValueAdapterHeaders = object : KeyValueAdapter(restPresenter.headersArrayList) {
            override fun onEditItem(element: KeyValueModel, position: Int) {
                restDialogsPresenter.showDialogForEditHeader(element, position)
            }
        }

        myFragmentView.headersRecyclerView.adapter = keyValueAdapterHeaders

    }

    private fun initRequestRecyclerView() {
        initRecyclerView(myFragmentView.requestRecyclerView)
        keyValueAdapterRequest = object : KeyValueAdapter(restPresenter.requestArrayList) {
            override fun onEditItem(element: KeyValueModel, position: Int) {
                restDialogsPresenter.showDialogForEditRequest(element, position)
            }
        }
        myFragmentView.requestRecyclerView.adapter = keyValueAdapterRequest
    }

    private fun initRecyclerView(recyclerView: RecyclerView?) {
        //recyclerView.setHasFixedSize(true);
        val layoutManager = LinearLayoutManager(context)
        layoutManager.isAutoMeasureEnabled = true
        recyclerView?.layoutManager = layoutManager
        recyclerView?.isNestedScrollingEnabled = false
    }

    ///////////////////////////////////////////////////Dialog Part

    /**
     * Dialog where you can add new header
     */
    override fun showDialogForHeader() {
        initDialogVariablesHeader()
        alertDialogBuilder?.setPositiveButton(R.string.add) { _, _ ->
            val keyValueModel = KeyValueModel()
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterHeaders.addItem(keyValueModel)
            restDialogsPresenter.hideDialog()
        }
        initDialogEventsHeader()
    }

    override fun showDialogForEditHeader(keyValueModel: KeyValueModel, position: Int) {
        initDialogVariablesHeader()
        fillDialogVariables(keyValueModel, true)
        alertDialogBuilder?.setPositiveButton(R.string.edit) { _, _ ->
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterHeaders.notifyItemChanged(position)
            restDialogsPresenter.hideDialog()
        }
        initDialogEventsHeader()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        savedInstanceLaunch = true

        editTextKey?.let {
            Log.e(TAG, "editTextKey " + it.text.toString())
            restDialogsPresenter.currentKey = it.text.toString()
        }

        editTextValue?.let {
            restDialogsPresenter.currentValue = it.text.toString()
        }

        currentDialog?.dismiss()
    }


    /**
     * Dialog where you can add new request
     */
    override fun showDialogForRequest() {
        initDialogVariablesRequest()

        alertDialogBuilder?.setPositiveButton(R.string.add) { _, _ ->
            val keyValueModel = KeyValueModel()
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterRequest.addItem(keyValueModel)
            initDialogEventsRequest()
        }

        alertDialogBuilder?.setOnDismissListener {
            if (!savedInstanceLaunch)
                restDialogsPresenter.hideDialog()
        }
        alertDialogBuilder?.show()
    }

    override fun hideDialog() {
        restDialogsPresenter.currentKey = ""
        restDialogsPresenter.currentValue = ""
        currentDialog?.dismiss()
    }

    override fun showDialogForEditRequest(keyValueModel: KeyValueModel, position: Int) {
        initDialogVariablesRequest()
        fillDialogVariables(keyValueModel, false)
        alertDialogBuilder?.setPositiveButton(R.string.edit) { _, _ ->
            keyValueModel.key = editTextKey!!.text.toString()
            keyValueModel.value = editTextValue!!.text.toString()
            keyValueAdapterRequest.notifyItemChanged(position)
            restDialogsPresenter.hideDialog()
        }

        initDialogEventsRequest()
    }


    /**
     * Init all views in dialog
     */
    private fun initDialogVariablesHeader() {
        alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = activity.layoutInflater.inflate(R.layout.dialog_key_value_header, null)
        alertDialogBuilder?.setView(alertView)
        editTextKey = alertView?.findViewById<View>(R.id.editTextKey) as EditText
        editTextValue = alertView?.findViewById<View>(R.id.editTextValue) as EditText
        keySpinner = alertView?.findViewById<View>(R.id.materialSpinnerHeaderKey) as Spinner
        valueSpinner = alertView?.findViewById<View>(R.id.materialSpinnerHeaderValue) as Spinner
        setDialogSpinnerKey()
        editTextKey?.setText(restDialogsPresenter.currentKey)
        editTextValue?.setText(restDialogsPresenter.currentValue)
    }

    private fun initDialogEventsHeader() {
        alertDialogBuilder?.setOnDismissListener {
            if (!savedInstanceLaunch)
                hideDialog()
        }
        currentDialog = alertDialogBuilder?.create()
        currentDialog?.show()
    }

    /**
     * Init all views in dialog
     */
    private fun initDialogVariablesRequest() {
        alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        alertView = activity.layoutInflater.inflate(R.layout.dialog_add_key_value_request, null)
        alertDialogBuilder?.setView(alertView)
        editTextKey = alertView?.findViewById<View>(R.id.editTextKey) as EditText
        editTextValue = alertView?.findViewById<View>(R.id.editTextValue) as EditText
        editTextKey?.setText(restDialogsPresenter.currentKey)
        editTextValue?.setText(restDialogsPresenter.currentValue)
    }

    private fun initDialogEventsRequest() {
        alertDialogBuilder?.setOnDismissListener {
            if (!savedInstanceLaunch)
                hideDialog()
        }
        currentDialog = alertDialogBuilder?.create()
        currentDialog?.show()
    }

    private fun fillDialogVariables(keyValueModel: KeyValueModel, hasSpinners: Boolean) {
        editTextKey!!.setText(keyValueModel.key)
        editTextValue!!.setText(keyValueModel.value)
        if (hasSpinners) {
            val keyIndex = keyParamsList.indexOf(keyValueModel.key)
            if (keyIndex > 0) {
                keySpinner!!.setSelection(keyIndex)
                valueSpinner!!.visibility = View.VISIBLE
                setDialogSpinnerValue(keyValueModel.key)
                val valueIndex = valueParamsList.indexOf(keyValueModel.value)
                if (valueIndex > 0) {
                    valueSpinner!!.setSelection(valueIndex)
                } else {
                    valueSpinner!!.setSelection(0)
                }

            } else {
                keySpinner?.setSelection(0)
                valueSpinner?.visibility = View.GONE
            }
        }
    }

    private fun setDialogSpinnerKey() {
        keyParamsList = ArrayList()
        keyParamsList.add(getString(R.string.custom_header))
        keyParamsList.addAll(AllHeaders.headersHashmap.keys)
        val keySpinnerAdapter = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, keyParamsList)
        keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        keySpinner?.visibility = View.VISIBLE
        keySpinner?.adapter = keySpinnerAdapter
        keySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if (item == getString(R.string.custom_header)) {
                    editTextKey?.setText("")
                    valueSpinner?.visibility = View.GONE
                } else {
                    editTextKey?.setText(item)
                    setDialogSpinnerValue(item)
                }
                editTextValue?.setText("")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setDialogSpinnerValue(key: String) {
        valueParamsList = ArrayList()
        valueParamsList.add(getString(R.string.custom_value))
        valueParamsList.addAll(AllHeaders.headersHashmap[key] as ArrayList<String>)
        if (valueParamsList.size == 1) {
            valueSpinner?.visibility = View.GONE
            return
        }
        val valueSpinnerAdapter = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, valueParamsList)
        valueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        valueSpinner?.visibility = View.VISIBLE
        valueSpinner?.adapter = valueSpinnerAdapter
        valueSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if (item == getString(R.string.custom_value)) {
                    editTextValue?.setText("")
                } else {
                    editTextValue?.setText(item)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun setResponse(responseDev: ResponseDev) {

    }
}
