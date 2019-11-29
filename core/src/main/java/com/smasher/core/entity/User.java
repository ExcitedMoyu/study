package com.smasher.core.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author Smasher
 * on 2019/11/8 0008
 */
@Entity(tableName = "user")
public class User {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "telephone")
    private String telephone;

    @ColumnInfo(name = "email")
    private String email;

    @Ignore
    private String domainId;

    @Ignore
    private String duty;

    @Ignore
    @SerializedName("deviceuserId")
    private int deviceUserId;

    @Ignore
    private String ext1;

    @Ignore
    private String ext2;

    @Ignore
    private String ext3;

    @Ignore
    private String ext4;

    @Ignore
    private String ext5;

    @Ignore
    private int groupId;

    @Ignore
    private String password;

    @Ignore
    private long repairId;

    @Ignore
    private int status;

    @Ignore
    private int type;



    public int getDeviceUserId() {
        return deviceUserId;
    }

    public void setDeviceUserId(int deviceUserId) {
        this.deviceUserId = deviceUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRepairId() {
        return repairId;
    }

    public void setRepairId(long repairId) {
        this.repairId = repairId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
