package app.deadmc.devnetworktool.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.helpers.FileFormatHelper;
import app.deadmc.devnetworktool.helpers.StringHelper;
import app.deadmc.devnetworktool.models.ResponseDev;
import app.deadmc.devnetworktool.singletons.SharedData;

public class FullViewActivity extends AppCompatActivity {

    private ResponseDev responseDev;
    private ScrollView scrollView;
    private TextView textView;
    private WebView webView;
    private Toolbar toolbar;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);
        initValues();
        initElements();
        setViewDependsOnType();
    }

    public void initValues() {
        responseDev = SharedData.getInstance().getResponseDev();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("text")) {
            text = bundle.getString("text");
            return;
        }

        if (responseDev != null) {
            text = responseDev.getBody();
        }

    }

    public void initElements() {
        if (text == null)
            return;
        textView = (TextView) findViewById(R.id.jsonTextView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        webView = (WebView) findViewById(R.id.webView);
        initToolbar();
        textView.setText(StringHelper.formatString(text));
        setViewDependsOnType();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setViewDependsOnType() {
        if (text == null) {
            return;
        }

        int type = FileFormatHelper.getTypeOfString(text);
        webView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        switch (type) {
            case FileFormatHelper.JSON:
                toolbar.setTitle(R.string.json);
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
            case FileFormatHelper.XML:
                toolbar.setTitle(R.string.xml);
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
            case FileFormatHelper.HTML:
                toolbar.setTitle(R.string.html);
                webView.setVisibility(View.VISIBLE);
                webView.loadData(text, "text/html; charset=utf-8", "UTF-8");
                //webView.loadUrl("http://google.com");
                break;
            case FileFormatHelper.UNDEFINED:
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
