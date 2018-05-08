package com.ht.communi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ht.communi.customView.ResizableImageView;
import com.ht.communi.customView.RiseNumberTextView;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;
import com.sackcentury.shinebuttonlib.ShineButton;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class CommDetailActivity extends AppCompatActivity {
    private TextView tv_comm_detail_title;
    private TextView tv_comm_detail_school;
    private TextView tv_comm_detail_content;
    private ResizableImageView iv_comm_icon;
    private ShineButton shineButton;
    private RiseNumberTextView tv_likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_detail);
        initView();

        final CommunityItem communityItem = (CommunityItem) getIntent().getSerializableExtra("COMM");
        if (shineButton != null) {
            shineButton.init(CommDetailActivity.this);
            shineButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    Log.i("htht", "点赞了吗: " + checked);
                    if (checked) {
                        tv_likes.setText(Integer.parseInt(tv_likes.getText().toString()) + 1 + "");
                        communityItem.increment("likes");
                    } else {
                        tv_likes.setText(Integer.parseInt(tv_likes.getText().toString()) - 1 + "");
                        communityItem.increment("likes", -1);
                    }
                    communityItem.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("htht", "点赞成功！！ ");
                            }
                        }
                    });
                }
            });
        }

        tv_comm_detail_title.setText(communityItem.getCommName());
        tv_comm_detail_school.setText(communityItem.getCommSchool());
        tv_comm_detail_content.setText(communityItem.getCommDescription());

        new CommModel().getCommLikes(communityItem, new CommModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                Integer likes = ((CommunityItem) o).getLikes();
                if (likes != null) {
                    tv_likes.setInteger(0,likes);
                    tv_likes.start();
                }

            }

            @Override
            public void getFailure() {

            }
        });

        if (communityItem.getCommIcon() != null) {
            Glide.with(CommDetailActivity.this)
                    .load(communityItem.getCommIcon().getFileUrl())
                    .into(iv_comm_icon);
        }
    }

    private void initView() {
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_comm_icon = findViewById(R.id.iv_comm_icon);
        tv_comm_detail_title = findViewById(R.id.tv_comm_detail_title);
        tv_comm_detail_school = findViewById(R.id.tv_comm_detail_school);
        tv_comm_detail_content = findViewById(R.id.tv_comm_detail_content);
        shineButton = findViewById(R.id.shineButton);
        tv_likes = findViewById(R.id.tv_likes);
    }
}
