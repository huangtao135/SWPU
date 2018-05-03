package com.ht.communi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ht.communi.activity.R;
import com.ht.communi.adapter.CommunityAdapter;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;
import com.ht.communi.presenter.CommunityFragmentPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.ICommunityFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class CommunityFragment extends Fragment implements ICommunityFragment {

    private RecyclerView rv_community_list;
    private CommunityAdapter communityAdapter;
    private CommunityFragmentPresenter mPresenter;
    private List<CommunityItem> mList = new ArrayList<>();  //临时容器
    private List<CommunityItem> mCommunityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        mPresenter = new CommunityFragmentPresenter(this);
        communityAdapter = new CommunityAdapter(getContext(), mList);
        rv_community_list.setAdapter(communityAdapter);
        rv_community_list.setLayoutManager(new LinearLayoutManager(getContext()));

        if (NetUtil.checkNet(getActivity())) {
            mPresenter.onRefresh();
        }
    }

    private void initView() {
        Button btn_add = getActivity().findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityItem communityItem = new CommunityItem();
                communityItem.setCommDescription("大学生心理协会，是西南石油大学比较牛逼的协会111\n" +
                        "                大学生心理协会，是西南石油大学比较牛逼的协会222\n" +
                        "                大学生心理协会，是西南石油大学比较牛逼的协会333\n" +
                        "                大学生心理协会，是西南石油大学比较牛逼的协会444");

                communityItem.setCommSchool("西南石油大学");
                communityItem.setCommLeader(BmobUser.getCurrentUser(Student.class));
                communityItem.setCommName("大学生心理协会");
                communityItem.setVerify(new Boolean(false));
                new CommModel().addCommItem(communityItem, new CommModelImpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {

                    }

                    @Override
                    public void getFailure() {

                    }
                });
            }
        });

        rv_community_list = getActivity().findViewById(R.id.rv_community_list);
    }

    @Override
    public void onLoadMore(List<CommunityItem> list) {

    }

    @Override
    public void onRefresh(List<CommunityItem> list) {
        mCommunityList = list;
        communityAdapter.setDatas(mCommunityList);
        communityAdapter.notifyDataSetChanged();
    }
}
