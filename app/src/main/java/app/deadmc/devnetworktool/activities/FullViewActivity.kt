package app.deadmc.devnetworktool.activities

import android.os.Bundle
import android.view.View
import android.support.v7.widget.Toolbar
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.FULL_VIEW
import app.deadmc.devnetworktool.helpers.StringHelper
import app.deadmc.devnetworktool.interfaces.views.FullView
import app.deadmc.devnetworktool.presenters.FullViewPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.activity_full_view.*
import app.deadmc.devnetworktool.R.id.webView



class FullViewActivity : BaseActivity(), FullView {

    private var toolbar: Toolbar? = null

    @InjectPresenter(type = PresenterType.GLOBAL, tag = FULL_VIEW)
    lateinit var fullViewPresenter: FullViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_view)
        initElements()
    }

    fun initElements() {
        initToolbar()
        fullViewPresenter.setViewByType()
    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toobar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun hide() {
        webView.visibility = View.GONE
        scrollView.visibility = View.GONE
    }

    override fun setResult(stringId: Int, text: String) {
        toolbar?.setTitle(stringId)
        setTitle(stringId)
        scrollView.visibility = View.VISIBLE
        jsonTextView.text = StringHelper.formatString(text)
    }

    override fun setResultWebView(stringId: Int, text: String, url:String) {
        toolbar?.setTitle(stringId)
        setTitle(stringId)
        webView.getSettings().javaScriptEnabled = true
        webView.visibility = View.VISIBLE
        //webView.loadData(text, "text/html; charset=utf-8", "UTF-8")
        val mimeType = "text/html"
        val encoding = "UTF-8"
        webView.loadDataWithBaseURL(url, text, mimeType, encoding, url)
    }
}
