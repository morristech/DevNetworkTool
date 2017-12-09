package app.deadmc.devnetworktool.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.widget.ScrollView
import android.widget.TextView
import android.support.v7.widget.Toolbar

import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.helpers.FileFormatHelper
import app.deadmc.devnetworktool.helpers.StringHelper
import app.deadmc.devnetworktool.models.ResponseDev
import app.deadmc.devnetworktool.singletons.SharedData

class FullViewActivity : AppCompatActivity() {

    private var responseDev: ResponseDev? = null
    private var scrollView: ScrollView? = null
    private var textView: TextView? = null
    private var webView: WebView? = null
    private var toolbar: Toolbar? = null

    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_view)
        initValues()
        initElements()
        setViewDependsOnType()
    }

    fun initValues() {
        responseDev = SharedData.getInstance().responseDev
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("text")) {
            text = bundle.getString("text")
            return
        }

        if (responseDev != null) {
            text = responseDev!!.body
        }

    }

    fun initElements() {
        if (text == null)
            return
        textView = findViewById<View>(R.id.jsonTextView) as TextView
        scrollView = findViewById<View>(R.id.scrollView) as ScrollView
        webView = findViewById<View>(R.id.webView) as WebView
        initToolbar()
        textView!!.text = StringHelper.formatString(text)
        setViewDependsOnType()
    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toobar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    fun setViewDependsOnType() {
        if (text == null) {
            return
        }

        val type = FileFormatHelper.getTypeOfString(text)
        webView!!.visibility = View.GONE
        scrollView!!.visibility = View.GONE
        when (type) {
            FileFormatHelper.JSON -> {
                toolbar!!.setTitle(R.string.json)
                scrollView!!.visibility = View.VISIBLE
                textView!!.text = StringHelper.formatString(text)
            }
            FileFormatHelper.XML -> {
                toolbar!!.setTitle(R.string.xml)
                scrollView!!.visibility = View.VISIBLE
                textView!!.text = StringHelper.formatString(text)
            }
            FileFormatHelper.HTML -> {
                toolbar!!.setTitle(R.string.html)
                webView!!.visibility = View.VISIBLE
                webView!!.loadData(text, "text/html; charset=utf-8", "UTF-8")
            }
            FileFormatHelper.UNDEFINED -> {
                scrollView!!.visibility = View.VISIBLE
                textView!!.text = StringHelper.formatString(text)
            }
        }//webView.loadUrl("http://google.com");
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
