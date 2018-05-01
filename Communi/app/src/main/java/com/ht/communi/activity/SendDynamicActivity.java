package com.ht.communi.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.ht.communi.adapter.DynamicPhotoChooseAdapter;
import com.ht.communi.alrtdialog.DialogBuilder;
import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.javabean.DynamicPhotoItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.DynamicModel;
import com.ht.communi.model.impl.DynamicModelImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class SendDynamicActivity extends AppCompatActivity {
    private TextView cancel;
    private TextView send;
    private EditText editContent;
    private GridView gridView;
    private DynamicPhotoChooseAdapter mDynamicPhotoChooseAdapter;
    private final int REQUEST_CODE = 0x01;    //跳转选择图片请求码

//    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_dynamic);
        initView();
        init();
    }

    private void initView() {
        cancel = findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send = findViewById(R.id.tv_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        editContent = findViewById(R.id.edit_content);
        gridView = findViewById(R.id.gridView);
    }

    public void init() {
        final PhotoPickerIntent intent = new PhotoPickerIntent(SendDynamicActivity.this);
        intent.setPhotoCount(6);
        intent.setShowCamera(true);
        mDynamicPhotoChooseAdapter = new DynamicPhotoChooseAdapter(SendDynamicActivity.this);
        gridView.setAdapter(mDynamicPhotoChooseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mDynamicPhotoChooseAdapter.getCount() - 1) {
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
//        mLoadingDialog = DialogBuilder.createLoadingDialog(SendDynamicActivity.this, "正在上传");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(SendDynamicActivity.this, "上传完成");
        mProgressDialog = DialogBuilder.createProgressDialog(SendDynamicActivity.this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            List<DynamicPhotoItem> list = new ArrayList<>();
            if (pathList.size() != 0) {
                for (String path : pathList) {
                    list.add(new DynamicPhotoItem(path, false));
                }
            }
            mDynamicPhotoChooseAdapter.addData(list);
        }
    }

    private void send() {
//        mLoadingDialog.show();
        mProgressDialog.show();
        DynamicItem dynamicItem = new DynamicItem();
        dynamicItem.setWriter(BmobUser.getCurrentUser(Student.class));
        List<BmobFile> fileList = new ArrayList<>();
        ArrayList<DynamicPhotoItem> photoItems = (ArrayList<DynamicPhotoItem>) mDynamicPhotoChooseAdapter.getData();
        for (int i = 0; i < photoItems.size() - 1; i++) {
            fileList.add(new BmobFile(new File(photoItems.get(i).getFilePath())));
        }
        dynamicItem.setText(editContent.getText().toString());
        dynamicItem.setPhotoList(fileList);
        new DynamicModel().sendDynamicItem(mProgressDialog,dynamicItem, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
//                mLoadingDialog.dismiss();
                mProgressDialog.dismiss();
                mLoadingFinishDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingFinishDialog.dismiss();
                        finish();
                    }
                }, 500);
            }

            @Override
            public void getFailure() {

            }
        });
    }
}
