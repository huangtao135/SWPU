package com.ht.communi.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ht.communi.adapter.CommunityReplyAdapter;
import com.ht.communi.customView.MyXListView;
import com.ht.communi.customView.ResizableImageView;
import com.ht.communi.customView.RiseNumberTextView;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.CommunityReplyItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;
import com.ht.communi.presenter.CommunityReplyPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.ICommunityReply;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.maxwin.view.XListView;

public class CommDetailActivity extends AppCompatActivity implements XListView.IXListViewListener, ICommunityReply {
    private TextView tv_comm_detail_title;
    private TextView tv_comm_detail_school;
    private TextView tv_comm_detail_content;
    private ResizableImageView iv_comm_icon;
    private ShineButton shineButton;
    private RiseNumberTextView tv_likes;

    //评论
    private ImageView comment;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;
    private LinearLayout rl_enroll;
    private RelativeLayout rl_comment;

    private MyXListView xListView;

    private CommunityReplyPresenter mPresenter;
    private CommunityItem communityItem;
    private CommunityReplyAdapter mAdapter;
    private List<CommunityReplyItem> mList = new ArrayList<>();     //临时数据
    private List<CommunityReplyItem> mDynamicList;              //真正的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_detail);
        initView();

        //社团信息+点赞
        communityItem = (CommunityItem) getIntent().getSerializableExtra("COMM");
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
                    tv_likes.setInteger(0, likes);
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


        //评论的xListView
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(this);
        mAdapter = new CommunityReplyAdapter(this, R.layout.activity_comm_reply_item, mList);

        mPresenter = new CommunityReplyPresenter(this);

        xListView.setAdapter(mAdapter);

        if (NetUtil.checkNet(this) && communityItem != null) {
            mPresenter.onRefresh(communityItem);
        } else {
            xListView.setVisibility(View.GONE);
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


        //评论功能
        rl_enroll = (LinearLayout) findViewById(R.id.rl_enroll);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        comment = (ImageView) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInputFromWindow(CommDetailActivity.this, comment_content);
                // 显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
            }
        });
        hide_down = (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏评论框
                rl_enroll.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
            }
        });
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        xListView = findViewById(R.id.xListView_comm_reply);
    }

    /**
     * 发送评论
     */
    public void sendComment() {
        if (comment_content.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            CommunityReplyItem communityReplyItem = new CommunityReplyItem();
            if (communityItem != null) {
                communityReplyItem.setCommunity(communityItem);
            }
            communityReplyItem.setContent(comment_content.getText().toString());
            communityReplyItem.setStudent(BmobUser.getCurrentUser(Student.class));
            mPresenter.onSendReply(communityReplyItem);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(final Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        // 弹出输入法
        InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * xListview  回调
     */
    @Override
    public void onRefresh() {
        if(communityItem != null) {
            mPresenter.onRefresh(communityItem);
        }
    }

    /**
     * xListview  回调
     */
    @Override
    public void onLoadMore() {
        mPresenter.onLoadMore();
    }

    /**
     * view 回调
     *
     * @param list
     */
    @Override
    public void onLoadMore(List<CommunityReplyItem> list) {

    }

    /**
     * view 回调
     *
     * @param list
     */
    @Override
    public void onRefresh(List<CommunityReplyItem> list) {
//        xListView.setPullLoadEnable(true);
        mDynamicList = list;
        mAdapter.setDatas(mDynamicList);
        mAdapter.notifyDataSetChanged();
        onLoad();
    }

    @Override
    public void onSendFinish() {
        // 发送完，清空输入框
        comment_content.setText("");
        Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
    }

    private void onLoad() {
        xListView.setVisibility(View.VISIBLE);

        xListView.stopRefresh();
        xListView.stopLoadMore();

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期显示格式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);// 将时间装换为设置好的格式
        xListView.setRefreshTime(str);//设置时间
    }
}
