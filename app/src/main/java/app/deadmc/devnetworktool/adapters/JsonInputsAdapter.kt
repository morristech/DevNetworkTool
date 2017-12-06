package app.deadmc.devnetworktool.adapters

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.models.JsonInput
import kotlinx.android.synthetic.main.item_key_value.view.*
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.item_recycler_view_inputs.view.*

class JsonInputsAdapter(val jsonInputsList:ArrayList<JsonInput>,
                        val fullJsonEditText: EditText):BaseSwipeAdapter<JsonInput>(jsonInputsList, R.layout.item_recycler_view_inputs_swipe, onlyDelete = true) {


    override fun onDeleteItem(element: JsonInput) { }

    override fun onEditItem(element: JsonInput, position: Int) {}

    override fun onClickItem(element: JsonInput, position: Int) {}

    override fun onBindViewHolder(viewHolder: BaseSwipeAdapter<JsonInput>.ViewHolder, position: Int) {
        // Get the data model based on position
        val jsonInput = jsonInputsList.get(position)

        viewHolder.itemView.layoutInputs.editTextKey

        viewHolder.itemView.layoutInputs.editTextKey.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                jsonInput.key = s.toString()
                if (s != "") {
                    setUpdatedJson()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        viewHolder.itemView.layoutInputs.editTextValue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                jsonInput.value = s.toString()
                if (s != "") {
                    setUpdatedJson()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        super.onBindViewHolder(viewHolder,position)
    }


    private fun setUpdatedJson() {
        val jsonObject = JSONObject()
        try {
            for (jsonInput in jsonInputsList) {
                if (!jsonInput.key.isEmpty())
                    jsonObject.put(jsonInput.key, jsonInput.value)
            }
        } catch (e: JSONException) {
        }

        fullJsonEditText.setText(jsonObject.toString())
    }

}