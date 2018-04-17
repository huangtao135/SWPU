package com.ht.communi.javabean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Student extends BmobUser {
    private String stuName;
    private String school;
    private Boolean sex;
    private Integer age;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }
}
