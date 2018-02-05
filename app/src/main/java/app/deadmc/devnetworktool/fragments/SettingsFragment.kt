package app.deadmc.devnetworktool.fragments

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.getStringArrayListOfUnits
import app.deadmc.devnetworktool.helpers.getStringFromTimeUnit
import app.deadmc.devnetworktool.helpers.toCapitalizedFirstLetterString
import app.deadmc.devnetworktool.interfaces.views.SettingsView
import app.deadmc.devnetworktool.presenters.SettingsPresenter
import app.deadmc.devnetworktool.shared_preferences.DevPreferences
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.text.FieldPosition


class SettingsFragment : BaseFragment(), SettingsView {

    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater!!.inflate(R.layout.fragment_settings, container, false)
        initGeneral()
        initRest()
        return myFragmentView
    }

    fun initGeneral() {

    }

    private fun initRest() {
        myFragmentView.disableSslSwitchCompat.isChecked = DevPreferences.disableSsl
        myFragmentView.disableSslSwitchCompat.setOnCheckedChangeListener({ _, isChecked ->
            DevPreferences.disableSsl = isChecked
        })

        myFragmentView.timeoutLayout.setOnClickListener {
            settingsPresenter.showRestTimeoutDialog()
        }
    }

    override fun showRestTimeoutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        val alertView = activity.layoutInflater.inflate(R.layout.dialog_choose_timeout, null)
        alertDialogBuilder.setView(alertView)
        val timeoutEditText = alertView?.findViewById<EditText>(R.id.timeoutEditText)
        timeoutEditText?.setText(DevPreferences.restTimeoutAmount.toString())
        val timeoutUnitSpinner = alertView?.findViewById<Spinner>(R.id.timeoutUnitSpinner)
        val valueSpinnerAdapter = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, getStringArrayListOfUnits())
        valueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeoutUnitSpinner?.visibility = View.VISIBLE
        timeoutUnitSpinner?.adapter = valueSpinnerAdapter
        timeoutUnitSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        timeoutUnitSpinner?.setSelection(valueSpinnerAdapter.getPosition(getStringFromTimeUnit(DevPreferences.restTimeoutUnit).toCapitalizedFirstLetterString()))
        alertDialogBuilder.show()

    }

}
