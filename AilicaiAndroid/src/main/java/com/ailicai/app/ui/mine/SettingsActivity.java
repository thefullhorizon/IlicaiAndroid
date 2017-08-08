package com.ailicai.app.ui.mine;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.utils.GestureLockTools;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.eventbus.EditUserInfoEvent;
import com.ailicai.app.eventbus.ExitEvent;
import com.ailicai.app.eventbus.UserInfoUpdateEvent;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.gesture.GestureLockActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.ui.paypassword.PayPwdManageActivity;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.ToggleButton;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Gerry on 2017/7/18.
 */

public class SettingsActivity extends BaseBindActivity implements ToggleButton.OnToggleChanged{
    private final static int REQUEST_CODE_OPEN_ACCOUNT = 10001;
    private final static int REQUEST_CODE_GESTURE_LOCK = 10002;
    @Bind(R.id.user_phone_tag)
    TextView mPhoneTag;
    @Bind(R.id.real_name)
    TextView mRealName;
    @Bind(R.id.login_out)
    LinearLayout mLoginOut;
    @Bind(R.id.mine_password_manage_container)
    LinearLayout mPasswordManage;
    @Bind(R.id.tb_control_gesture_lock)
    ToggleButton mTbControlGestureLock;
    @Bind(R.id.rl_fix_gesture_lock_container)
    View mVFixGestureLockContainer;

    Handler mBackHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            finish();
            return false;
        }
    });
    private Map<String, Object> dataMap;

    @Override
    public int getLayout() {
        return R.layout.settings_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (getIntent() != null) {
            dataMap = MyIntent.getData(getIntent());
            setUserInfo(dataMap);
        } else if (savedInstanceState != null) {
            dataMap = MyIntent.getData(savedInstanceState);
            setUserInfo(dataMap);
        }
        mTbControlGestureLock.setOnToggleChanged(this);

        setToggleStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserInfo.getInstance().getLoginState() != UserInfo.LOGIN){
            finish();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 获取页面传参的数据Map
     *
     * @return
     */
    public Map<String, Object> getDataMap() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
            Map<String, Object> dataMap = ObjectUtil.newHashMap();
            dataMap.put(CommonTag.PERSONAL_USER_ID, infoBase.getUserId());
            dataMap.put(CommonTag.PERSONAL_USER_NAME, infoBase.getRealName());
            dataMap.put(CommonTag.PERSONAL_USER_R_NAME, infoBase.getrName());
            dataMap.put(CommonTag.PERSONAL_USER_SEX, infoBase.getGender());
            dataMap.put(CommonTag.PERSONAL_USER_PHONE, infoBase.getMobile());
            dataMap.put(CommonTag.PERSONAL_USER_ISREALNAMEVERIFY, infoBase.getIsRealNameVerify());
            dataMap.put(CommonTag.PERSONAL_USER_IDCARDNUMBER, infoBase.getIdCardNo());
            return dataMap;
        }
        return ObjectUtil.newHashMap();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEditUserInfoEvent(EditUserInfoEvent event) {
        LogUtil.d("=====getMobile====1===" + event.getMobile());
        mPhoneTag.setText(StringUtil.formatMobileSubTwo(event.getMobile()));
    }

    /**
     * 用户信息获取接口返回后发送事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUserInfoUpdateEvent(UserInfoUpdateEvent event) {
        dataMap = getDataMap();
        LogUtil.d("=====getMobile====2===" + MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE));
        setUserInfo(dataMap);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (dataMap != null) {
            MyIntent.setData(outState, dataMap);
        }
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.check_real_name)
    void checkRealName() {
        if (MapUtil.getInt(dataMap, CommonTag.PERSONAL_USER_ISREALNAMEVERIFY) == 1) {
            MyIntent.startActivity(mContext, RealUserInfoActivity.class, dataMap);
        } else {
            //TODO:未实名
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OPEN_ACCOUNT);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPEN_ACCOUNT:
                    //开户成功
                    MyIntent.startActivity(mContext, RealUserInfoActivity.class, dataMap);
                    break;
                case REQUEST_CODE_GESTURE_LOCK:
                    setToggleStatus();
                    break;
            }
        }
    }

    private void setToggleStatus(){
        if(TextUtils.isEmpty(MyPreference.getInstance().read(GestureLockTools.getLockKey(),""))){
            mTbControlGestureLock.toggleOff();
            MyPreference.getInstance().write(GestureLockTools.getLockEnableKey(),false);
        }else{
            mTbControlGestureLock.toggleOn();
            MyPreference.getInstance().write(GestureLockTools.getLockEnableKey(),true);
        }
    }

    @OnClick(R.id.change_transaction_password)
    void changePassword() {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            // 判断是否设置支付密码 0:否，1:是
            boolean isSetPayPwd = UserManager.getInstance(MyApplication.getInstance()).isSetPayPwd();
            if (isSetPayPwd) {
                MyIntent.startActivity(mContext, PayPwdManageActivity.class, "");
            } else {
                //开户
                MyIntent.startActivity(mContext, ProcessActivity.class, "");
            }
        }
    }

    @OnClick(R.id.change_phone)
    void changePhone() {
        MyIntent.startActivity(mContext, UserPhoneValidateActivity.class, dataMap);
    }

    public void setUserInfo(Map<String, Object> dataMap) {
        mPhoneTag.setText(StringUtil.formatMobileSubTwo(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE)));
        if (MapUtil.getInt(dataMap, CommonTag.PERSONAL_USER_ISREALNAMEVERIFY) == 1) {
            mRealName.setText("已实名");
        } else {
            mRealName.setText("未实名");
        }
        setUIData();
    }

    @OnClick(R.id.rl_fix_gesture_lock_container)
    void fixGestureLockClick(){
        GestureLockTools.goGestureLockView(this,GestureLockActivity.TYPE_PERSON_VERIFY_FOR_FIX);
    }

    public void setUIData() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            mLoginOut.setVisibility(View.GONE);
        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            mLoginOut.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.login_out)
    void onClickLoginOut() {
        DialogBuilder.showSimpleDialog(this, getString(R.string.loginout_tips_text), null, "取消", null, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExitEvent exitEvent = new ExitEvent();
                exitEvent.setForceExit(false);
                EventBus.getDefault().post(exitEvent);
                setUIData();
                mBackHandler.sendEmptyMessageDelayed(0, 200);
            }
        });
    }


    @Override
    public void onToggle(boolean fromClick,boolean on) {
        Intent intent;
        if(fromClick){
            if(!on){
                intent = GestureLockTools.getGestureLockIntent(this,GestureLockActivity.TYPE_PERSON_VERIFY_FOR_CLOSE);
            }else{
                MyPreference.getInstance().write(GestureLockTools.getJumpLockViewKey(),false);
                intent = GestureLockTools.getGestureLockIntent(this,GestureLockActivity.TYPE_PERSON_SETTING);
            }
            if(intent != null){
                startActivityForResult(intent,REQUEST_CODE_GESTURE_LOCK);
            }
        }

        mVFixGestureLockContainer.setVisibility(on ? View.VISIBLE : View.GONE);
    }


}
