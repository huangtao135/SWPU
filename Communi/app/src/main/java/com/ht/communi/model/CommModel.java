package com.ht.communi.model;

import android.util.Log;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.model.impl.CommModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommModel implements CommModelImpl {
    /**
     * 创建社团
     *
     * @param communityItem 社团item
     */
    public void addCommItem( final CommunityItem communityItem, final BaseListener listener) {
        final BmobFile commIcon = communityItem.getCommIcon();
        if (commIcon != null) {
            commIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("htht", "上传社团icon成功:");
                        communityItem.setCommIcon(commIcon);
                        communityItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "社团创建成功，等待验证！！！");
                                    listener.getSuccess(null);
                                } else {
                                    Log.i("htht", "社团创建失败！！！");
                                }
                            }
                        });
                    }else{
                        listener.getFailure();
                    }

                }
            });
        } else {
            communityItem.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("htht", "社团创建成功，等待验证！！！");
                        listener.getSuccess(null);
                    } else {
                        listener.getFailure();
                        Log.i("htht", "社团创建失败！！！");
                    }
                }
            });
        }

    }


    /**
     * 获取所有的社团消息
     *
     * @param listener
     */
    public void getCommItem(final BaseListener listener) {
        BmobQuery<CommunityItem> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<CommunityItem>() {
            @Override
            public void done(List<CommunityItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询社团成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询社团失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
