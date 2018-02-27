package app.deadmc.devnetworktool.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.support.v7.widget.Toolbar
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.constants.FULL_VIEW
import app.deadmc.devnetworktool.helpers.formatString
import app.deadmc.devnetworktool.helpers.safe
import app.deadmc.devnetworktool.interfaces.views.FullView
import app.deadmc.devnetworktool.presenters.FullViewPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.activity_full_view.*

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
        safe {
            toolbar = findViewById<View>(R.id.toobar) as Toolbar
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
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
        jsonTextView.text = formatString(text)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setResultWebView(stringId: Int, text: String, url:String) {
        toolbar?.setTitle(stringId)
        setTitle(stringId)
        webView.settings.javaScriptEnabled = true
        webView.visibility = View.VISIBLE
        val mimeType = "text/html"
        val encoding = "UTF-8"
        webView.loadDataWithBaseURL(url, text, mimeType, encoding, url)
    }
}
