package com.ailicai.app.ui.asset;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.eventbus.CapitalApplyRefreshEvent;
import com.ailicai.app.eventbus.CapitalProductTypeChangeEvent;
import com.ailicai.app.model.request.CheckTransferStatusRequest;
import com.ailicai.app.model.request.ReserveCancelRequest;
import com.ailicai.app.model.response.AutoBidSwitchResponse;
import com.ailicai.app.model.response.CheckTransferStatusResponse;
import com.ailicai.app.model.response.ProductSimpleInfoResponse;
import com.ailicai.app.model.response.ReserveSimpleInfoResponse;
import com.ailicai.app.model.response.TiyanbaoSimpleInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.AutomaticTenderPay;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.ReserveCancelInterface;
import com.ailicai.app.ui.buy.ReserveCancelPwdCheckDialog;
import com.ailicai.app.ui.dialog.BaseBuyFinanceDialog;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;
import com.ailicai.app.ui.view.TransferPayActivity;
import com.ailicai.app.ui.view.TransferRecordH5Activity;
import com.ailicai.app.ui.view.reserveredrecord.ReserveRecordListPresenter;
import com.ailicai.app.widget.CapitalProductProgressBar;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewDinFont;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CapitalListProductDetailActivity extends BaseBindActivity implements ReserveCancelInterface {

    public static final String TYPE = "product_type";
    public static final String PRODUCT_ID = "product_id";
    public static final String IS_RESERVE = "is_reserve";
    public static final String TIYANBAO_ID = "tiyanbao_id";//体验宝ID
    public static final String IS_TIYANBAO = "is_tiyanbao";
    public static final String HAS_TRANSFER_AMOUNT = "has_transfer_amount";
    public static final String TRANSFERING_AMOUNT = "transfering_amount";
    public static final String START_CAPIYAL = "start_capital";//页面finish时是否打开我的房产宝首页

    @Bind(R.id.product_detail_label_left)
    TextView mLabelLeft;
    @Bind(R.id.product_detail_value_left)
    TextViewDinFont mValueLeft;
    @Bind(R.id.product_detail_label_right)
    TextView mLabelRight;
    @Bind(R.id.product_detail_value_right)
    TextViewDinFont mValueRight;
    @Bind(R.id.apply_time_layout)
    View mApplyTimeLayout;
    @Bind(R.id.apply_time_title)
    TextView mApplyTimeTitle;
    @Bind(R.id.apply_time)
    TextView mApplyTime;//申购时间
    @Bind(R.id.investment_deadline_title)
    TextView mDeadlineTitle;
    @Bind(R.id.investment_deadline)
    TextView mDeadline;//投资期限
    @Bind(R.id.hold_capital_title)
    TextView mHoldCapitalTitle;
    @Bind(R.id.hold_capital)
    TextView mHoldCapital;
    @Bind(R.id.expect_year_rate)
    TextView mExpectYearRate;
    @Bind(R.id.expect_income)
    TextView mExpectIncome;
    @Bind(R.id.agreement_layout)
    View mAgreementLayout;//借款协议
    @Bind(R.id.expect_year_rate_title)
    TextView mExpectYearRateTitle;
    @Bind(R.id.expect_income_title)
    TextView mExpectIncomeTitle;
    @Bind(R.id.iw_title_back)
    IWTopTitleView mTitleView;
    @Bind(R.id.hold_capital_layout)
    View mHoldCapitalLayout;

    @Bind(R.id.compenstate_layout)
    View mCompenstateLayout;
    @Bind(R.id.compenstate_tips)
    TextView mCompenstateTips;
    @Bind(R.id.compenstate_total)
    TextView mCompenstateTotal;

    @Bind(R.id.expect_year_rate_layout)
    View mExpectYearRateLayout;
    @Bind(R.id.expect_income_layout)
    View mExpectIncomeLayout;
    @Bind(R.id.old_regular_detail_layout)
    View mOldRegularDetailLayout;

    @Bind(R.id.transfer_price_layout)
    View mTransferPriceLayout;
    @Bind(R.id.transfer_price)
    TextView mTransferPrice;
    @Bind(R.id.transfer_agreement_layout)
    View mTransferAgreementLayout;//债权转让协议

    @Bind(R.id.transfer_capital_layout)
    View mTransferCapitalLayout;
    @Bind(R.id.has_transfer_capital)
    TextView hasTransferCapital;
    @Bind(R.id.transfering_capital)
    TextView transferingCapital;
    @Bind(R.id.time_line)
    CapitalProductProgressBar mTimeLine;

    @Bind(R.id.reason_cannot_transfer_layout)
    View mReasonCannotTransferLayout;
    @Bind(R.id.reason_cannot_transfer_text)
    TextView mReasonCannotTransferText;
    @Bind(R.id.to_transfer_layout)
    View toTransferLayout;
    @Bind(R.id.common_tips)
    TextView commonTips;


    private boolean isReserve;
    private boolean isTiyanbao;
    private String productId = "";
    private long tiyanbaoId;
    private int type;
    private String protocolUrl;//借款协议，债权转让协议 h5 url
    private SpannableUtil mSpannableUtil;
    private String transferRecordUrl;

    private String productDetailUrl = "";//房产宝详情页h5url

    private ReserveSimpleInfoResponse reserveSimpleInfoResponse;
    private String bidOrderNo; //交易单号
    private boolean isPaid; //是否风险保障金赔付
    private CapitalListProductDetailPresenter mPresenter;
    private String hasTransferAmount = "";
    private String transferingAmount = "";

    @Override
    public int getLayout() {
        return R.layout.activity_capital_list_product_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mPresenter = new CapitalListProductDetailPresenter();
        mPresenter.setView(this);
        mSpannableUtil = new SpannableUtil(this);
        Bundle bundle = getIntent().getExtras();
        isReserve = bundle.getBoolean(IS_RESERVE, false);
        productId = bundle.getString(PRODUCT_ID, "");
        type = bundle.getInt(TYPE, 0);
        isTiyanbao = bundle.getBoolean(IS_TIYANBAO, false);
        tiyanbaoId = bundle.getLong(TIYANBAO_ID, 0);
        hasTransferAmount = bundle.getString(HAS_TRANSFER_AMOUNT, "");
        transferingAmount = bundle.getString(TRANSFERING_AMOUNT, "");


        if (isReserve) {
            mPresenter.getReserveProductDetailData(productId);
        } else if (isTiyanbao) {
            mPresenter.getTiyanbaoDetailData(tiyanbaoId);
        } else {
            mPresenter.getProductDetailData(productId);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (type == 2) {
            mPresenter.getProductDetailData(productId);
        }
    }

    /**
     * 处理体验宝产品状态
     */
    public void disposeTiyanbaoInfo(TiyanbaoSimpleInfoResponse response) {
        if (response.getStatus() != type) {
            productTypeChanged(true);
            type = response.getStatus();
        }


        mTitleView.setTitleText(response.getProductName());
        mApplyTimeLayout.setVisibility(View.GONE);
        mOldRegularDetailLayout.setVisibility(View.GONE);
        mAgreementLayout.setVisibility(View.GONE);
        mTimeLine.setVisibility(View.VISIBLE);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("申购日");
        titles.add("起息日");

        ArrayList<String> values = new ArrayList<>();
        values.add(response.getSubDateMMDD());
        values.add(response.getInterestDateMMDD());
        values.add(response.getExpireDateMMDD());

        if (type == 2) {
            mLabelLeft.setText("预计回款(元)");
            mLabelRight.setText("预计回款日");
            titles.add("预计到期日");

        } else {
            mLabelLeft.setText("回款(元)");
            mLabelRight.setText("回款时间");
            mExpectYearRateTitle.setText("实际年化");
            mExpectIncomeTitle.setText("实际收益");
            titles.add("到期日");
        }

        mValueLeft.setText(response.getBackAmount() + "");
        mValueRight.setText(response.getBackTime());
        mDeadline.setText(response.getHorizonStr());
        mHoldCapitalTitle.setText("体验金    ");
        mHoldCapital.setText(response.getBidAmount());
        mExpectYearRate.setText(response.getYearInterestRateStr());
        mExpectIncome.setText(response.getBackAmount() + "元");

        if (TextUtils.isEmpty(response.getBottomTips())) {
            commonTips.setVisibility(View.GONE);
        } else {
            commonTips.setVisibility(View.VISIBLE);
            commonTips.setText(response.getBottomTips());
        }
        mTimeLine.updateState(titles, values, 1, 1, response.getHorizon(), response.getPassDay());
    }

    /**
     * 处理预约的产品状态
     */
    public void disposeReserveProductInfo(ReserveSimpleInfoResponse response) {
        mHoldCapitalLayout.setVisibility(View.GONE);
        mExpectIncomeLayout.setVisibility(View.GONE);
        mOldRegularDetailLayout.setVisibility(View.GONE);
        mAgreementLayout.setVisibility(View.GONE);
        mCompenstateLayout.setVisibility(View.GONE);

        mTitleView.setTitleText(response.getProductName());
        mLabelRight.setText("预计申购日期");
        mValueRight.setText(response.getPreBuyTime());
        mLabelLeft.setText("预约金额(元)");
        mValueLeft.setText(response.getReserveAmt());
        mApplyTimeTitle.setText(getString(R.string.capital_order_date));
        mApplyTime.setText(response.getReserveTime());
        mDeadlineTitle.setText(getString(R.string.capital_order_deadline));
        mDeadline.setText(response.getHorizon());
        mExpectYearRate.setText(response.getYearInterestRateStr());

        reserveSimpleInfoResponse = response;
        if (response.isCancel()) {
            mTitleView.addRightText("取消预约", rightClick);
            mTitleView.getRightText().setAlpha(1);
        }
    }

    /**
     * 处理申购，持有，到期的产品状态
     */
    public void disposeProductInfo(final ProductSimpleInfoResponse response) {
        boolean change = false;
        if (response.getType() != type) {
            change = true;
            type = response.getType();
        }

        if (response.getHasTransferAmount().equals(hasTransferAmount) && response.getTransferingAmount().equals(transferingAmount)) {
        } else {
            change = true;
        }

        if (change) {
            productTypeChanged(true);
        }
        mTitleView.setTitleText(response.getProductName());
        productDetailUrl = response.getProductDetailUrl();
        protocolUrl = response.getProtocolUrl();
        transferRecordUrl = response.getTransferRecordsUrl();
        if (type == 1) {
            //申购
            mHoldCapitalLayout.setVisibility(View.GONE);
            mLabelLeft.setText("申购金额(元)");
            mValueLeft.setText(response.getBidAmount());
            mLabelRight.setText("申购进度");
            mValueRight.setText(response.getHasBuyPrecentStr());
            mApplyTime.setText(response.getBidTimeStr());
            mDeadline.setText(response.getHorizonStr());

        } else if (type == 2) {
            //持有
            mLabelRight.setText("预计回款日");
            mValueRight.setText(response.getBackTime());
            mLabelLeft.setText("预计回款(元)");
            mValueLeft.setText(response.getBackAmount());
            mApplyTimeLayout.setVisibility(View.GONE);
            mTimeLine.setVisibility(View.VISIBLE);
            boolean isTransfer = response.getIsTransfer() == 1;
            ArrayList<String> titles = new ArrayList<>();
            titles.add(isTransfer ? "起息日" : "申购日");
            titles.add(isTransfer ? "受让日" : "起息日");
            titles.add("预计到期日");
            ArrayList<String> values = new ArrayList<>();
            values.add(isTransfer ? response.getInterestDateMMDD() : response.getSubDateMMDD());
            values.add(isTransfer ? response.getBeTransferMMdd() : response.getInterestDateMMDD());
            values.add(response.getBackDateMMDD());

            if (isTransfer) {
                //是购买的转让的房产宝
                mTransferPriceLayout.setVisibility(View.VISIBLE);
                mTransferPrice.setText(response.getTransferAmount());
                mAgreementLayout.setVisibility(View.GONE);
                mTransferAgreementLayout.setVisibility(View.VISIBLE);
                commonTips.setVisibility(View.VISIBLE);
                commonTips.setText("房产宝暂不支持二次转让");
            } else {
                if (response.getCanBeTransfer() == 1) {//可转让
                    if(TextUtils.isEmpty(response.getTransferingAmount())){
                        toTransferLayout.setVisibility(View.VISIBLE);
                        toTransferLayout.setOnClickListener(onTransferClickListener);
                        bidOrderNo = response.getBidOrderNo();
                    }
                }else if (response.getCanBeTransfer() == 0){//不可转让
                    toTransferLayout.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(response.getTransferingAmount())){
                        mReasonCannotTransferLayout.setVisibility(View.GONE);
                        commonTips.setVisibility(View.GONE);
                    }else{
                        mReasonCannotTransferLayout.setVisibility(View.VISIBLE);
                        mReasonCannotTransferText.setText(response.getNotTransferTitle());
                        commonTips.setVisibility(View.VISIBLE);
                        commonTips.setText(response.getNotTransferReason());
                    }

                }
            }
            mDeadline.setText(response.getHorizonStr());
            mHoldCapital.setText(response.getBidAmount());
            mTimeLine.updateState(titles, values, response.getInterestTotal(), response.getFullDay(), response.getTotalDay(), response.getPassDay());
        } else {
            //到期
            mLabelRight.setText("回款时间");
            mValueRight.setText(response.getBackTime());
            mLabelLeft.setText("回款(元)");
            mValueLeft.setText(response.getBackAmount());
            mApplyTimeLayout.setVisibility(View.GONE);
            mTimeLine.setVisibility(View.VISIBLE);
            boolean isTransfer = response.getIsTransfer() == 1;
            ArrayList<String> titles = new ArrayList<>();
            titles.add(isTransfer ? "起息日" : "申购日");
            titles.add(isTransfer ? "受让日" : "起息日");
            titles.add("到期日");
            ArrayList<String> values = new ArrayList<>();
            values.add(isTransfer ? response.getInterestDateMMDD() : response.getSubDateMMDD());
            values.add(isTransfer ? response.getBeTransferMMdd() : response.getInterestDateMMDD());
            values.add(response.getBackDateMMDD());

            if (response.getIsPaid() == 1) {
                isPaid = true;
                mExpectYearRateLayout.setVisibility(View.GONE);
                mExpectIncomeLayout.setVisibility(View.GONE);
                mCompenstateLayout.setVisibility(View.VISIBLE);
                mCompenstateTips.setText(response.getPaidMemo());
                mCompenstateTotal.setText(response.getPaidAmount());
                if (isTransfer) {
                    mTransferAgreementLayout.setVisibility(View.VISIBLE);
                    mAgreementLayout.setVisibility(View.GONE);
                    mTransferPriceLayout.setVisibility(View.VISIBLE);
                    mTransferPrice.setText(response.getTransferAmount());
                } else {
                    mTransferAgreementLayout.setVisibility(View.GONE);
                    mAgreementLayout.setVisibility(View.VISIBLE);
                }
            } else {
                mCompenstateLayout.setVisibility(View.GONE);
                mAgreementLayout.setVisibility(View.GONE);
            }


            mExpectYearRateTitle.setText("实际年化");
            mExpectIncomeTitle.setText("实际收益");
            mDeadline.setText(response.getHorizonStr());
            mHoldCapital.setText(response.getBidAmount());
            mTimeLine.updateState(titles, values, response.getInterestTotal(), response.getFullDay(), response.getTotalDay(), response.getPassDay());
        }

        //预计收益
        String yearInterestRateStr = TextUtils.isEmpty(response.getYearInterestRateAddStr()) ? "": response.getYearInterestRateAddStr();
        String addRateStr = TextUtils.isEmpty(response.getAddRateStr()) ? "": response.getAddRateStr();
        String yearInterestRateBoostStr = TextUtils.isEmpty(response.getYearInterestRateBoostStr()) ? "": response.getYearInterestRateBoostStr();

        SpannableStringBuilder predictProfit = mSpannableUtil.getSpannableString(response.getYearInterestRateStr()+" ",
                yearInterestRateStr + addRateStr + yearInterestRateBoostStr+" ",
                R.style.text_14_757575, R.style.text_14_e84a01);

        if (response.isHelpRaiseFlag()){

            Drawable d = getResources().getDrawable(R.drawable.btn_jiaxi);
            d.setBounds(0, 0, UIUtils.dipToPx(this,50), UIUtils.dipToPx(this,18));
            predictProfit.setSpan(new ImageSpan(d), predictProfit.length()-1, predictProfit.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            predictProfit.setSpan(new CenteredImageSpan(CapitalListProductDetailActivity.this, ImageSpan.ALIGN_BASELINE), predictProfit.length()-1, predictProfit.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            predictProfit.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    handleINeedRaiseInterest(response);
                }
            }, predictProfit.length() - 1, predictProfit.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mExpectYearRate.setMovementMethod(LinkMovementMethod.getInstance());
        }
        mExpectYearRate.setText(predictProfit);
        //expanse the response area of clicking
        mExpectYearRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckDoubleClick.isFastDoubleClick()) return;
                handleINeedRaiseInterest(response);
            }
        });

        String profitAddStr = TextUtils.isEmpty(response.getProfitAddStr()) ? "": response.getProfitAddStr();
        String profitBoostStr = TextUtils.isEmpty(response.getProfitBoostStr()) ? "": response.getProfitBoostStr();
        mExpectIncome.setText(getSpannableString(response.getProfitStr(), profitAddStr + profitBoostStr ));

        if (!TextUtils.isEmpty(response.getHasTransferAmount()) || !TextUtils.isEmpty(response.getTransferingAmount())) {
            mTransferCapitalLayout.setVisibility(View.VISIBLE);
            hasTransferCapital.setText(response.getHasTransferAmount());
            transferingCapital.setText(response.getTransferingAmount());
        }
    }

    private void handleINeedRaiseInterest(ProductSimpleInfoResponse response){
        if (!TextUtils.isEmpty(response.getHelpRaiseUrl())){
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.URL, response.getHelpRaiseUrl());
            dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
            dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
            MyIntent.startActivity(CapitalListProductDetailActivity.this, WebViewActivity.class, dataMap);
        }
    }

    public void productTypeChanged(boolean changed) {
        if (changed) {
            CapitalProductTypeChangeEvent event = new CapitalProductTypeChangeEvent();
            EventBus.getDefault().post(event);
        }


    }

    @Override
    public void reloadData() {
        if (isReserve) {
            mPresenter.getReserveProductDetailData(productId);
        } else if (isTiyanbao) {
            mPresenter.getTiyanbaoDetailData(tiyanbaoId);
        } else {
            mPresenter.getProductDetailData(productId);
        }
    }


    @OnClick(R.id.agreement_layout)
    void toAgreementDetail() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!TextUtils.isEmpty(protocolUrl)) {
            if (type == 2) {
                EventLog.upEventLog("801", "jkxy");
            } else if (type == 3) {
                if (isPaid) {
                    EventLog.upEventLog("802", "jkxy", "yhfxjpf");
                }
            }

            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.TITLE, "借款协议");
            dataMap.put(WebViewActivity.URL, protocolUrl);
            dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
            MyIntent.startActivity(this, WebViewActivity.class, dataMap);
        }
    }

    @OnClick(R.id.transfer_agreement_layout)
    void toTransferAgreementDetail() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!TextUtils.isEmpty(protocolUrl)) {
            if (type == 2) {
                EventLog.upEventLog("801", "zjzrxy");
            } else if (type == 3) {
                if (isPaid) {
                    EventLog.upEventLog("802", "zqzrxy", "yhfxjpf");
                }
            }

            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.TITLE, "债权转让协议");
            dataMap.put(WebViewActivity.URL, protocolUrl);
            dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
            MyIntent.startActivity(this, WebViewActivity.class, dataMap);
        }

    }

    @OnClick(R.id.transfer_capital_layout)
    void goTransferRecord() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!TextUtils.isEmpty(transferRecordUrl)) {
            if (type == 2) {
                EventLog.upEventLog("801", "zrrk");
            } else if (type == 3) {
                if (isPaid) {
                    EventLog.upEventLog("802", "zrrk", "yhfxjpf");
                } else {
                    EventLog.upEventLog("802", "zrrk", "whfxjpf");
                }
            }


            Intent intent = new Intent(this, TransferRecordH5Activity.class);
            intent.putExtra(TransferRecordH5Activity.EXTRA_URL, transferRecordUrl);
            startActivity(intent);
        }
    }

    @OnClick(R.id.old_regular_detail_layout)
    void toOldRegularDetail() {
        if (TextUtils.isEmpty(productDetailUrl)) return;
        if (type == 2) {
            EventLog.upEventLog("801", "fcbxq");
        } else if (type == 3) {
            if (isPaid) {
                EventLog.upEventLog("802", "yfcbxq", "yhfxjpf");
            } else {
                EventLog.upEventLog("802", "yfcbxq", "whfxjpf");
            }

        }

        Intent intent = new Intent(this, RegularFinanceDetailH5Activity.class);
        intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, productDetailUrl);
        startActivity(intent);
    }

    private SpannableStringBuilder getSpannableString(String one, String two) {
        return mSpannableUtil.getSpannableString(one, two, R.style.text_14_757575, R.style.text_14_e84a01);

    }

    private View.OnClickListener rightClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showCancelDialog();
        }
    };

    ReserveCancelPwdCheckDialog pwdDialog;

    public void showCancelDialog() {
        pwdDialog = new ReserveCancelPwdCheckDialog(CapitalListProductDetailActivity.this, new IwPwdPayResultListener() {
            @Override
            public void onPayPwdTryAgain() {
                pwdDialog.show();
            }

            @Override
            public void onPayComplete(Object object) {
                SystemUtil.showOrHideSoftInput(CapitalListProductDetailActivity.this);
                ReserveRecordListPresenter.requestForBookingCancel(CapitalListProductDetailActivity.this);
            }

            @Override
            public void onPayStateDelay(String msgInfo, Object object) {
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, Object object) {
            }
        }, new ReserveCancelPwdCheckDialog.ShowInfo() {
            @Override
            public double showAmount() {
                return Double.parseDouble(reserveSimpleInfoResponse.getReserveAmtValue());
            }

            @Override
            public String showMoneyOutStr() {
                return "取消" + reserveSimpleInfoResponse.getProductName();
            }
        });
        pwdDialog.show();
    }

    @Override
    public BaseBindActivity getBaseActivity() {
        return this;
    }

    @Override
    public ReserveCancelRequest getBookingCancelRequest() {
        ReserveCancelRequest request = new ReserveCancelRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setPaypwd(pwdDialog.getPayPwd());
        request.setBidOrderNo(reserveSimpleInfoResponse.getBidOrderNo());
        return request;
    }

    @Override
    public void cancelSuccess() {
        DialogBuilder.customSimpleDialog("未完成预约金额" + reserveSimpleInfoResponse.getReserveAmt() + "元，资金在钱包内解冻为可用余额。", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new CapitalApplyRefreshEvent());
                finish();
            }
        }, getString(R.string.ok));
    }

    @Override
    public void cancelFailed(String failInfo) {
        ToastUtil.showInCenter(failInfo);
    }


    private View.OnClickListener onTransferClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventLog.upEventLog("801", "zr");
            mTitleView.getRightText().setEnabled(false);

            if(UserInfo.getInstance().isAutoBid()){
                showAutoInvestDialog();
            }else{
                checkTransferStatus();
            }


        }
    };
    private boolean showLoading;

    private void checkTransferStatus() {
        if (showLoading) {
            return;
        }
        showLoading = true;
        final CheckTransferStatusRequest request = new CheckTransferStatusRequest();
        request.setOrderNo(bidOrderNo);
        showLoadTranstView();
        ServiceSender.exec(this, request, new IwjwRespListener<CheckTransferStatusResponse>() {
            @Override
            public void onJsonSuccess(CheckTransferStatusResponse response) {
                showLoading = false;
                showContentView();
                if (response.getErrorCode() == 0) {
                    int bizCode = response.getBizCode();
                    if (bizCode == 0) {
                        goTransferPay(response.getOrderNo());
                    } else if (bizCode == -1) {
                        showCannotTransferDialog(response.getMessage());
                    } else if (bizCode == -2) {
                        showTransferDialog(response.getMessage(), response.getOrderNo());
                    }

                } else {
                    onFailInfo(response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showLoading = false;
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mTitleView.getRightText().setEnabled(true);
            }
        });

    }
    private void showAutoInvestDialog() {

        DialogBuilder.showSimpleDialog(this, getString(R.string.friendly_reminder), getString(R.string.dialog_auto_invest),
                getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showPwdDialogForOpen(false,0,"0");
                    }},
                getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkTransferStatus();
                    }
                }
        );
    }

    /***
     * 关闭或者点击地步确认按钮需要交易密码验证
     */
    public void showPwdDialogForOpen(boolean forOpen,int strategyType,String reserveMoney){

        final AutomaticTenderPay.AutomaticTenderInfo info = new AutomaticTenderPay.AutomaticTenderInfo();
        info.forOpen = forOpen;
        info.strategyType = strategyType;
        info.reserveMoney = reserveMoney;
        AutomaticTenderPay mPay = new AutomaticTenderPay(CapitalListProductDetailActivity.this, info,new IwPwdPayResultListener<AutoBidSwitchResponse>() {
            @Override
            public void onPayPwdTryAgain() {
                showPwdDialogForOpen(info.forOpen, info.strategyType, info.reserveMoney);
            }

            @Override
            public void onPayComplete(AutoBidSwitchResponse object) {
                UserInfo.getInstance().setAutoBid(false);//因为本地的页面只与关闭操作有关
                checkTransferStatus();
            }

            @Override
            public void onPayStateDelay(String msgInfo, AutoBidSwitchResponse object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, AutoBidSwitchResponse object) {

                if(!TextUtils.isEmpty(msgInfo)){
                    showMyToast(msgInfo);
                }
            }
        });
        mPay.setDialogDismissListener(new BaseBuyFinanceDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        mPay.pay();
    }


    private void showCannotTransferDialog(String msg) {
        DialogBuilder.getAlertDialog(this)
                .setTitle(getString(R.string.can_not_transfer))
                .setMessage(msg)
                .setPositiveButton(getString(R.string.i_know), null)
                .setCancelable(false)
                .show();

    }

    private void showTransferDialog(String msg, final String orderNo) {

        DialogBuilder.showSimpleDialog(this, getString(R.string.friendly_reminder), msg, getString(R.string.continue_transfer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goTransferPay(orderNo);

            }
        }, getString(R.string.stop_transfer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void goTransferPay(String orderNumber) {
        Intent intent = new Intent(this, TransferPayActivity.class);
        intent.putExtra(TransferPayActivity.INTENT_EXTRA_ORDER_ID, orderNumber);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mPresenter.getProductDetailData(productId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra(START_CAPIYAL, false)) {
            //跳转到我的房产宝首页
            Intent intent = new Intent(this, CapitalActivity.class);
            //1-申购;2-持有;3-到期
            if (type == 2) {
                intent.putExtra(CapitalActivity.TAB, CapitalActivity.HOLD);
            } else if (type == 3) {
                intent.putExtra(CapitalActivity.TAB, CapitalActivity.EXPIRED);
            }
            startActivity(intent);
        }
    }
}
