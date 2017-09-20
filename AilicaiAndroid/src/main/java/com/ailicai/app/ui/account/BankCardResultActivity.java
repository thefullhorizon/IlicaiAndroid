package com.ailicai.app.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.rsa.RSAEncrypt;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.request.account.QueryCardBinRequest;
import com.ailicai.app.model.response.account.QueryCardBinResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tangxiaolong on 2015/11/6.
 */
public class BankCardResultActivity extends BaseBindActivity {

    @Bind(R.id.top_title)
    IWTopTitleView topTitleView;
    @Bind(R.id.idShotsIv)
    ImageView idShotsIv;
    @Bind(R.id.bank_card_container)
    LinearLayout mContainer;
    private final int RC_PAGE_BACK = 1;

    private List<EditText> mEditTexts = new ArrayList<>();
    private Bitmap mBitmap;
    private String cardBinNextUrl;

    /**
     * 开户流程步数
     **/
    @Bind(R.id.openaccountStepContainer)
    View openaccountStepContainer;
    @Bind(R.id.llOpenAccountBonusContainer)
    LinearLayout llOpenAccountBonusContainer;
    @Bind(R.id.tfStep01)
    TextViewTF tfStep01;
    @Bind(R.id.tfStep02)
    TextViewTF tfStep02;
    @Bind(R.id.tfStep03)
    TextViewTF tfStep03;
    @Bind(R.id.textViewStep01)
    TextView textViewStep01;
    @Bind(R.id.textViewStep02)
    TextView textViewStep02;
    @Bind(R.id.lineStep01Whith02)
    View lineStep01Whith02;
    @Bind(R.id.lineStep02Whith03)
    View lineStep02Whith03;
    @Bind(R.id.tvOpenAccountBonus)
    TextView tvOpenAccountBonus;
    @Bind(R.id.tvLastProcessName)
    TextView tvLastProcessName;
    /**
     * 开户流程步数
     **/

    OpenAccountFeature openAccountFeature = OpenAccountFeature.openAccountUseNewBankCard();

    @Override
    public int getLayout() {
        return R.layout.activity_bank_card_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        topTitleView.setDark(false);

        topTitleView.addRightText("支持银行", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountInfo.isRealNameVerify()) {
                    EventLog.upEventLog("683", "supercard", "setcard");
                } else {
                    EventLog.upEventLog("682", "supercard", "setcard");
                }
                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.TITLE, "支持银行");
                dataMap.put(WebViewActivity.NEED_REFRESH, "0");
                dataMap.put(WebViewActivity.URL, openAccountFeature.supportCardsUrl);
                MyIntent.startActivity(BankCardResultActivity.this, WebViewActivity.class, dataMap);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            byte buff[] = extras.getByteArray("cardIdshots");
            mBitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
            idShotsIv.setImageBitmap(mBitmap);
            cardBinNextUrl = extras.getString("url");

            initCardContainer((String) extras.get("bankCardNumber"));
        }

        //setOpenAccountStepTo2();

        // 绑银行卡并且未开户
        if(openAccountFeature.cardBinType == 0 && !AccountInfo.isOpenAccount()) {
            setOpenAccountStepTo2();
        } else {
            openaccountStepContainer.setVisibility(View.GONE);
        }

    }

    private void initCardContainer(String cardNo) {
        String[] pieces = cardNo.split(" ");

        for (int i = 0; i < pieces.length; i++) {
            if (i > 0) {
                View split = new View(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UIUtils.dipToPx(this, .5f), LinearLayout.LayoutParams.MATCH_PARENT);
                split.setBackgroundColor(0xFFCCCCCC);
                split.setLayoutParams(lp);
                mContainer.addView(split);
            }

            LinearLayout.LayoutParams linerParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            linerParams.weight = pieces[i].length();

            FrameLayout.LayoutParams frameLayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

            //EditText和TextView 切换使用
            FrameLayout layout = new FrameLayout(this);

            final EditText edit = new EditText(this);

            edit.setTextAppearance(this, R.style.text_20_de000000);
            edit.getPaint().setFakeBoldText(true);
            edit.setSingleLine(true);
            edit.setPadding(0, 0, 0, 0);

            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setBackgroundDrawable(null);
            edit.setSelectAllOnFocus(true);

            edit.setGravity(Gravity.CENTER);
            edit.setText(pieces[i]);

            layout.addView(edit, frameLayout);

            final TextView text = new TextView(this);
            text.setTextAppearance(this, R.style.text_20_de000000);
            text.getPaint().setFakeBoldText(true);
            text.setSingleLine(true);
            text.setEllipsize(TextUtils.TruncateAt.END);

            text.setGravity(Gravity.CENTER);
            text.setText(pieces[i]);
            text.setBackgroundColor(0xffffffff);

            layout.addView(text, frameLayout);

            //使得两个view的内容大小一样
            text.setPadding(edit.getPaddingLeft(), edit.getPaddingTop(), edit.getPaddingRight(), edit.getPaddingBottom());

            edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) text.setVisibility(View.GONE);
                    else text.setVisibility(View.VISIBLE);

                    String value = edit.getText().toString();
                    text.setText(value);

                    //计算TextView Gravity
                    float valueWidth = text.getPaint().measureText(value);
                    float textWidth = text.getWidth() - text.getPaddingLeft() - text.getPaddingRight();
                    if (valueWidth > textWidth) {
                        text.setGravity(Gravity.CENTER | Gravity.LEFT);
                        //计算value的长度 使TextView显示省略号... 系统有只能显示.的bug
                        CharSequence ellipsize = TextUtils.ellipsize(value, text.getPaint(), textWidth, TextUtils.TruncateAt.END);
                        text.setText(ellipsize);
                    } else text.setGravity(Gravity.CENTER);

                }
            });

            mContainer.addView(layout, linerParams);
            mEditTexts.add(edit);
        }
    }

    private String getBankCardNumber() {
        String cardNo = "";
        for (EditText edit : mEditTexts) {
            cardNo += edit.getText().toString();
        }
        return cardNo;
    }

    private void setOpenAccountStepTo2() {

        openaccountStepContainer.setVisibility(View.VISIBLE);

        tfStep01.setTextAppearance(this, R.style.textview_openaccount_already_pass_step_left);
        tfStep01.setPadding(UIUtils.dipToPx(this, 24), 0, 0, 0);

        lineStep01Whith02.setBackgroundColor(Color.parseColor("#f75a14"));

        tfStep02.setTextAppearance(this, R.style.textview_openaccount_current_step_center_for_native);
        tfStep02.setText(getResources().getString(R.string.open_account_step_for_native));
        tfStep03.setTextAppearance(this, R.style.textview_openaccount_not_reach_step_right);
        int processNameLength = AccountInfo.getOpenAccountLastProcessName().length();
        tfStep03.setPadding(0, 0, UIUtils.dipToPx(this, (processNameLength * 14 / 2) - 4), 0);

        lineStep02Whith03.setBackgroundColor(Color.parseColor("#e6e6e6"));
        if (!TextUtils.isEmpty(AccountInfo.getOpenAccountActivityMemo())) {
            llOpenAccountBonusContainer.setVisibility(View.VISIBLE);
            tvOpenAccountBonus.setText(AccountInfo.getOpenAccountLastProcessName()+"，即可获得" + AccountInfo.getOpenAccountActivityMemo());
        } else {
            llOpenAccountBonusContainer.setVisibility(View.GONE);
        }
        textViewStep01.setTextColor(Color.parseColor("#757575"));
        textViewStep02.setTextColor(Color.parseColor("#f75a14"));
        tvLastProcessName.setText(AccountInfo.getOpenAccountLastProcessName());
    }

    @OnClick(R.id.next)
    void next() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }

        //开户流程中点击下一步添加埋点
        EventLog.upEventLog("682", "next", "setcard");

        String cardNo = getBankCardNumber();

        if (cardNo.length() < 13 || cardNo.length() > 19) {
            ToastUtil.show("请输入正确的银行卡号!");
            return;
        }

        // 把银行卡号传回刚才输入的地方
        Intent intent = new Intent();
        intent.putExtra("bankCardNumber", cardNo);
        setResult(RESULT_OK, intent);

        queryCardBin(cardNo);

        EventLog.getInstance().init();
        EventLog.getInstance().setActionType(String.valueOf(190));
        int scanRight = 1;
        EventLog.getInstance().setActionValue(String.valueOf(scanRight));
        EventLog.getInstance().upSearchInfo();
    }

    @Override
    public void onPause() {
        SystemUtil.HideSoftInput(this);
        super.onPause();
    }

    private void queryCardBin(final String cardNo) {
        showLoadTranstView();
        QueryCardBinRequest request = new QueryCardBinRequest();
        request.setCardNo(RSAEncrypt.encrypt(cardNo));
        request.setCardBinType(openAccountFeature.cardBinType);
        ServiceSender.exec(this, request, new IwjwRespListener<QueryCardBinResponse>() {
            @Override
            public void onJsonSuccess(QueryCardBinResponse response) {
                showContentView();
                if (response.getErrorCode() == RestException.PAY_LIMIT_ERROR) {
                    showSolutionDialog(response.getMessage());
                } else {
                    if (response.getValid() == 1) {
                        Map<String, String> dataMap = ObjectUtil.newHashMap();
                        dataMap.put(BaseWebViewActivity.URL, cardBinNextUrl + "?cardNo=" + cardNo);
                        dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
                        dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
                        MyIntent.startActivity(BankCardResultActivity.this, OpenAccountWebViewActivity.class, dataMap);
                        finish();
                    } else {
                        switch (response.getCardType()) {
                            case 0:// 0则卡bin失败
                                ToastUtil.showInCenter("请输入正确的银行卡号");
                                break;
                            case 2:
                                ToastUtil.showInCenter("不支持信用卡");
                                break;
                            default:
                                ToastUtil.showInCenter("暂不支持该银行卡");
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String error) {
                showContentView();
                ToastUtil.showInCenter(error);
            }
        });
    }

    private void showSolutionDialog(String message) {
        new AlertDialog.Builder(BankCardResultActivity.this, R.style.AppCompatDialog).setPositiveButton("查看帮助", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.TITLE, "解决方案");
                dataMap.put(WebViewActivity.NEED_REFRESH, "0");
//                dataMap.put(WebViewActivity.URL, OnlinePayActivity.cardLimitSolutionUrl);
                MyIntent.startActivity(BankCardResultActivity.this, WebViewActivity.class, dataMap);
            }
        }).setNegativeButton("返回", null).setMessage(message).setCancelable(false).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (OpenAccountActivity.feature.cardBinType == 0) {
//            EventLog.upEventLog("682", "back", "setcard");
//        }

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_PAGE_BACK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}