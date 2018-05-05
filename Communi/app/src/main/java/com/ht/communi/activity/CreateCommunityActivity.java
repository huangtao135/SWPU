package com.ht.communi.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;

import cn.bmob.v3.BmobUser;

public class CreateCommunityActivity extends AppCompatActivity {

    private EditText ed_comm_name;
    private EditText ed_comm_description;
    private EditText ed_comm_school;
    private TextView tv_font_count;
    private Button btn_create_comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_community);
        initView();
    }

    private void initView(){
        ed_comm_name = findViewById(R.id.ed_comm_name);
        ed_comm_description = findViewById(R.id.ed_comm_description);
        ed_comm_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_font_count.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ed_comm_school = findViewById(R.id.ed_comm_school);
        tv_font_count = findViewById(R.id.tv_font_count);
        btn_create_comm = findViewById(R.id.btn_create_comm);
        btn_create_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComm();
            }
        });
    }


    private void createComm(){
        if (!validate()) {
            onSignupFailed();
            Log.i("htht", "createComm: 直接退出了");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(CreateCommunityActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账号中...");
        progressDialog.show();

        String name = ed_comm_name.getText().toString();
        String school = ed_comm_school.getText().toString();
        String description = ed_comm_description.getText().toString();

        CommunityItem communityItem = new CommunityItem();
        communityItem.setCommDescription(description);

        communityItem.setCommSchool(school);
        communityItem.setCommLeader(BmobUser.getCurrentUser(Student.class));
        communityItem.setCommName(name);
        communityItem.setVerify(new Boolean(false));
        new CommModel().addCommItem(communityItem, new CommModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                onSignupSuccess();
            }

            @Override
            public void getFailure() {

            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        String name = ed_comm_name.getText().toString();
        String school = ed_comm_school.getText().toString();
        String description = ed_comm_description.getText().toString();

        if (name.isEmpty()|| name.length() > 20) {
            ed_comm_name.setError("请输入小于20位社团名");
            valid = false;
        } else {
            ed_comm_name.setError(null);
        }

        if (school.isEmpty()) {
            ed_comm_school.setError("请输入一个学校名");
            valid = false;
        } else {
            ed_comm_school.setError(null);
        }

        if (description.isEmpty()) {
            ed_comm_description.setError("请输入您对您社团的详细描述");
            valid = false;
        } else {
            ed_comm_description.setError(null);
        }
        return valid;
    }

    private void onSignupSuccess() {
        btn_create_comm.setEnabled(true);
        finish();
    }

    private void onSignupFailed() {
        btn_create_comm.setEnabled(true);
    }
}
