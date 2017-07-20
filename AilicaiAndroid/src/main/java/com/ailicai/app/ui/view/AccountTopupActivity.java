package com.ailicai.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.ActivityRuleModel;
import com.ailicai.app.model.bean.BuyHuoqibaoResponse;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.request.AilicaiNoticeListOnRollRequest;
import com.ailicai.app.model.request.CurrentRollInBaseInfoRequest;
import com.ailicai.app.model.response.AilicaiNoticeListOnRollReponse;
import com.ailicai.app.model.response.CurrentRollInBaseInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.BuyCurrentPay;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.RollHotTopicView;
import com.huoqiu.framework.util.ManyiUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 账户充值
 * Created by nanshan on 2017/7/10.
 */
public class AccountTopupActivity extends BaseBindActivity {
    @Bind(R.id.top_title_view)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.index_hot_topic_view)
    RollHotTopicView mIndexHotTopicView;

    @Bind(R.id.roll_in_balance)
    TextView mRollInBalance;
    @Bind(R.id.confirm_btn)
    Button mConfirmBtn;
    @Bind(R.id.bank_card_name)
    TextView mBankCardName;
    @Bind(R.id.input_price_edit)
    EditText mInputPriceEdit;
    @Bind(R.id.input_price_edit_lable)
    TextView mInputPriceEditLable;

    @Bind(R.id.agreement_layout)
    LinearLayout mAgreementLayout;

    @Bind(R.id.agreement_checkbox)
    CheckBox mAgreementCheckbox;
    @Bind(R.id.input_error_tips)
    TextView mErrorTips;
    @Bind(R.id.price_del)
    TextView priceDel;
    @Bind(R.id.activity_content)
    TextView activityContent;
    @Bind(R.id.activity_layout_view)
    LinearLayout activityLayoutView;
    @Bind(R.id.open_net_bank_layout)
    LinearLayout mOpenNetBankLayout;
    @Bind(R.id.open_net_bank)
    TextView mOpenNetBank;

    private CurrentRollInBaseInfoResponse infoResponse;

    //接口定义卡类型,1：储蓄卡 、2：信用卡 、3：存折 、4：其它
    private Map<Integer, String> cardTypeMap = ObjectUtil.newHashMap();

    private long ruleId;
    private CurrentRollInBaseInfoResponse jsonObject;

    private static final String CASHIERDESKTYPE = "106";

    @Override
    public int getLayout() {
        return R.layout.activity_account_topup;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (infoResponse != null){
            ManyiUtils.showKeyBoard(this, mInputPriceEdit);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        cardTypeMap.put(1, "储蓄卡");
        cardTypeMap.put(2, "信用卡");
        cardTypeMap.put(3, "存折");
        cardTypeMap.put(4, "其它");
        mTopTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                //SystemUtil.HideSoftInput(CurrentRollInActivity.this);
                SystemUtil.hideKeyboard(mInputPriceEdit);
                finish();
                return true;
            }
        });
        mTopTitleView.setHasDivider(false);
        mConfirmBtn.setEnabled(false);

        //请求数据
        initAilicaiNoticeList();
        initBaseInfo();
//        initProtocol();

    }

    private void initAilicaiNoticeList() {
        showLoadTranstView();
        AilicaiNoticeListOnRollRequest request = new AilicaiNoticeListOnRollRequest();
        ServiceSender.exec(this, request, new IwjwRespListener<AilicaiNoticeListOnRollReponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(AilicaiNoticeListOnRollReponse jsonObject) {
                showContentView();
                bindAlicaiNoticeData(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    private void bindAlicaiNoticeData(AilicaiNoticeListOnRollReponse jsonObject) {
        if (jsonObject.getBankNotices() != null && !jsonObject.getBankNotices().isEmpty()) {
            mIndexHotTopicView.setVisibility(View.VISIBLE);
            mIndexHotTopicView.updateView4AlicaiData(AccountTopupActivity.this, jsonObject.getBankNotices());
        } else {
            mIndexHotTopicView.setVisibility(View.GONE);
        }
    }

    private ProtocolHelper protocolHelper;

    private void initProtocol() {
        protocolHelper = new ProtocolHelper(this, ProtocolHelper.TYPE_WALLET_ROLL_IN);
    }

    /**
     * 用户协议点击事件
     */
    @Nullable
    @OnClick(R.id.user_agreement_link)
    public void onUserAgreementLink() {
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);
        if (infoResponse != null) {
            protocolHelper.setProtocols(infoResponse.getProtocolList());
        }
        protocolHelper.loadProtocol(true);
    }

    @OnCheckedChanged(R.id.agreement_checkbox)
    public void onBoxChange(CheckBox box, boolean isChecked) {
        if (isChecked && checkInputMoney()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }

    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                mInputPriceEdit.setText(s);
                mInputPriceEdit.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            mInputPriceEdit.setText(s);
            mInputPriceEdit.setSelection(2);
        }
        /*
        //0开始后面不能输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                mInputPriceEdit.setText(s.subSequence(0, 1));
                mInputPriceEdit.setSelection(1);
                return;
            }
        }
        */

        //0789 -> 789,00->0
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".") && !s.toString().substring(1, 2).equals("0")) {
                mInputPriceEdit.setText(s.subSequence(1, s.toString().trim().length()));
                mInputPriceEdit.setSelection(s.length() - 1);
                return;
            } else if (s.toString().substring(1, 2).equals("0")) {
                mInputPriceEdit.setText(s.subSequence(0, 1));
                mInputPriceEdit.setSelection(1);
                return;
            }
        }
    }

    @OnClick(R.id.price_del)
    public void onEditTextDelClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setText("");
            activityContent.setText("");
        }
    }

    @OnClick(R.id.input_price_edit)
    public void onEditTextClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }
    @OnClick(R.id.open_net_bank_layout)
    public void openNetBankLayout(View v) {
        if (infoResponse != null && !TextUtils.isEmpty(infoResponse.getOpenEBankDetailUrl())) {
            Intent intent = new Intent(this, RegularFinanceDetailH5Activity.class);
            intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, infoResponse.getOpenEBankDetailUrl());
            startActivity(intent);
        }
    }

    @Nullable
    @OnClick(R.id.confirm_btn)
    public void onConfirmClick() {
        if (!checkInputMoneyOnConfirm()) {
            return;
        }
        String money = mInputPriceEdit.getText().toString();
        BuyCurrentPay.CurrentPayInfo currentPayInfo = new BuyCurrentPay.CurrentPayInfo();
        currentPayInfo.setAmount(Double.parseDouble(money));
        currentPayInfo.setAccountType(CASHIERDESKTYPE);
        if (infoResponse.getActivity() != null) {
            currentPayInfo.setRuleId(ruleId);
            currentPayInfo.setActivityId(infoResponse.getActivity().getActivityId());
            currentPayInfo.setConfigId(infoResponse.getActivity().getConfigId());
            currentPayInfo.setRelationId(infoResponse.getActivity().getRelationId());

        }
        BuyCurrentPay buyCurrentPay = new BuyCurrentPay(this, currentPayInfo, new IwPwdPayResultListener<BuyHuoqibaoResponse>() {
            @Override
            public void onPayComplete(BuyHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayStateDelay(String msgInfo, BuyHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, BuyHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });
        buyCurrentPay.pay();
//        EventLog.upEventLog("201610281", "sub", "into_syt");
    }

    public void startActivity(BuyHuoqibaoResponse object) {
        finish();
        Intent mIntent = new Intent(mContext, AccountTransactionResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(AccountTransactionResultActivity.KEY, object);
        mBundle.putSerializable(AccountTransactionResultActivity.TRANSACTIONTYPE, AccountTransactionResultActivity.TOPUP);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
    }

    /**
     * 输入的金额
     *
     * @return
     */
    private boolean checkInputMoney() {
        if(infoResponse == null){
            return false;
        }
        mErrorTips.setVisibility(View.GONE);
        mInputPriceEditLable.setVisibility(View.GONE);
        mRollInBalance.setVisibility(View.VISIBLE);
        priceDel.setVisibility(View.VISIBLE);
        String money = mInputPriceEdit.getText().toString();
        double x = Math.min(infoResponse.getBuyLimit(),infoResponse.getDayRemain());
        if (!"".equals(money) && CommonUtil.isMoneyNumber(money)) {
            showAutoActivity(Double.parseDouble(money));
        }
        if ("".equals(money)) {
            mErrorTips.setText("");
            mInputPriceEditLable.setVisibility(View.VISIBLE);
            mRollInBalance.setVisibility(View.VISIBLE);
            priceDel.setVisibility(View.GONE);
            return false;
        } else if (!CommonUtil.isMoneyNumber(money)) {
            //ToastUtil.show("请输入正确的金额");
            return false;
        } else if (Double.parseDouble(money) <= 0) {
            //mErrorTips.setText(infoResponse.getBuyMinLimitStr());
            mErrorTips.setText("最低转入" + infoResponse.getFormatBuyMinLimitStr() + "元");
            mErrorTips.setVisibility(View.VISIBLE);
            mRollInBalance.setVisibility(View.GONE);
            return false;
        } else if (infoResponse.getDayRemain() >= 0 && Double.parseDouble(money) > x) {
            DecimalFormat df = new DecimalFormat("######0.00");
            if (infoResponse.getBuyLimit() > infoResponse.getDayRemain()){
                mErrorTips.setText("当前最多可转入" + df.format(x) + "元");
            }else{
                mErrorTips.setText("单笔最多可转入" + df.format(x) + "元");
            }
            mErrorTips.setVisibility(View.VISIBLE);
            mRollInBalance.setVisibility(View.GONE);
            return false;
        }else if (infoResponse.getBuyMinLimit() > 0 && Double.parseDouble(money) < infoResponse.getBuyMinLimit()) {
            //mErrorTips.setText(infoResponse.getBuyMinLimitStr());
            mErrorTips.setText("最低转入" + infoResponse.getFormatBuyMinLimitStr() + "元");
            mErrorTips.setVisibility(View.VISIBLE);
            mRollInBalance.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    private boolean checkInputMoneyOnConfirm() {
        String money = mInputPriceEdit.getText().toString();
        if (!CommonUtil.isMoneyNumber(money)) {
            ToastUtil.show("请输入正确的金额");
            return false;
        } else if (Double.parseDouble(money) <= 0) {
            ToastUtil.show("请输入正确的金额");
            return false;
        } else if (infoResponse.getBuyLimit() > 0 && Double.parseDouble(money) > infoResponse.getBuyLimit()) {
            //ToastUtil.show("单笔最多" + infoResponse.getBuyLimitStr());
            ToastUtil.show("最高转入" + infoResponse.getBuyLimit() + "元");
            return false;
        } else if (infoResponse.getBuyMinLimit() > 0 && Double.parseDouble(money) < infoResponse.getBuyMinLimit()) {
            //ToastUtil.show(infoResponse.getBuyMinLimitStr());
            ToastUtil.show("最低转入" + infoResponse.getBuyMinLimit() + "元");
            return false;
        }
        return true;
    }

    public void bindViewData(CurrentRollInBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        verifyProtocolListLogical(jsonObject.getProtocolList());
        String giveDate = jsonObject.getGiveDate(); //到账日期
        String cardNo = jsonObject.getCardNo(); //银行卡号（尾号）
        String bankName = jsonObject.getBankName(); //银行名称
        int cardType = jsonObject.getCardType(); //卡类型【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
        SpannableUtil spanUtil = new SpannableUtil(this);
        SpannableStringBuilder builder1 = null;

        mBankCardName.setText(bankName + "卡(尾号" + cardNo + ") 充值 账户余额");
        builder1 = spanUtil.getSpannableString("账户可用余额 ", CommonUtil.numberFormatWithTwoDigital(jsonObject.getWithdrawBalance()), " 元",
                R.style.text_14_757575, R.style.text_14_757575, R.style.text_14_757575);

        mRollInBalance.setText(builder1);
        mInputPriceEditLable.setText(jsonObject.getHint());

        List<ActivityRuleModel> ruleList = ObjectUtil.newArrayList();
        if (infoResponse != null && infoResponse.getActivity() != null) {
            ruleList = infoResponse.getActivity().getRuleList();
        }
        if (!ruleList.isEmpty()) {
            activityLayoutView.setVisibility(View.VISIBLE);
        } else {
            activityLayoutView.setVisibility(View.GONE);
        }
        mOpenNetBankLayout.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(infoResponse.getOpenEBankTitle())){
            mOpenNetBankLayout.setVisibility(View.VISIBLE);
            mOpenNetBank.setText(infoResponse.getOpenEBankTitle());
        }
    }

    @Override
    public void verifyProtocolListLogical(List<Protocol> list) {
        if (list != null && list.size() == 0){
            if (mAgreementLayout != null){
                mAgreementLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 页面数据获取请求
     */
    public void initBaseInfo() {
        showLoadTranstView();
        CurrentRollInBaseInfoRequest request = new CurrentRollInBaseInfoRequest();
        request.setAccountType(CASHIERDESKTYPE);
        ServiceSender.exec(this, request, new IwjwRespListener<CurrentRollInBaseInfoResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(CurrentRollInBaseInfoResponse jsonObject) {
                showContentView();
                String msg = jsonObject.getMessage();
                int code = jsonObject.getErrorCode();
                if (!"".equals(msg)) {
                    //ToastUtil.showInCenter(mContext, msg);
                }
                bindViewData(jsonObject);
                SystemUtil.showKeyboard(mInputPriceEdit);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    @OnClick(R.id.activity_help_btn)
    public void onHelpClick(View v) {
        showActivityDialog();
    }

    public void showAutoActivity(double inputMoney) {
        List<ActivityRuleModel> ruleList = ObjectUtil.newArrayList();
        if (infoResponse != null && infoResponse.getActivity() != null) {
            ruleList = infoResponse.getActivity().getRuleList();
        }
        /**
         if (!ruleList.isEmpty()) {
         for (int i = ruleList.size() - 1; i > -1; i--) {
         ActivityRuleModel model = ruleList.get(i);
         if (inputMoney >= model.getReachRule()) {
         activityContent.setText("满" + model.getReachRule() + "返" + model.getAwardMoney());
         ruleId = model.getRuleId();
         return;
         } else {
         activityContent.setText("");
         ruleId = 0;
         return;
         }
         }
         }
         */

        boolean hasRule = false;
        if (!ruleList.isEmpty()) {
            for (int i = 0; i < ruleList.size(); i++) {
                ActivityRuleModel model = ruleList.get(i);
                if (inputMoney >= model.getReachRule()) {
                    //activityContent.setText("满" + model.getReachRule() + "返" + model.getAwardMoney());
                    activityContent.setText(model.getMemo());
                    ruleId = model.getRuleId();
                    hasRule = true;
                }
            }
        }
        if (!hasRule) {
            ruleId = 0;
            activityContent.setText("");
        }

    }

    public void showActivityDialog() {
        View contentView = View.inflate(this, R.layout.dialog_wallet_activity_list, null);
        TextView setTimeBtn = (TextView) contentView.findViewById(R.id.i_know_btn);
        ListView myList = (ListView) contentView.findViewById(R.id.activity_list);
        LinearLayout activityView = (LinearLayout) contentView.findViewById(R.id.activity_view);
        final AlertDialog shareAlertDialog = DialogBuilder.getAlertDialog(this, R.style.AppCompatDialog2).setView(contentView).show();
        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlertDialog.dismiss();
            }
        });

        List<ActivityRuleModel> ruleList = ObjectUtil.newArrayList();
        if (infoResponse != null && infoResponse.getActivity() != null) {
            ruleList = infoResponse.getActivity().getRuleList();
        }
        if (!ruleList.isEmpty()) {
            ActivityListAdapter adapter = new ActivityListAdapter(ruleList);
            myList.setAdapter(adapter);
            if (ruleList.size() > 5) {
                activityView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getPixelFromDip(mContext, 180)));
            }
        }

    }

    class ActivityListAdapter extends BaseAdapter {

        private List<ActivityRuleModel> ruleList;

        public ActivityListAdapter(List<ActivityRuleModel> ruleList) {
            this.ruleList = ruleList;
        }

        @Override
        public int getCount() {
            return ruleList.size();
        }

        @Override
        public Object getItem(int position) {
            return ruleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.wallet_activity_list_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ActivityRuleModel detail = (ActivityRuleModel) getItem(position);
            viewHolder.index.setText(position + 1 + ".");
            viewHolder.memo.setText(detail.getMemo());
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.index_num)
            TextView index;
            @Bind(R.id.memo_text)
            TextView memo;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemUtil.hideKeyboard(mInputPriceEdit);
    }

}
