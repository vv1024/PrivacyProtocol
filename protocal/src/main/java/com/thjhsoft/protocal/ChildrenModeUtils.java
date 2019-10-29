package com.thjhsoft.protocal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/*
需要计时的 Activity 继承此 BaseActivity
public class BaseActivity extends AppCompatActivity {

    long startTime;
    @Override
    protected void onResume() {
        startTime = SystemClock.elapsedRealtime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long lastTime = SystemClock.elapsedRealtime() - startTime;
        ChildrenModeUtils.getInstance().addUsingTime(lastTime);
    }
}

在最后退出的出口 Activity 中设置
@Override
protected void onDestroy() {
    ChildrenModeUtils.getInstance().saveUsingTime(this);
    super.onDestroy();
}

程序中获取使用总时间：
long usingTime = ChildrenModeUtils.getInstance().readUsingTimeToday(this);
tvTime.setText(MessageFormat.format("{0} 秒", usingTime / 1000));

 */
public class ChildrenModeUtils {

    private static final String TAG = ChildrenModeUtils.class.getSimpleName();
    private static ChildrenModeUtils childrenModeUtils;

    public static final String CONFIG_NAME = "ChildMode";
    public static final String PWD_FIELD = "password";
    public static final String FIRST_SHOW = "first_show";

    // 本次使用时长
    private long usingTime = 0;

    // 上一次使用时长，从记录中读取
    private long lastTime = 0;

    //
    private boolean childrenMode = false;
    // 延时是否开启
    private boolean delayedMode = false;

    private long totalMinute = 40;

    private long startTime = 0;

    private ChildrenModeUtils() {
    }

    public static ChildrenModeUtils getInstance() {
        if (childrenModeUtils == null) {
            childrenModeUtils = new ChildrenModeUtils();
        }
        return childrenModeUtils;
    }

    public String getTodayDate() {
        Log.d(TAG, "getTodayDate()");
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance(Locale.getDefault()).getTime());
    }

    /**
     * 读取状态参数
     * @param context Context
     * @return boolean
     */
    public boolean init(Context context, long totalMinute){
        Log.d(TAG, "init()");
        readUsingTimeToday(context);
        this.totalMinute = totalMinute;
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        String password = sharedPreferences.getString("password", null);
        childrenMode = !TextUtils.isEmpty(password);
        return childrenMode;
    }

    /**
     * 是否为第一次运行
     * @param context Context
     * @return boolean
     */
    public boolean isFirstShow(Context context){
        Log.d(TAG, "isFirstShow()");
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        boolean firstShow = sharedPreferences.getBoolean(ChildrenModeUtils.FIRST_SHOW, true);
        if(firstShow){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first_show", false);
            editor.apply();
        }
        return firstShow;
    }

    /**
     * 开启青少年模式，设置独立密码
     * @param password String
     */
    public void openChildrenMode(Context context, String password){
        Log.d(TAG, "openChildrenMode()");
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PWD_FIELD, password);
        editor.apply();

        delayedMode = false;
        lastTime = 0;
        usingTime = 0;
        childrenMode = true;

        startTime = SystemClock.elapsedRealtime();
    }

    /**
     * 关闭青少年模式
     * @param context Context
     * @param password String
     * @return boolean
     */
    public boolean closeChildrenMode(Context context, String password){
        Log.d(TAG, "closeChildrenMode()");
        SharedPreferences sharedPreferences = context.getSharedPreferences(ChildrenModeUtils.CONFIG_NAME, Context.MODE_PRIVATE);
        String originalPassword = sharedPreferences.getString(ChildrenModeUtils.PWD_FIELD, null);
        if (TextUtils.isEmpty(originalPassword) || TextUtils.isEmpty(password) || !originalPassword.equals(password)) {
            return false;
        } else {
            ChildrenModeUtils.getInstance().resetTime(sharedPreferences);
            return true;
        }
    }


    public void resetTime(SharedPreferences sharedPreferences){
        Log.d(TAG, "resetTime()");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.remove(ChildrenModeUtils.PWD_FIELD);
        Map<String, ?> map = sharedPreferences.getAll();
        for(String key: map.keySet()){
            if(key.equals(FIRST_SHOW)){
                continue;
            }
            editor.remove(key);
        }
        editor.apply();
        delayedMode = false;
        childrenMode = false;
        lastTime = 0;
        usingTime = 0;
    }

    /**
     * 添加本次运行时间（毫秒）
     * Activity OnResume() 开始记录时间 OnPause() 暂停，两个时间相减，添加到总时间中
     */
    public void updateUsingTime() {
        if(childrenMode){
            Log.d(TAG, "updateUsingTime()");
            long time = SystemClock.elapsedRealtime() - startTime;
            usingTime += time;
            Log.d(TAG, "addUsingTime : " + time / 1000);
            Log.d(TAG, "usingTime : " + usingTime / 1000);

        }
    }

    /**
     * 记录时间
     * 关闭应用时 Application OnDestroy() 保存本次运行时间
     */
    public void saveUsingTime(Context context) {
        if(childrenMode) {
            Log.d(TAG, "saveUsingTime()");
            String todayKey = getTodayDate();
            SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
            long lastTime = sharedPreferences.getLong(todayKey, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(todayKey, lastTime + usingTime);
            editor.apply();
            usingTime = 0;
        }

    }

    public long readUsingTimeToday(Context context) {
        Log.d(TAG, "readUsingTimeToday()");
        String todayKey = getTodayDate();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        lastTime = sharedPreferences.getLong(todayKey, 0);
        return lastTime;
    }

    /**
     * 是否在时间范围内
     *
     * @return boolean
     */
    public boolean inUsingTime(Context context) {
        Log.d(TAG, "inUsingTime()");
        if(!childrenMode || delayedMode){
            return true;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        // 如果没有 PWD_FIELD 字段，用户没有开启未成年模式，则返回 true
        if(TextUtils.isEmpty(sharedPreferences.getString(PWD_FIELD, null))){
            return true;
        }else{
            // 读取今天的使用时间，加上当前应用的使用时间，判断是否超时
            long minute = (lastTime + usingTime) / 1000 / 60;
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
            return hour24 < 22 && hour24 >= 6 && minute < totalMinute;
        }
    }

    public void setDelayedMode(boolean delayedMode) {
        this.delayedMode = delayedMode;
    }

    public boolean isChildrenMode() {
        return childrenMode;
    }

    public void setStartTime(long startTime) {
        if(childrenMode){
            Log.d(TAG, "setStartTime : " + startTime / 1000);
            this.startTime = startTime;
        }

    }
}
