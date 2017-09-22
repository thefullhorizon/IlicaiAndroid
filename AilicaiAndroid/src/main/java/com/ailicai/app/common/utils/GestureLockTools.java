package com.ailicai.app.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.GlobleConstants;
import com.ailicai.app.ui.gesture.GestureLockActivity;
import com.ailicai.app.ui.login.UserInfo;

/**
 * Created by jeme on 2017/8/1.
 */

public class GestureLockTools {


    public static Intent getGestureLockIntent(Context context,@GestureLockActivity.LockType int type){
        Intent intent = null;
        if(UserInfo.getInstance().getLoginState() == UserInfo.LOGIN){
            switch (type){
                case GestureLockActivity.TYPE_AUTO:
                    String lockPwd = MyPreference.getInstance().read(getLockKey(), "");
                    if (TextUtils.isEmpty(lockPwd)) {
                        if (!MyPreference.getInstance().read(getJumpLockViewKey(), false)) {
                            intent = GestureLockActivity.getGestureLockView(context, GestureLockActivity.TYPE_SETTING);
                        }
                    }else{
                        intent = GestureLockActivity.getGestureLockView(context, GestureLockActivity.TYPE_LOCK);
                    }
                    break;
                case GestureLockActivity.TYPE_PERSON_SETTING:
                case GestureLockActivity.TYPE_PERSON_VERIFY_FOR_CLOSE:
                case GestureLockActivity.TYPE_PERSON_VERIFY_FOR_FIX:
                    intent = GestureLockActivity.getGestureLockView(context, type);
                    break;
                case GestureLockActivity.TYPE_SETTING:
                    if(TextUtils.isEmpty(MyPreference.getInstance().read(getLockKey(), ""))){
                        intent = GestureLockActivity.getGestureLockView(context, GestureLockActivity.TYPE_SETTING);
                    }
                    break;
                default:
                    break;
            }
        }
        return intent;
    }
    public static void goGestureLockView(Context context, @GestureLockActivity.LockType int type){
        Intent intent = getGestureLockIntent(context,type);
        if(intent != null){
            context.startActivity(intent);
            if(context instanceof Activity) {
                ((Activity)context).overridePendingTransition(R.anim.activity_lollipop_open_enter,R.anim.activity_lollipop_close_exit);
            }
        }
    }

    public static void goGestureLockView(Context context){
        goGestureLockView(context,GestureLockActivity.TYPE_AUTO);
    }

    public static void checkGesture(Activity activity){
        if (!MyApplication.getAppPresenter().isInFront()) {
            MyApplication.getAppPresenter().setAppFront(true);
            if( !MyPreference.getInstance().read(GestureLockTools.getLockEnableKey(),true)){
                return;
            }
            // 减得当前APP在后台滞留的时间 durTime
            long durTime = System.currentTimeMillis() - GlobleConstants.mLockAppTime;
            if (durTime > Constants.LOCK_TIME) {
                // 显示手势密码页面
                GestureLockTools.goGestureLockView(activity);
            }
        }
    }

    /***
     * 本地保存手势密码的key
     */
    public static String getLockKey(){
        return "lockpwd:" + UserInfo.getInstance().getUserId();
    }
    public static String getJumpLockViewKey(){
        return "lock_jump_" + UserInfo.getInstance().getUserId();
    }
    public static String getLockEnableKey(){
        return "lockEnable:" + UserInfo.getInstance().getUserId();
    }
    public static String getLockTryTimesKey(){
        return "lockTryTimesKey:"+ UserInfo.getInstance().getUserId();
    }
}
