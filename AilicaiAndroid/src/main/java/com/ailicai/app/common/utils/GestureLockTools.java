package com.ailicai.app.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

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
        }
    }
    public static void goGestureLockView(Context context){
        goGestureLockView(context,GestureLockActivity.TYPE_AUTO);
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
}