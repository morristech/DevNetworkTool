package app.deadmc.devnetworktool.ui.presentation.fragments.rest


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
import app.deadmc.devnetworktool.ui.adapters.KeyValueAdapter
import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import app.deadmc.devnetworktool.utils.AllHeaders
import app.deadmc.devnetworktool.utils.safe
import app.deadmc.devnetworktool.ui.presentation.views.RestRequestView
import app.deadmc.devnetworktool.data.models.KeyValueModel
import app.deadmc.devnetworktool.data.models.RestRequestHistory
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.RestRequestPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_rest_request.view.*
import java.util.*

class RestRequestFragment : BaseFragment(), RestRequestView {

    @InjectPresenter
    lateinit var restPresenter: RestRequestPresenter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getPresenter(): BasePresenter<*> {
        return restPresenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_request, container, false)
        initElements()
        return myFragmentView
    }

    fun initElements() {
        //super.initElements();
        initButtons()
        initEditText()
        initSpinner()
        initRequestRecyclerView()
        initHeadersRecyclerView()
    }

    override fun loadRestHistory(restRequestHistory: RestRequestHistory) {
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
            } catch (e: Exception) {
                Crashlytics.logException(e)
            }
        }

        myFragmentView.addHeaderButton.setOnClickListener { restPresenter.showDialogForHeader(restPresenter.keyValueModel) }
        myFragmentView.addRequestButton.setOnClickListener { restPresenter.showDialogForRequest(restPresenter.keyValueModel) }
    }

    private fun initEditText() {
        if (!restPresenter.currentUrl.contains("http")) {
            restPresenter.currentUrl = "http://" + restPresenter.currentUrl
        }
        myFragmentView.urlEditText.setText(restPresenter.currentUrl)
        myFragmentView.urlEditText.setSelection(myFragmentView.urlEditText.text.length)
    }

    private fun initSpinner() {
        methodSpinner = myFragmentView.findViewById<View>(R.id.materialSpinner) as Spinner
        methodSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                restPresenter.currentMethod = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        val array:Array<String> = resources.getStringArray(R.array.methods)
        methodSpinner?.setSelection( array.binarySearch(restPresenter.currentMethod))

    }

    private fun initHeadersRecyclerView() {
        safe {
            initRecyclerView(myFragmentView.headersRecyclerView)
            keyValueAdapterHeaders = object : KeyValueAdapter(restPresenter.headersArrayList) {
                override fun onEditItem(element: KeyValueModel, position: Int) {
                    restPresenter.showDialogForHeader(element, position)
                }
            }

            myFragmentView.headersRecyclerView.adapter = keyValueAdapterHeaders
        }

    }

    private fun initRequestRecyclerView() {
        safe {
            initRecyclerView(myFragmentView.requestRecyclerView)
            keyValueAdapterRequest = object : KeyValueAdapter(restPresenter.requestArrayList) {
                override fun onEditItem(element: KeyValueModel, position: Int) {
                    restPresenter.showDialogForRequest(element, position)
                }
            }
            myFragmentView.requestRecyclerView.adapter = keyValueAdapterRequest
        }
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
    override fun showDialogForHeader(keyValueModel: KeyValueModel, position: Int) {
        if (checkActivityIsFinishing())
            return
        initDialogVariablesHeader()
        fillDialogVariables(keyValueModel, true)
        alertDialogBuilder?.setPositiveButton(R.string.edit) { _, _ ->
            Log.e(TAG,"apply")
            val isNew = keyValueModel.isEmpty()
            keyValueModel.key = editTextKey?.text.toString()
            keyValueModel.value = editTextValue?.text.toString()
            if (isNew) {
                keyValueAdapterHeaders.addItem(keyValueModel)
            } else {
                keyValueAdapterHeaders.notifyItemChanged(position)
            }
            restPresenter.hideDialog()
        }
        initDialogEvents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        editTextKey?.let {
            restPresenter.keyValueModel.key = it.text.toString()
        }

        editTextValue?.let {
            restPresenter.keyValueModel.value = it.text.toString()
        }

        currentDialog?.setOnDismissListener(null)
        currentDialog?.dismiss()
    }


    override fun hideDialog() {
        restPresenter.keyValueModel = KeyValueModel()
        currentDialog?.dismiss()
        activity?.hideKeyboard()
    }

    /**
     * @param keyValueModel
     * @param position has -1 value if not specified
     */
    override fun showDialogForRequest(keyValueModel: KeyValueModel, position:Int) {
        if (checkActivityIsFinishing())
            return
        val isNew = keyValueModel.isEmpty()
        initDialogVariablesRequest()
        fillDialogVariables(keyValueModel, false)
        alertDialogBuilder?.setPositiveButton(R.string.edit) { _, _ ->
            keyValueModel.key = editTextKey?.text.toString()
            keyValueModel.value = editTextValue?.text.toString()
            if (isNew || position ==-1) {
                keyValueAdapterRequest.addItem(keyValueModel)
            } else {
                keyValueAdapterRequest.notifyItemChanged(position)
            }
            restPresenter.hideDialog()
        }
        initDialogEvents()
    }


    /**
     * Init all views in header dialog
     */
    private fun initDialogVariablesHeader() {
        alertDialogBuilder = AlertDialog.Builder(context!!, R.style.AppTheme_Dialog_Alert)
        alertView = activity?.layoutInflater?.inflate(R.layout.dialog_key_value_header, null)
        alertDialogBuilder?.setView(alertView)
        editTextKey = alertView?.findViewById(R.id.editTextKey)
        editTextValue = alertView?.findViewById(R.id.editTextValue)
        keySpinner = alertView?.findViewById(R.id.materialSpinnerHeaderKey)
        valueSpinner = alertView?.findViewById(R.id.materialSpinnerHeaderValue)
        setDialogSpinnerKey()
    }

    /**
     * Init all views in request dialog
     */
    private fun initDialogVariablesRequest() {
        alertDialogBuilder = AlertDialog.Builder(context!!, R.style.AppTheme_Dialog_Alert)
        alertView = activity?.layoutInflater?.inflate(R.layout.dialog_add_key_value_request, null)
        alertDialogBuilder?.setView(alertView)
        editTextKey = alertView?.findViewById(R.id.editTextKey)
        editTextValue = alertView?.findViewById(R.id.editTextValue)
    }

    private fun initDialogEvents() {
        alertDialogBuilder?.setOnDismissListener {
            restPresenter.hideDialog()
        }
        currentDialog = alertDialogBuilder?.create()
        currentDialog?.show()
    }

    private fun fillDialogVariables(keyValueModel: KeyValueModel, hasSpinners: Boolean) {
        if (hasSpinners) {
            val keyIndex = keyParamsList.indexOf(keyValueModel.key)
            if (keyIndex > 0) {
                keySpinner?.setSelection(keyIndex)
                valueSpinner?.visibility = View.VISIBLE
                setDialogSpinnerValue(keyValueModel.key)
                val valueIndex = valueParamsList.indexOf(keyValueModel.value)
                if (valueIndex > 0) {
                    valueSpinner?.setSelection(valueIndex)
                } else {
                    valueSpinner?.setSelection(0)
                }
            } else {
                keySpinner?.setSelection(0)
                valueSpinner?.visibility = View.GONE
            }
        }

        editTextKey?.setText(keyValueModel.key)
        editTextValue?.setText(keyValueModel.value)


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
                    valueSpinner?.visibility = View.GONE
                } else {
                    editTextKey?.setText(item)
                    setDialogSpinnerValue(item)
                }
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
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if (item == getString(R.string.custom_value)) {
                } else {
                    editTextValue?.setText(item)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
