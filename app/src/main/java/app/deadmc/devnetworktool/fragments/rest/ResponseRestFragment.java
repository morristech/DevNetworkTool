package app.deadmc.devnetworktool.fragments.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.activities.FullViewActivity;
import app.deadmc.devnetworktool.adapters.ParametersAdapter;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.helpers.FileFormatHelper;
import app.deadmc.devnetworktool.helpers.StringHelper;
import app.deadmc.devnetworktool.modules.ResponseDev;
import app.deadmc.devnetworktool.singletons.SharedData;
import app.deadmc.devnetworktool.views.CollapseLinearLayout;

import static app.deadmc.devnetworktool.helpers.ImageHelpers.rotateImage;

/**
 * Created by adanilov on 14.03.2017.
 */

public class ResponseRestFragment extends ParentFragment {

    private CollapseLinearLayout collapseLinearLayoutStats;
    private CollapseLinearLayout collapseLinearLayoutHeaders;
    private CollapseLinearLayout collapseLinearLayoutRequest;
    private LinearLayout statsTitleLayout;
    private LinearLayout headerTitleLayout;
    private LinearLayout requestTitleLayout;
    private RecyclerView statsRecyclerView;
    private RecyclerView headersRecyclerView;
    private TextView bodyTextView;
    private ImageView imageViewArrowStats;
    private ImageView imageViewArrowHeader;
    private ImageView imageViewArrowRequest;
    private ResponseDev responseDev;
    private Button watchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_rest_response, container, false);
        restoreState(savedInstanceState);
        initElements();
        return myFragmentView;
    }

    @Override
    public void initElements() {
        //super.initElements();
        headersRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.headersRecyclerView);
        bodyTextView = (TextView) myFragmentView.findViewById(R.id.bodyTextView);
        statsRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.statsRecyclerView);
        collapseLinearLayoutStats = (CollapseLinearLayout) myFragmentView.findViewById(R.id.collapseStats);
        collapseLinearLayoutHeaders = (CollapseLinearLayout) myFragmentView.findViewById(R.id.collapseHeaders);
        collapseLinearLayoutRequest = (CollapseLinearLayout) myFragmentView.findViewById(R.id.collapseRequest);
        statsTitleLayout = (LinearLayout) myFragmentView.findViewById(R.id.statsTitleLayout);
        headerTitleLayout = (LinearLayout) myFragmentView.findViewById(R.id.headerTitleLayout);
        requestTitleLayout = (LinearLayout) myFragmentView.findViewById(R.id.requestTitleLayout);
        imageViewArrowStats = (ImageView) myFragmentView.findViewById(R.id.imageViewArrowStats);
        imageViewArrowHeader = (ImageView) myFragmentView.findViewById(R.id.imageViewArrowHeader);
        imageViewArrowRequest = (ImageView) myFragmentView.findViewById(R.id.imageViewArrowRequest);
        watchButton = (Button) myFragmentView.findViewById(R.id.watchButton);
        initLayout(collapseLinearLayoutStats, statsTitleLayout, imageViewArrowStats);
        initLayout(collapseLinearLayoutHeaders, headerTitleLayout, imageViewArrowHeader);
        initLayout(collapseLinearLayoutRequest, requestTitleLayout, imageViewArrowRequest);
        initRecyclerViews(headersRecyclerView,null);
        initRecyclerViews(statsRecyclerView,null);
        initButton();
        if (responseDev != null)
            setResponse(responseDev);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void restoreState(Bundle bundle) {
        responseDev = SharedData.getInstance().getResponseDev();
        if (bundle == null)
            return;
        if (!bundle.containsKey("responseDev"))
            return;

    }

    public void initButton() {
        watchButton.setVisibility(View.GONE);

        if (responseDev == null || responseDev.getBody() == null)
            return;
        watchButton.setVisibility(View.VISIBLE);

        int type = FileFormatHelper.getTypeOfString(responseDev.getBody());
        Log.e("Response","type is "+type);
        switch (type) {
            case FileFormatHelper.JSON:
                watchButton.setText(getString(R.string.open_as)+" "+getString(R.string.json));
                break;
            case FileFormatHelper.XML:
                watchButton.setText(getString(R.string.open_as)+" "+getString(R.string.xml));
                break;
            case FileFormatHelper.HTML:
                watchButton.setText(getString(R.string.open_as)+" "+getString(R.string.html));
                break;
            case FileFormatHelper.UNDEFINED:
                watchButton.setVisibility(View.GONE);
                break;
        }


        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseDev.getBody() == null)
                    return;
                Intent intent = new Intent(getContext(), FullViewActivity.class);
                startActivity(intent);
            }
        });



    }

    public void initLayout(final CollapseLinearLayout collapseLinearLayout, final LinearLayout titleLayout, final ImageView imageView) {
        collapseLinearLayout.collapse();

        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collapseLinearLayout.isCollapsed()) {
                    collapseLinearLayout.expand();
                } else {
                    collapseLinearLayout.collapse();
                }
                rotateImage(imageView, collapseLinearLayout.isCollapsed(), 300);
            }
        });
        rotateImage(imageView, collapseLinearLayout.isCollapsed(), 0);
    }


    public void setResponse(ResponseDev responseDev) {
        //headersRecyclerView.setText(responseDev.getResponse().headers().toString());
        Log.e("Response","setResponse");

        this.responseDev = responseDev;

        initHeadersRecyclerView(responseDev.getHeaders());
        initStatsRecyclerView(responseDev.getCode(), responseDev.getDelay());
        bodyTextView.setText(responseDev.getBody());
        initButton();

    }

    private void initRecyclerViews(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void initStatsRecyclerView(int code, int responseTime) {
        ArrayList<Spanned> arrayList = new ArrayList<>();
        arrayList.add(StringHelper.fromHtml(getString(R.string.response_code)+ " " + code));
        arrayList.add(StringHelper.fromHtml(getString(R.string.response_time) +" "+ responseTime + " " + getString(R.string.ms)));
        ParametersAdapter parametersAdapter = new ParametersAdapter(getContext(), arrayList);
        initRecyclerViews(statsRecyclerView, parametersAdapter);
    }

    private void initHeadersRecyclerView(String headers) {
        Log.e("headers", headers.toString());
        ArrayList<Spanned> arrayList = new ArrayList<>();
        String[] stringArray = headers.split("\n");
        for (String head : stringArray) {
            int index = head.indexOf(":");
            String key = head.substring(0, index);
            String value = head.substring(index, head.length());
            arrayList.add(StringHelper.fromHtml("<b>" + key + "</b>" + value));
        }

        ParametersAdapter parametersAdapter = new ParametersAdapter(getContext(), arrayList);
        initRecyclerViews(headersRecyclerView, parametersAdapter);

    }
}
