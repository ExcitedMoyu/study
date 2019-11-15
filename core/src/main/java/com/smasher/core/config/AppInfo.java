//package com.xcm.oa.core.config;
//
//import android.content.pm.PackageManager;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//
//import com.qidian.QDReader.framework.core.ApplicationContext;
//import com.qidian.QDReader.framework.core.io.FileUtil;
//import com.qidian.QDReader.framework.core.tool.DeviceUtil;
//import com.qidian.QDReader.framework.core.tool.EmulatorUtil;
//import com.qidian.QDReader.framework.core.tool.RootUtil;
//import com.xcm.oa.core.io.FileUtil;
//import com.xcm.oa.core.other.ApplicationContext;
//
//import org.json.JSONObject;
//
///**
// * Created by huangzhaoyi on 2017/4/16.
// */
//
//public class AppInfo {
//    protected static final String OTHER = "0821CAAD409B8402";
//    public static int IsNightMode = 0; // 1为夜间模式,0为白天模式
//
//    protected String buildRevision;//编译时间，由于git并没有像svn那样的version值，故用编译时间代替
//    protected String versionName;
//    protected int versionCode;
//
//    protected String sourceType = "b";// 注册用户时，传给acs.qidian.com接口用到的，单本是j，客户端是b
//    protected String source;//第一次安装的
//    protected String apkSource;//config里拿的
//
//    protected int isRoot;
//    protected int isEmulator;
//    protected int clientType = 1;
//    protected int apiVersion = 4;
//
//    protected String imei = "";
//    protected String imsi = "";
//    protected String iccid = "";
//    protected String wifiMac = "";
//    protected String simSerial;
//    protected String androidId;
//    protected String cpuSerial;
//    protected String systemInfo;
//    protected int screenWidth;
//    protected int screenHeight;
//    protected String sdk;
//    protected String phoneModel;
//    protected String phoneBrand;
//    protected String phoneOS;
//
//    protected boolean isDebug;
//
//    public AppInfo() {
//        init();
//    }
//
//    protected void init() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isEmulator = EmulatorUtil.isEmulator(ApplicationContext.getInstance());
//            }
//        }).start();
//        //初始化imei号，6.0以上需要权限
//        initByPermission();
//
//        this.isRoot = RootUtil.isDeviceRooted();
//        this.versionCode = DeviceUtil.getVersionCode();
//        this.versionName = DeviceUtil.getVersionName();
//
//        this.sdk = DeviceUtil.getSDK();
//        this.phoneModel = DeviceUtil.getPhoneModel();
//        this.phoneOS = DeviceUtil.getPhoneOS();
//        this.phoneBrand = DeviceUtil.getPhoneBrand();
//
//        this.cpuSerial = DeviceUtil.getCPUSerial();
//        this.androidId = DeviceUtil.getAndroidId();
//        this.systemInfo = DeviceUtil.getSystemVersion();
//
//        this.screenWidth = DeviceUtil.getScreenWidth();
//        this.screenHeight = DeviceUtil.getScreenHeight();
//
//        try {
//            byte[] buildConfigData = FileUtil.loadAsset(ApplicationContext.getInstance(), "BuildConfig.txt");
//            if (buildConfigData != null) {
//                String buildConfig = new String(buildConfigData);
//                JSONObject json = new JSONObject(buildConfig);
//                this.isDebug = json.optBoolean("Debug", false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            byte[] buildData = FileUtil.loadAsset(ApplicationContext.getInstance(), "build.txt");
//            if (buildData != null) {
//                String buildTxt = new String(buildData);
//                this.buildRevision = buildTxt;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            byte[] configData = FileUtil.loadAsset(ApplicationContext.getInstance(), "config.txt");
//            if (configData != null) {
//                String config = new String(configData);
//                String[] configArray = config.split("\\|");
//                if (configArray.length > 0) {
//                    this.apkSource = configArray[0];
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 6.0以上需要先申请权限才能拿到
//     */
//    protected void initByPermission() {
//        this.imei = DeviceUtil.getIMEI();
//        this.imsi = DeviceUtil.getIMSI();
//        this.iccid = DeviceUtil.getSimSerial();
//        this.simSerial = DeviceUtil.getSimSerial();
//        this.wifiMac = DeviceUtil.getWifiMac();
//    }
//
//    public String getVersionName() {
//        return versionName;
//    }
//
//    public int getVersionCode() {
//        return versionCode;
//    }
//
//    public String getSourceType() {
//        return sourceType;
//    }
//
//    public String getSource() {
//        return source;
//    }
//
//    public String getApkSource() {
//        return apkSource;
//    }
//
//    public int getIsRoot() {
//        return isRoot;
//    }
//
//    public int getIsEmulator() {
//        return isEmulator;
//    }
//
//    public int getClientType() {
//        return clientType;
//    }
//
//    public int getApiVersion() {
//        return apiVersion;
//    }
//
//    public String getImei() {
//        return imei;
//    }
//
//    public String getImsi() {
//        return imsi;
//    }
//
//    public String getIccid() {
//        return iccid;
//    }
//
//    public String getWifiMac() {
//        return wifiMac;
//    }
//
//    public String getSimSerial() {
//        return simSerial;
//    }
//
//    public String getAndroidId() {
//        return androidId;
//    }
//
//    public String getCpuSerial() {
//        return cpuSerial;
//    }
//
//    public String getSystemInfo() {
//        return systemInfo;
//    }
//
//    public int getScreenWidth() {
//        return screenWidth;
//    }
//
//    public int getScreenHeight() {
//        return screenHeight;
//    }
//
//    public String getSdk() {
//        return sdk;
//    }
//
//    public String getPhoneModel() {
//        return phoneModel;
//    }
//
//    public String getPhoneBrand() {
//        return phoneBrand;
//    }
//
//    public String getPhoneOS() {
//        return phoneOS;
//    }
//
//    public String getIMEI() {
//        return imei;
//    }
//
//    /**
//     * 获取编译时间
//     *
//     * @return
//     */
//    public String getBuildRevision() {
//        if (!TextUtils.isEmpty(buildRevision) && buildRevision.length() > 7) {
//            return buildRevision.substring(0, 8);
//        }
//        return buildRevision;
//    }
//
//    public String getDeviceInfo() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("IMEI:");
//        sb.append(imei);
//        sb.append("|");
//        sb.append("|");
//        sb.append("PhoneModel:");
//        sb.append(phoneModel);
//        sb.append("|");
//        sb.append("Source:");
//        sb.append(source);
//        sb.append("|");
//        sb.append("SDK:");
//        sb.append(sdk);
//        sb.append("|");
//        sb.append("VersionCode:");
//        sb.append(versionCode);
//        sb.append("|");
//        sb.append("VersionName:");
//        sb.append(versionName);
//        return sb.toString();
//    }
//
//    public boolean isDebug() {
//        return isDebug;
//    }
//
//}
