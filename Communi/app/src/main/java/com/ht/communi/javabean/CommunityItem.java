package com.ht.communi.javabean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommunityItem extends BmobObject implements Serializable{
    private String commName;
    private String commDescription;
    private BmobFile commIcon;
    private Student commLeader;
    private String commSchool;  //属于哪个学校
    private List<Student> commMembers;
    private List<Student> commApplies;
    private Boolean verify;  //审核是否同意创建该社团

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommSchool() {
        return commSchool;
    }

    public void setCommSchool(String commSchool) {
        this.commSchool = commSchool;
    }

    public String getCommDescription() {
        return commDescription;
    }

    public void setCommDescription(String commDescription) {
        this.commDescription = commDescription;
    }

    public BmobFile getCommIcon() {
        return commIcon;
    }

    public void setCommIcon(BmobFile commIcon) {
        this.commIcon = commIcon;
    }

    public Student getCommLeader() {
        return commLeader;
    }

    public void setCommLeader(Student commLeader) {
        this.commLeader = commLeader;
    }

    public List<Student> getCommMembers() {
        return commMembers;
    }

    public void setCommMembers(List<Student> commMembers) {
        this.commMembers = commMembers;
    }

    public List<Student> getCommApplies() {
        return commApplies;
    }

    public void setCommApplies(List<Student> commApplies) {
        this.commApplies = commApplies;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }
}
