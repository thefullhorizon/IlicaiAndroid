package com.ailicai.app.ui.gesture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.utils.GestureLockTools;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.LoginSuccessCardDialog;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.gestruelock.GestureLockIndicator;
import com.ailicai.app.widget.gestruelock.GestureLockViewContent;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手势页面及设置页面
 * Created by jeme on 2017/8/1.
 */

public class GestureLockActivity extends BaseBindActivity implements GestureLockViewContent.OnGestureLockViewListener,View.OnClickListener{

    public static final int TYPE_AUTO = 1;
    public static final int TYPE_SETTING = 2;
    public static final int TYPE_LOCK = 3;
    public static final int TYPE_PERSON_SETTING = 4;//用户设置
    public static final int TYPE_PERSON_VERIFY_FOR_CLOSE = 5;//用户验证然后关闭手势
    public static final int TYPE_PERSON_VERIFY_FOR_FIX = 6;//用户验证然后重设密码
    public static final String TYPE_KEY = "lockTypeKey";
    private int GESTURE_TRY_TIMES = 5;


    @IntDef({TYPE_AUTO,TYPE_SETTING,TYPE_LOCK,TYPE_PERSON_SETTING,TYPE_PERSON_VERIFY_FOR_CLOSE,TYPE_PERSON_VERIFY_FOR_FIX})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface LockType{}

    @Bind(R.id.vs_lock_view_indicator)
    ViewStub mVsLockViewIndicator;
    @Bind(R.id.vs_userinfo)
    ViewStub mVsUserInfo;
    @Bind(R.id.tv_gesture_lock_tip)
    TextView mTvGestureLockTip;
    @Bind(R.id.glvg_lock_on)
    GestureLockViewContent mGlvgLock;
    @Bind(R.id.iwttv_top)
    IWTopTitleView mTitleBar;
    @Bind(R.id.vs_bottom)
    ViewStub mVsBottom;
    //底部布局
    View mVForgetPwd;
    View mVLine;
    View mVChangeUser;
    //指示器的vs
    GestureLockIndicator mGlvgIndicator;
    //用户的vs
//    CircleImageView mCivUserPhoto;
    TextView mTvUserPhone;


    private @LockType int mLockType;
    private LoginEvent mLoginEvent;

    public static Intent getGestureLockView(Context context, @LockType int type){
        Intent intent = new Intent(context,GestureLockActivity.class);
        intent.putExtra(TYPE_KEY,type);
        return intent;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gensturelock;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void init(Bundle savedInstanceState) {
        // 禁止唤起手势页
        disablePatternLock();
        mLockType = getIntent().getIntExtra(TYPE_KEY,TYPE_SETTING);
        mLoginEvent = (LoginEvent) (getIntent().getSerializableExtra("loginEvent"));
        if(mLockType == GestureLockActivity.TYPE_AUTO){
            //Auto的状态会在工具类中指定为唯一的状态，进入此页面不应有此状态
            finish();
        }
        View view;
        if(mLockType == TYPE_SETTING || mLockType == TYPE_PERSON_SETTING){
            MyPreference.getInstance().remove(GestureLockTools.getLockTryTimesKey());
            view = mVsLockViewIndicator.inflate();
            mGlvgIndicator = ButterKnife.findById(view,R.id.gli_indicator);
            mTitleBar.setTitleText("设置手势密码");
            if(mLockType == TYPE_SETTING) {
                mTitleBar.addRightText("跳过", this);
                mTitleBar.setIsShowLeftBtn(false);
            }else{
                mTitleBar.setIsShowLeftBtn(true);
            }
            mTitleBar.setVisibility(View.VISIBLE);
            mTvGestureLockTip.setText(R.string.gesture_lock_view_set_lock_tip);
            mGlvgLock.setUnMatchExceedBoundary(Integer.MAX_VALUE);
            mGlvgLock.setStatus(GestureLockViewContent.STATUS_SETTING);
        }else if(mLockType == TYPE_LOCK || mLockType == TYPE_PERSON_VERIFY_FOR_FIX ||
                mLockType == TYPE_PERSON_VERIFY_FOR_CLOSE){

            view = mVsUserInfo.inflate();
            View vEmpty = ButterKnife.findById(view,R.id.v_empty);
            mTvUserPhone = ButterKnife.findById(view,R.id.tv_user_phone);
            mTvUserPhone.setText(StringUtil.formatMobileSubTwo(UserInfo.getInstance().getUserMobile()));

            int tryTimes = MyPreference.getInstance().read(GestureLockTools.getLockTryTimesKey(),GESTURE_TRY_TIMES);
            mGlvgLock.setUnMatchExceedBoundary(tryTimes == 0 ? GESTURE_TRY_TIMES : tryTimes);//默认最多重试5次
            mGlvgLock.setAnswer(MyPreference.getInstance().read(GestureLockTools.getLockKey(),""));

            view = mVsBottom.inflate();
            mVForgetPwd = ButterKnife.findById(view,R.id.tv_forget_pwd);
            mVChangeUser = ButterKnife.findById(view,R.id.tv_change_user);
            mVLine = ButterKnife.findById(view,R.id.v_line);
            mVForgetPwd.setOnClickListener(this);
            if(mLockType == TYPE_LOCK) {
                mTitleBar.setVisibility(View.GONE);
                vEmpty.setVisibility(View.VISIBLE);
                mVLine.setVisibility(View.VISIBLE);
                mVChangeUser.setVisibility(View.VISIBLE);
                mVChangeUser.setOnClickListener(this);
            }else{
                mTitleBar.setTitleText("验证手势密码");
                mTitleBar.setIsShowLeftBtn(true);
                mTitleBar.setVisibility(View.VISIBLE);
                vEmpty.setVisibility(View.GONE);
                mTvGestureLockTip.setText(R.string.gesture_lock_view_verify_tip);
            }
        }

        mGlvgLock.setOnGestureLockViewListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MyApplication.getAppPresenter().setAppFront(true);
    }

    /***
     * 从登陆页面到此，需要延迟处理登陆事件
     */
    private void processLoginEvent(){
        if(mLoginEvent != null){
            EventBus.getDefault().post(mLoginEvent);
            //新用户登录弹出大礼包
            if (!mLoginEvent.isContinueNext()) {
                Intent cardIntent = new Intent(this, LoginSuccessCardDialog.class);
                cardIntent.putExtra(LoginSuccessCardDialog.CARD_DATA, mLoginEvent.getJsonObject());
                startActivity(cardIntent);
            }
        }
    }


    @Override
    public void onGestureEvent(boolean matched) {
        if(mLockType == TYPE_SETTING || mLockType == TYPE_PERSON_SETTING){
            if (matched) {
                mTvGestureLockTip.setTextColor(getResources().getColor(R.color.color_4a4a4a));
                mTvGestureLockTip.setText(R.string.gesture_lock_view_pattern);
                mTitleBar.getRightText().setEnabled(false);
                MyPreference.getInstance().write(GestureLockTools.getLockKey(), mGlvgLock.getChooseStr());
                MyApplication.getInstance().getUiHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        processLoginEvent();
                    }
                },1000);

            }else{
                mTvGestureLockTip.setText(R.string.gesture_lock_view_unpattern);
                mTvGestureLockTip.setTextColor(getResources().getColor(R.color.color_b92b27));
                if(mLockType == TYPE_SETTING){
                    mGlvgIndicator.setIndictorAnswer(null);
                    mGlvgLock.reInit();
                }

            }
        }else{
            if (matched) {
                MyPreference.getInstance().remove(GestureLockTools.getLockTryTimesKey());
                mTvGestureLockTip.setVisibility(View.GONE);
                if(mLockType == TYPE_PERSON_VERIFY_FOR_CLOSE){
                    MyPreference.getInstance().remove(GestureLockTools.getLockKey());
                }else if(mLockType == TYPE_PERSON_VERIFY_FOR_FIX){
//                    MyPreference.getInstance().remove(GestureLockTools.getLockKey());
                    GestureLockTools.goGestureLockView(this,TYPE_PERSON_SETTING);
                }
                //进入app
                finish();
            }else{
                MyPreference.getInstance().write(GestureLockTools.getLockTryTimesKey(),mGlvgLock.getTryTimes());
                mTvGestureLockTip.setText(String.format(Locale.CHINA,
                        getString(R.string.gesture_lock_view_unpattern_tip),
                        GESTURE_TRY_TIMES-mGlvgLock.getTryTimes(),mGlvgLock.getTryTimes()));
                mTvGestureLockTip.setTextColor(getResources().getColor(R.color.color_b92b27));
            }
        }

    }

    @Override
    public void onUnmatchedExceedBoundary() {
        //次数已经达到重试限定的次数
        LoginManager.loginOut(this);
        LoginManager.goLogin(this,0);
        finish();
    }

    @Override
    public void onFirstSetPattern(boolean patternOk) {
        if(mLockType != TYPE_SETTING && mLockType != TYPE_PERSON_SETTING){
            return;
        }
        mTvGestureLockTip.setText(patternOk ? R.string.gesture_lock_view_reconfirm :
                R.string.gesture_lock_view_set_unpattern);
        mTvGestureLockTip.setTextColor(patternOk ? getResources().getColor(R.color.color_9b9b9b) :
                getResources().getColor(R.color.color_b92b27));
        if(mLockType == TYPE_PERSON_SETTING && patternOk){
            mTitleBar.getRightText().setEnabled(true);
            mTitleBar.addRightText("重设",this);
        }
        if(patternOk) {
            mGlvgIndicator.setIndictorAnswer(mGlvgLock.getChoose());
        }
    }

    private void resetGesture(){
        mGlvgIndicator.setIndictorAnswer(null);
        mGlvgLock.reInit();
        mTvGestureLockTip.setText(R.string.gesture_lock_view_set_lock_tip);
        mTvGestureLockTip.setTextColor(getResources().getColor(R.color.color_4a4a4a));
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId){
            case R.id.top_title_right_icon:
                if(mLockType == GestureLockActivity.TYPE_SETTING){
                    processLoginEvent();
                    MyPreference.getInstance().write(GestureLockTools.getJumpLockViewKey(),true);
                    finish();
                }else if(mLockType == GestureLockActivity.TYPE_PERSON_SETTING){
                    mGlvgIndicator.setIndictorAnswer(null);
                    mGlvgLock.reInit();
                    mTvGestureLockTip.setText(R.string.gesture_lock_view_set_lock_tip);
                    mTvGestureLockTip.setTextColor(getResources().getColor(R.color.color_4a4a4a));
                    mTitleBar.getRightText().setVisibility(View.GONE);
                }
                break;
            case R.id.tv_forget_pwd://忘记密码
                MyPreference.getInstance().remove(GestureLockTools.getLockKey());
                onUnmatchedExceedBoundary();
                break;
            case R.id.tv_change_user://切换用户
                onUnmatchedExceedBoundary();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mLockType == TYPE_SETTING || mLockType == TYPE_LOCK){
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.activity_lollipop_close_exit);
    }
}
