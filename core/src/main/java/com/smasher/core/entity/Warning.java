package com.smasher.core.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Smasher
 * on 2019/11/4 0004
 */
@Entity(tableName = "warning")
public class Warning {

    /**
     * 设置主键
     * 字段映射具体的数据表字段名
     */
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "device_id")
    private long deviceId;

    @ColumnInfo(name = "device_name")
    private String deviceName;

    /**
     * 控制系统品牌
     */
    @ColumnInfo(name = "device_type_name")
    private String deviceTypeName;

    /**
     * 所属区域id
     */
    @ColumnInfo(name = "domain_id")
    private String domainId;

    /**
     * 所属区域
     */
    @ColumnInfo(name = "domain_path_name")
    private String domainPathName;

    @ColumnInfo(name = "fault_code")
    private String faultCode;

    /**
     * 首次预警时间
     */
    @ColumnInfo(name = "lastest_time")
    private long lastestTime;

    /**
     * （1:高，2：中，3：低）
     */
    @ColumnInfo
    private int level;

    @ColumnInfo(name = "record_time")
    private long recordTime;

    /**
     * 控制系统型号id
     */
    @ColumnInfo(name = "template_id")
    private int templateId;

    /**
     * 控制系统型号
     */
    @ColumnInfo(name = "template_name")
    private String templateName;

    /**
     * 未处理数
     */
    @ColumnInfo(name = "total_count")
    private int totalCount;

    /**
     * 已处理数
     */
    @ColumnInfo(name = "treated_count")
    private int treatedCount;

    /**
     * 预警信息
     */
    @ColumnInfo(name = "warning_description")
    private String warningDescription;

    /**
     * 预警配置id
     */
    @ColumnInfo(name = "warning_id")
    private int warningId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDomainPathName() {
        return domainPathName;
    }

    public void setDomainPathName(String domainPathName) {
        this.domainPathName = domainPathName;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public long getLastestTime() {
        return lastestTime;
    }

    public void setLastestTime(long lastestTime) {
        this.lastestTime = lastestTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTreatedCount() {
        return treatedCount;
    }

    public void setTreatedCount(int treatedCount) {
        this.treatedCount = treatedCount;
    }

    public String getWarningDescription() {
        return warningDescription;
    }

    public void setWarningDescription(String warningDescription) {
        this.warningDescription = warningDescription;
    }

    public int getWarningId() {
        return warningId;
    }

    public void setWarningId(int warningId) {
        this.warningId = warningId;
    }

}
