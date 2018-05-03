package com.ht.communi.presenter;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;
import com.ht.communi.view.ICommunityFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommunityFragmentPresenter {
    private CommModel mCommModel = new CommModel();
    private ICommunityFragment mView;

    public CommunityFragmentPresenter(ICommunityFragment mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mCommModel.getCommItem(new CommModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<CommunityItem> list = (List<CommunityItem>)o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

    public void onLoadMore( int currPage){
    }
}
