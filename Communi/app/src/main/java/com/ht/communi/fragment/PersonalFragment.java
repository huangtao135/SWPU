package com.ht.communi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.R;
import com.ht.communi.alrtdialog.ActionSheetDialog;
import com.ht.communi.javabean.Student;
import com.ht.communi.view.CircleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonalFragment extends Fragment {
    private CircleImageView cImageView;
    private ImageView bImageView;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final String IMAGE_FILE_CROP_NAME = "temp_crop_head_image.jpg";

    /* 请求识别码 */
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private File tempFile;
    private BmobFile bmobFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }

    /**
     * 初始化
     */
    public void init() {
        //修改密码
        Button btn_changePassword = getActivity().findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.updateCurrentUserPassword("1111", "11111111", new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("htht", "done: 密码修改成功，可以用新密码进行登录啦");
                        } else {
                            Log.i("htht", "done: 密码修改失败，可以用新密码进行登录啦" + e.getErrorCode());
                        }
                    }

                });
            }
        });
        //退出登陆
        Button btn_signout = getActivity().findViewById(R.id.btn_signout);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                Log.i("htht", "done: 退出后" + currentUser);
            }
        });

        //左上角返回图标
        bImageView = getActivity().findViewById(R.id.imageview_person_back);
        bImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });

        //头像icon
        cImageView = getActivity().findViewById(R.id.circleimageview_personal_photo);
        cImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheetDialog(getContext())
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("打开相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        gallery();
                                    }
                                })
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        camera();
                                    }
                                }).show();
            }
        });

        //个性昵称
        EditText ed_username = getActivity().findViewById(R.id.ed_username);

        //学校
        EditText ed_school = getActivity().findViewById(R.id.ed_school);

        //手机号
        EditText ed_mobile = getActivity().findViewById(R.id.ed_mobile);

        //邮箱
        EditText ed_email = getActivity().findViewById(R.id.ed_email);

        Student student = BmobUser.getCurrentUser(Student.class);
        if (student != null) {
            ed_username.setText(student.getUsername());
            ed_school.setText(student.getSchool());
            ed_mobile.setText(student.getMobilePhoneNumber());
            ed_email.setText(student.getEmail());

            Log.i("htht", "student.getuserIcon=======================" + student.getUserIcon());
            Log.i("htht", "student.getUserIcon().getFileUrl()========" + student.getUserIcon().getFileUrl());
            Log.i("htht", "student.getuserIcon=======================" + student.getUserIcon().getUrl());

            Glide.with(getContext())
                    .load(student.getUserIcon().getFileUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(cImageView);


        }
    }


    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    crop(uri);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(getContext(), "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                cImageView.setImageBitmap(bitmap);

                //将图片设置完之后，还要上传网络
                bmobFile = new BmobFile(saveBitmapFile(bitmap));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            Log.i("htht", "上传文件成功:" + bmobFile.getFileUrl());
                            Student student = new Student();
                            student.setUserIcon(bmobFile);
                            student.update(BmobUser.getCurrentUser(Student.class).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.i("htht", "成功");
                                    } else {
                                        Log.i("htht", "失败" + e.getErrorCode());
                                    }
                                }
                            });
                            deleteIcon(IMAGE_FILE_CROP_NAME);
                        } else {
                            String errorMessage;
                            switch (e.getErrorCode()) {
                                case 9016:
                                    errorMessage = "没有网，上传服务器失败！！！";
                                    break;
                                default:
                                    errorMessage = "发生了一个不为人知的错误！！！";
                                    break;

                            }
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                            Log.i("htht", "上传文件失败:错误码   " + e.getErrorCode());
                            deleteIcon(IMAGE_FILE_CROP_NAME);
                        }
                    }
                });

                //检查是否拍了照片，如果拍了则删除。
                deleteIcon(IMAGE_FILE_NAME);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteIcon(String path){
        //检查剪裁图片删除没有，如果没有，则删除。
        tempFile = new File(Environment.getExternalStorageDirectory(),
                path);
        Log.i("htht", "文件存在吗======= " + tempFile);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    //将bitmap转换成file
    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_CROP_NAME);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
