package app.deadmc.devnetworktool.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.helpers.FileFormatHelper;
import app.deadmc.devnetworktool.helpers.StringHelper;

/**
 * Created by Feren on 25.09.2016.
 */
public class FormattedJsonFragment extends BaseFragment {


    private String text = "";
    private ScrollView scrollView;
    private TextView textView;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView =  inflater.inflate(R.layout.fragment_formatted_json, container, false);
        initElements();
        return myFragmentView;
    }

    public void initElements() {
        //super.initElements();
        textView = (TextView) myFragmentView.findViewById(R.id.jsonTextView);
        scrollView = (ScrollView) myFragmentView.findViewById(R.id.scrollView);
        webView = (WebView) myFragmentView.findViewById(R.id.webView);
        textView.setText(StringHelper.formatString(text));
        setViewDependsOnType();
    }

    public void setViewDependsOnType() {
        if (text == null)
            return;
        int type = FileFormatHelper.getTypeOfString(text);
        webView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        switch (type) {
            case FileFormatHelper.JSON:
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
            case FileFormatHelper.XML:
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
            case FileFormatHelper.HTML:
                webView.setVisibility(View.VISIBLE);
                webView.loadData(text, "text/html; charset=utf-8", "UTF-8");
                break;
            case FileFormatHelper.UNDEFINED:
                scrollView.setVisibility(View.VISIBLE);
                textView.setText(StringHelper.formatString(text));
                break;
        }
    }

    public void setJsonText(String jsonText) {
        this.text = jsonText;
    }

}
