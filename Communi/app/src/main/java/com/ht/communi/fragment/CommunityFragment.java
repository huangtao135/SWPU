package com.ht.communi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.ht.communi.activity.CreateCommunityActivity;
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

    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private FloatingActionButton menu_add_comm;
    private RecyclerView rv_community_list;
    private CommunityAdapter communityAdapter;
    private CommunityFragmentPresenter mPresenter;
    private List<CommunityItem> mList = new ArrayList<>();  //临时容器
    private List<CommunityItem> mCommunityList;     //真正的社团数据

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
        initRefresh();

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
        menu_add_comm = getActivity().findViewById(R.id.menu_add_comm);
        menu_add_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateCommunityActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
    }

    private void initRefresh(){
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh();
            }
        });
    }

    //view的onLoadMore
    @Override
    public void onLoadMore(List<CommunityItem> list) {

    }

    //view的onRefresh
    @Override
    public void onRefresh(List<CommunityItem> list) {
        mCommunityList = list;
        communityAdapter.setDatas(mCommunityList);
        communityAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
