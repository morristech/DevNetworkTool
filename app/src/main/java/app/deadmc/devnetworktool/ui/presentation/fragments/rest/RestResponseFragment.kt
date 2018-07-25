package app.deadmc.devnetworktool.ui.presentation.fragments.rest

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.activities.FullViewActivity
import app.deadmc.devnetworktool.ui.adapters.ParametersAdapter
import app.deadmc.devnetworktool.constants.FULL_VIEW
import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import app.deadmc.devnetworktool.utils.*
import app.deadmc.devnetworktool.ui.presentation.views.FullView
import app.deadmc.devnetworktool.ui.presentation.views.RestResponseView
import app.deadmc.devnetworktool.data.models.ResponseDev
import app.deadmc.devnetworktool.ui.presentation.presenters.BasePresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.FullViewPresenter
import app.deadmc.devnetworktool.ui.presentation.presenters.RestResponsePresenter
import app.deadmc.devnetworktool.ui.views.CollapseLinearLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import kotlinx.android.synthetic.main.fragment_rest_response.*
import kotlinx.android.synthetic.main.fragment_rest_response.view.*
import java.util.*

class RestResponseFragment : BaseFragment(), RestResponseView, FullView {

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var responseRestPresenter: RestResponsePresenter

    @InjectPresenter(type = PresenterType.WEAK, tag = FULL_VIEW)
    lateinit var fullViewPresenter: FullViewPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_response, container, false)
        initElements()
        return myFragmentView
    }

    override fun getPresenter(): BasePresenter<*> {
        return responseRestPresenter
    }

    @ProvidePresenter(type = PresenterType.WEAK)
    fun providePresenter(): FullViewPresenter {
        return FullViewPresenter()
    }

    @ProvidePresenterTag(presenterClass = FullViewPresenter::class, type = PresenterType.WEAK)
    fun providePresenterTag(): String {
        return FULL_VIEW
    }

    fun initElements() {
        initLayout(myFragmentView.collapseLinearLayoutStats, myFragmentView.statsTitleLayout, myFragmentView.imageViewArrowStats)
        initLayout(myFragmentView.collapseLinearLayoutHeaders, myFragmentView.headerTitleLayout, myFragmentView.imageViewArrowHeader)
        initLayout(myFragmentView.collapseLinearLayoutRequest, myFragmentView.requestTitleLayout, myFragmentView.imageViewArrowRequest)
        initLayout(myFragmentView.collapseLinearLayoutError, myFragmentView.errorTitleLayout, myFragmentView.imageViewArrowError)
        initRecyclerViews(myFragmentView.headersRecyclerView, null)
        initRecyclerViews(myFragmentView.statsRecyclerView, null)
        hideButton()
        successResponseLayout.visibility = View.GONE
        errorResponseLayout.visibility = View.GONE
    }

    fun hideButton() {
        myFragmentView.watchButton.visibility = View.GONE
    }

    fun initButton(responseDev: ResponseDev) {
        myFragmentView.watchButton.visibility = View.VISIBLE

        val type = getTypeOfString(responseDev.body)
        when (type) {
            JSON -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.json)
            XML -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.xml)
            HTML -> myFragmentView.watchButton.text = getString(R.string.open_as) + " " + getString(R.string.html)
            UNDEFINED -> myFragmentView.watchButton.visibility = View.GONE
        }

        myFragmentView.watchButton.setOnClickListener({
            fullViewPresenter.text = responseDev.body
            //fullViewPresenter.url = restPresenter.currentUrl
            val intent = Intent(context, FullViewActivity::class.java)
            startActivity(intent)
        })
    }

    fun initLayout(collapseLinearLayout: CollapseLinearLayout, titleLayout: LinearLayout, imageView: ImageView) {
        collapseLinearLayout.collapse()

        titleLayout.setOnClickListener {
            if (collapseLinearLayout.isCollapsed) {
                collapseLinearLayout.expand()
            } else {
                collapseLinearLayout.collapse()
            }
            rotateImage(imageView, collapseLinearLayout.isCollapsed, 300)
        }
        rotateImage(imageView, collapseLinearLayout.isCollapsed, 0)
    }


    override fun setSuccessResponse(responseDev: ResponseDev) {
        errorResponseLayout.visibility = View.GONE
        successResponseLayout.visibility = View.VISIBLE
        initHeadersRecyclerView(responseDev.headers)
        initStatsRecyclerView(responseDev.code, responseDev.delay)
        bodyTextView!!.text = responseDev.body
        initButton(responseDev)
        collapseLinearLayoutStats.expand()
    }

    override fun setErrorResponse(responseDev: ResponseDev) {
        successResponseLayout.visibility = View.GONE
        errorResponseLayout.visibility = View.VISIBLE
        errorTextView.text = responseDev.error?.localizedMessage ?: getString(R.string.no_error_description)
        collapseLinearLayoutError.expand()
    }

    private fun initRecyclerViews(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?) {
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }


    private fun initStatsRecyclerView(code: Int, responseTime: Int) {
        val arrayList = ArrayList<Spanned>()
        arrayList.add(fromHtml(getString(R.string.response_code) + " " + code))
        arrayList.add(fromHtml(getString(R.string.response_time) + " " + responseTime + " " + getString(R.string.ms)))
        val parametersAdapter = ParametersAdapter(context!!, arrayList)
        initRecyclerViews(statsRecyclerView, parametersAdapter)
    }

    private fun initHeadersRecyclerView(headers: String) {
        val arrayList = ArrayList<Spanned>()
        val stringArray = headers.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (head in stringArray) {
            val index = head.indexOf(":")
            val key = head.substring(0, index)
            val value = head.substring(index, head.length)
            arrayList.add(fromHtml("<b>$key</b>$value"))
        }

        val parametersAdapter = ParametersAdapter(context!!, arrayList)
        initRecyclerViews(headersRecyclerView, parametersAdapter)
    }

    override fun hide() {
    }

    override fun setResultWebView(stringId: Int, text: String, url: String) {
    }

    override fun setResult(stringId: Int, text: String) {
    }

}
