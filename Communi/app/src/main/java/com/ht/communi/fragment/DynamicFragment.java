package com.ht.communi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ht.communi.activity.R;

import me.maxwin.view.XListView;

public class DynamicFragment extends Fragment {

    private ImageView publish;
    private TextView title;
    private XListView xListView;
    private RelativeLayout loading;
    private LinearLayout tip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        publish = getActivity().findViewById(R.id.publish);
        title = getActivity().findViewById(R.id.title);
        xListView = getActivity().findViewById(R.id.xListView);
        loading = getActivity().findViewById(R.id.loading);
        tip = getActivity().findViewById(R.id.tip);

    }

}
