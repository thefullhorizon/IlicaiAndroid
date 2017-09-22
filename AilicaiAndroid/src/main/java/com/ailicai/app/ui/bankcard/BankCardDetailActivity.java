package com.ailicai.app.ui.bankcard;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.response.BankCardDetailResponse;
import com.ailicai.app.ui.account.OpenAccountWebViewActivity;
import com.ailicai.app.ui.bankcard.presenter.BankCardDetailPresenter;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.UnbindPwdCheckDialog;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.widget.BottomSheetsDialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;

/**
 * name: BankCardListActivity <BR>
 * description: 银行卡详情页面<BR>
 * create date: 2017/9/20
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardDetailActivity extends BaseBindActivity {

    BankCardDetailPresenter presenter;

    @Bind(R.id.main_container)
    View main_container;
    @Bind(R.id.topTitleView)
    IWTopTitleView topTitleView;
    @Bind(R.id.ivBankIcon)
    ImageView ivBankIcon;
    @Bind(R.id.tvBankName)
    TextView tvBankName;
    @Bind(R.id.tvBankNumber)
    TextView tvBankNumber;
    @Bind(R.id.tvSingleLimit)
    TextView tvSingleLimit;
    @Bind(R.id.tvSingleDayLimit)
    TextView tvSingleDayLimit;
    @Bind(R.id.tvDesc)
    TextView tvDesc;

    private BankCardDetailResponse response;

    @Override
    public int getLayout() {
        return R.layout.activity_bankcard_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter = new BankCardDetailPresenter(this);
        presenter.init();
    }

    public void initData() {
        topTitleView.setBgColor(R.color.white);
        topTitleView.addRightText(R.string.attention_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog();
            }
        });
    }

    public void bindViewData(BankCardDetailResponse response) {
        this.response = response;
        setAllBgColorAnimation(response);
        tvBankName.setText(response.getBankName()+response.getCardTypeDesc());
        tvBankNumber.setText("**** **** ****"+response.getCardNoDesc());
        tvSingleLimit.setText(response.getEachPayMax());
        tvSingleDayLimit.setText(response.getEachDayPayMax());

        String desc = "为了您的账户安全，仅支持绑定一张银行卡\n如需帮助，请联系客服";
        SpannableString spanableInfo = new SpannableString(desc + " " +"400-700-6622");
        int start = +desc.length();
        desc = desc + " " +"400-700-6622";
        int end = desc.length();
        spanableInfo.setSpan(new Clickable("400-700-6622"), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvDesc.setText(spanableInfo);
        //setMovementMethod()该方法必须调用，否则点击事件不响应
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        avoidHintColor(tvDesc);
    }

    private void setAllBgColorAnimation(BankCardDetailResponse response) {

        int iconResId = presenter.getBankIconResId(response.getBankCode());
        ivBankIcon.setImageResource(iconResId);
        int bgColorResId = presenter.getBankBgColorId(response.getBankCode());
        topTitleView.setBgColor(bgColorResId);
        Animation alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        topTitleView.setAnimation(alphaAnimation);
        main_container.setBackgroundResource(bgColorResId);
        main_container.setAnimation(alphaAnimation);
//        int colorA = Color.parseColor("#ffffff");
//        int colorB = getResources().getColor(iconResId);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(topTitleView,"backgroundColor",colorA,colorB);
//        objectAnimator.setDuration(1000);
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//        objectAnimator.start();
//
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(main_container,"backgroundColor",colorA,colorB);
//        objectAnimator2.setDuration(1000);
//        objectAnimator2.setEvaluator(new ArgbEvaluator());
//        objectAnimator2.start();
    }

    private void showMoreDialog() {
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("更换银行卡");
        arrayList.add("查看支持银行及限额说明");
        arrayList.add("该卡已注销或挂失，如何更换");
        BottomSheetsDialogBuilder.showSimpleDialog(this, arrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // 更换银行卡
                    goToExSafeCardValid();
                } else if(position == 1) {
                    // 查看银行卡限额
                    goToBankListLimitDesc();
                } else if(position == 2) {
                    // 该卡已注销或挂失，如何更换
                    goToExSafeCardInValid();
                }
            }
        });
    }

    private void goToExSafeCardInValid() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, "原银行卡不可用");
        dataMap.put(WebViewActivity.URL, response.getChangeSafeCardUrl());
        dataMap.put(WebViewActivity.NEED_REFRESH, String.valueOf(0));
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    private void goToBankListLimitDesc() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getResources().getString(R.string.support_cards));
        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
        dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getSafeBankSupport());
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    private void goToExSafeCardValid() {
        if (isPropertyBiggerThanChangeLimit()) {
            goToPropertyBiggerThanChangeLimit();
        } else {
            goToPropertyLessThanChangeLimit();
        }
    }

    private boolean isPropertyBiggerThanChangeLimit() {
        double property = response.getTotalAsset();
        return property > response.getChangeLimit();
    }

    private void goToPropertyBiggerThanChangeLimit() {
        Intent intent = new Intent(this, ExSafeCardValidBiggerThanChangeLimitActivity.class);
        intent.putExtra("property", response.getTotalAsset());
        intent.putExtra("changeLimit", response.getChangeLimit());
        startActivity(intent);
    }

    private void goToPropertyLessThanChangeLimit() {
        UnbindPwdCheckDialog unbindPwdCheckDialog = new UnbindPwdCheckDialog(this, response.getBankAccountId(), new IwPwdPayResultListener() {
            @Override
            public void onPayPwdTryAgain() {
                goToPropertyLessThanChangeLimit();
            }

            @Override
            public void onPayComplete(Object object) {

                setHasNoSafeCard();
                goToChangeSafeCard();

                finish();
            }

            @Override
            public void onPayStateDelay(String msgInfo, Object object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, Object object) {

            }
        });
        unbindPwdCheckDialog.show();
    }

    private void setHasNoSafeCard() {

        // 设置UserInfo中
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
        infoBase.setHasSafeCard(0);
        UserManager.getInstance(MyApplication.getInstance()).saveUser(infoBase);

        // 设置accountInfo中无银行卡
        AccountInfo.setHasSafeCard(false);

        //请求用户信息接口更新用户信息
        LoginManager.updateUserInfoData();
    }

    private void goToChangeSafeCard() {
        // 换银行卡
        OpenAccountWebViewActivity.goToBindNewSafeCard(this);
    }

    class Clickable extends ClickableSpan {

        String mobileNumber;

        public Clickable(String mobileNumber) {
            super();
            this.mobileNumber = mobileNumber;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.bgColor = getResources().getColor(R.color.transparent);
            ds.setColor(Color.parseColor("#ffffff"));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            avoidHintColor(v);
            Intent intent = new Intent(Intent.ACTION_DIAL,  Uri.parse("tel:" + mobileNumber));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.init();
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }
}
