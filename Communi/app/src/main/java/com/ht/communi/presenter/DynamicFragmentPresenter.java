package com.ht.communi.presenter;


import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.model.DynamicModel;
import com.ht.communi.model.impl.DynamicModelImpl;
import com.ht.communi.view.IDynamicFragment;

import java.util.List;

/**
 * 作用：朋友圈的Presenter
 */
public class DynamicFragmentPresenter {
    private DynamicModel mDynamicModel = new DynamicModel();
    private IDynamicFragment mView;

    public DynamicFragmentPresenter(IDynamicFragment mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mDynamicModel.getDynamicItem(new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {
            }
        });
    }

    public void onLoadMore(){

    }

}
