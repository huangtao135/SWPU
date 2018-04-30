package com.ht.communi.model;

import android.util.Log;

import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.impl.DynamicModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;


/**
 * 作用：对朋友圈数据操作的model
 */
public class DynamicModel implements DynamicModelImpl {


    /**
     * 获取所有的朋友圈消息
     *
     * @param listener
     */
    @Override
    public void getDynamicItem(final BaseListener listener) {
        BmobQuery<DynamicItem> query = new BmobQuery<DynamicItem>();
        query.order("-createdAt");
        query.findObjects(new FindListener<DynamicItem>() {
            @Override
            public void done(List<DynamicItem> list, BmobException e) {
                if (e == null){
                    Log.i("htht", "done: 查询成功：共   "+list.size()+"  条数据。");
                    listener.getSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 获取当前用户的所有动态
     *
     * @param user
     * @param listener
     */
    @Override
    public void getDynamicItemByPhone(Student user, final BaseListener listener) {
        BmobQuery<DynamicItem> query = new BmobQuery<DynamicItem>();
        query.addWhereEqualTo("Writer", user);
        query.findObjects(new FindListener<DynamicItem>() {
            @Override
            public void done(List<DynamicItem> list, BmobException e) {
                if(e==null){
                    Log.i("htht", "done: 查询成功：共   "+list.size()+"  条数据。");
                    listener.getSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 上传动态
     *
     * @param dynamicitem
     */
    public void sendDynamicItem(final DynamicItem dynamicitem, final BaseListener listener) {
        if (dynamicitem.getPhotoList().size() != 0) {
            final String[] array = new String[dynamicitem.getPhotoList().size()];
            for (int i = 0; i < dynamicitem.getPhotoList().size(); i++) {
                array[i] = dynamicitem.getPhotoList().get(i).getLocalFile().getAbsolutePath();
                Log.i("path", "sendDynamicItem: " + array[i] + " " + dynamicitem.getPhotoList().size());
            }
            BmobFile.uploadBatch( array, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    if (urls.size() == array.length) {
                        dynamicitem.setPhotoList(files);
                        dynamicitem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Log.i("htht", "上传发送的朋友圈图片成功！！！");
                                    listener.getSuccess(null);
                                }else{
                                    Log.i("bmob","上传发送的朋友圈图片失败！！！");
                                }
                            }
                        });
                    }
                }


                @Override
                public void onError(int statuscode, String errormsg) {
                    Log.i("TAG", "onError: " + errormsg + statuscode);
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                    Log.i("TAG", "onProgress: " + curIndex + " " + curPercent + "====== " + total +""+totalPercent);
                }
            });
        } else {
            dynamicitem.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {

                    if(e==null){
                        Log.i("htht", "上传发送的朋友圈消息成功！！！");
                        listener.getSuccess(null);
                    }else{
                        Log.i("bmob","上传发送的朋友圈消息失败！！！");
                    }
                }
            });
        }

    }

}
