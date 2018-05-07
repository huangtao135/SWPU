package com.ht.communi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ht.communi.activity.CommDetailActivity;
import com.ht.communi.activity.R;
import com.ht.communi.javabean.EventItem;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;

    private Context mContext;
    private List<EventItem> mDataList;
    private LayoutInflater mLayoutInflater;

    public EventAdapter(Context mContext, List<EventItem> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<EventItem> mDatas) {
        mDataList.clear();
        mDataList.addAll(mDatas);
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param parent   ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
//            View view = View.inflate(mContext,R.layout.community_cardview_item, null);
//            RecyclerView.ViewHolder holder = new NormalItemHolder(view);
//            view.setOnClickListener(this);
//            return holder;
            return new NormalItemHolder(mLayoutInflater.inflate(R.layout.event_cardview_item, parent, false));
        }
        return null;
    }


    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param i          数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final EventItem entity = mDataList.get(i);
        if (null == entity)
            return;

        if (viewHolder instanceof NormalItemHolder) {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            holder.commRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showCommDetail(v,entity);
                }
            });
            holder.commTitle.setText(entity.getEventName());
            holder.commDescription.setText(entity.getEventContent());
        } else {

        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源List的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        return NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    void showCommDetail(View v,EventItem item) {
        Intent intent = new Intent(v.getContext(), CommDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("COMM", item);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }

    /**
     * 新闻标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView commTitle;
        TextView commDescription;
        LinearLayout commRoot;


        public NormalItemHolder(View itemView) {
            super(itemView);
            commTitle = itemView.findViewById(R.id.event_item_title);
            commDescription = itemView.findViewById(R.id.event_item_content);
            commRoot = itemView.findViewById(R.id.event_item_container);
        }
    }
}
