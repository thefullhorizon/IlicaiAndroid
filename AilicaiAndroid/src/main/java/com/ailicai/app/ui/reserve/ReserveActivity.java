package com.ailicai.app.ui.reserve;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.eventbus.ReserveAvtivityFinishEvent;
import com.ailicai.app.model.bean.CustomSingleChoiceBean;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.model.response.ReserveDetailResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.view.reserveredrecord.ReserveRecordListActivity;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 预约购买页面
 * Created by David on 16/3/7.
 */
public class ReserveActivity extends BaseBindActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CounterFinishListener {

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.reserve_title)
    IWTopTitleView mTopView;

//    @Bind(R.id.progressBar)
//    ProgressBar progressBar;
//    @Bind(R.id.reserve_term_value)
//    TextView mTermValue;

//    @Bind(R.id.year_rate)
//    TextView mYearRate;
//    @Bind(R.id.tvBookSumHit)
//    TextView tvBookSumHit;
//    @Bind(R.id.reserve_horizon)
//    TextView mHorizon;
    @Bind(R.id.reserve_bid_amount)
TextView mBidAmount;

//    @Bind(R.id.reserve_deal_date)
//    TextView mDealDate;
    @Bind(R.id.reserve_confirmed)
TextView mConfirm;
    @Bind(R.id.reserve_count_down)
    TimeZHTextCounter mCountDown;

    @Bind(R.id.rl_reserve_command)
    RelativeLayout rlReserveCommand;

    @Bind(R.id.rl_root_reserve_pwd)
    RelativeLayout rlRootReservePwd;
    @Bind(R.id.ll_reserve_pwd_content)
    LinearLayout llReservePwdContent;
    @Bind(R.id.view_reserve_pwd_bg)
    View viewReservePwdBg;
    @Bind(R.id.et_input)
    EditText etInput;//口令输入
    @Bind(R.id.tv_clear)
    TextViewTF tvClear;
    @Bind(R.id.btn_use)
    Button btnUse;
    @Bind(R.id.tv_error)
    TextView tvError;
    @Bind(R.id.tvProductCount)
    TextView tvProductCount;

    @Bind(R.id.day_tag_layout)
    DayTagLayout dayTagLayout;

    @Bind(R.id.year_rate_container)
    YearRateLayoutContainer yearRateLayoutContainer;

    public ReservePresenter presenter;
    private ReserveDetailResponse reserveResponse;
    private String reservePwd;
    //是否正在验证口令
    private boolean isCheckPwdIng = false;
    private boolean isRefresh = true;
    private boolean isChangeProductNum = false;

    private int horizonNum = 90;

    public int getHorizonNum() {
        return horizonNum;
    }

    public void setHorizonNum(int horizonNum) {
        this.horizonNum = horizonNum;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_finance_reserve;
    }


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.main_red_color);
        presenter = new ReservePresenter(this);
        mTopView.addRightText("我的预约", recordEvent);
        mTopView.getRightText().setAlpha(1);
        etInput.addTextChangedListener(reservePwdTextWatcher);
        isChangeProductNum = true;
        EventBus.getDefault().register(this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        presenter.sendReserveService();
        isRefresh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        reservePwd = "";
        presenter.sendReserveService();
    }

    @Override
    public void onPause() {
        super.onPause();
        SystemUtil.hideKeyboard(etInput);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeActivity();
        EventBus.getDefault().unregister(this);
    }

    public void onRefreshFinished() {
        mSwipeLayout.setRefreshing(false);
    }

    /*********************
     * page association
     ******************/
    public void deployPageElements(ReserveDetailResponse reserve) {

        this.reserveResponse = reserve;
        Product product = reserve.getProduct();
        if (isRefresh) {
            //下拉刷新之后，重置口令
            reservePwd = "";
            selectMaxOption();
        } else {
            initReserveTermView(getHorizonNum());
            yearRateLayoutContainer.init(product.getPreBuyTimeStr(),getYearRatesByTerm(getHorizonNum()));
        }
        isRefresh = false;
        mTopView.setTitleText(product.getProductName());

        tvProductCount.setText(reserve.getReservedNum() + "");
        //刷新底部按钮状态
        initReserveStatus(getYearRatesByTerm(getHorizonNum()).size() > 0);
    }

    /**
     * 默认选中最大的
     */
    private void selectMaxOption() {
        int maxDay = 90;
        if (reserveResponse.getMaxDays() > 90) {
            maxDay = reserveResponse.getMaxDays();
        }
        setHorizonNum(maxDay);
        initReserveTermView(maxDay);
        yearRateLayoutContainer.init(reserveResponse.getProduct().getPreBuyTimeStr(),getYearRatesByTerm(maxDay));
    }

    /**
     * 初始化预约的状态
     */
    private void initReserveStatus(boolean confirmBtnEnable) {
        mCountDown.setVisibility(View.GONE);
        mBidAmount.setVisibility(View.VISIBLE);
        mConfirm.setVisibility(View.VISIBLE);
        //1-即将开始预约 2-预约中 3-额度已满 4-预约已过期
        switch (reserveResponse.getStatus()) {
            case 1:
                //即将开始预约,不可使用口令
                mConfirm.setVisibility(View.GONE);
                mCountDown.setVisibility(View.VISIBLE);
                mCountDown.setTimeMills(reserveResponse.getBeginReserveTime());
                mCountDown.setCounterFinishListener(this);
                rlReserveCommand.setVisibility(View.GONE);
                mBidAmount.setText("额度 " + reserveResponse.getProduct().getBiddableAmountStr() + " 元，预约金额须为1万的整数倍");
                break;
            case 2:
                //可接受预约,可使用口令
                mConfirm.setEnabled(true);
                mConfirm.setText("立即预约");
                mConfirm.setTextColor(Color.parseColor("#ffffff"));
                mConfirm.setBackgroundColor(Color.parseColor("#2962ff"));
                rlReserveCommand.setVisibility(View.VISIBLE);
                mBidAmount.setText("剩余额度 " + reserveResponse.getProduct().getBiddableAmountStr() + " 元，预约金额须为1万的整数倍");
                break;
            case 3:
                //预约额度已满,可使用口令
                mConfirm.setEnabled(false);
                mConfirm.setText("预约额度已满");
                mConfirm.setTextColor(getResources().getColor(R.color.pinkish_grey));
                mConfirm.setBackgroundColor(Color.parseColor("#302962ff"));
                rlReserveCommand.setVisibility(View.VISIBLE);
                mBidAmount.setVisibility(View.GONE);
                break;
            case 4:
                //预约结束,不可使用口令
                mConfirm.setEnabled(false);
                mConfirm.setText("已结束");
                mConfirm.setTextColor(getResources().getColor(R.color.pinkish_grey));
                mConfirm.setBackgroundColor(Color.parseColor("#21000000"));
                rlReserveCommand.setVisibility(View.GONE);
                mBidAmount.setVisibility(View.GONE);
                break;
        }

        if (!confirmBtnEnable && reserveResponse.getStatus() !=4) {
            mConfirm.setEnabled(false);
            mConfirm.setBackgroundColor(Color.parseColor("#302962ff"));
        }
    }

    /**
     * 倒计时结束
     */
    @Override
    public void onFinish() {
        presenter.sendReserveService();
    }

    /**
     * 去预约记录
     */
    private View.OnClickListener recordEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ReserveActivity.this, ReserveRecordListActivity.class);
            startActivity(intent);
        }
    };

    /**
     * 选择理财期限
     */
    private void initReserveTermView(int selectTerm) {

        dayTagLayout.init(getListData(),selectTerm);

        dayTagLayout.setOnItemCheckedListener(new TagLayout.OnItemCheckedListener<CustomSingleChoiceBean>() {
            @Override
            public void onChecked(View itemView, CustomSingleChoiceBean bean) {
                setHorizonNum(bean.getId());
                yearRateLayoutContainer.init(reserveResponse.getProduct().getPreBuyTimeStr(),getYearRatesByTerm(bean.getId()));
                initReserveStatus(getYearRatesByTerm(bean.getId()).size() > 0);
            }
        });
    }

    private List<CustomSingleChoiceBean> getListData() {
        List<CustomSingleChoiceBean> listData = new ArrayList<>();
        CustomSingleChoiceBean bean = new CustomSingleChoiceBean();
        bean.setId(60);
        bean.setName("60天内");
        listData.add(bean);
        CustomSingleChoiceBean bean2 = new CustomSingleChoiceBean();
        bean2.setId(90);
        bean2.setName("90天内");
        listData.add(bean2);

        if(reserveResponse.getMaxDays() > 90) {
            CustomSingleChoiceBean bean3 = new CustomSingleChoiceBean();
            bean3.setId(reserveResponse.getMaxDays());
            bean3.setName(reserveResponse.getMaxDays()+"天内");
            listData.add(bean3);
        }

        return listData;
    }

    private List<ReserveDetailResponse.ProductRate> getYearRatesByTerm(int term) {

        ArrayList<ReserveDetailResponse.ProductRate> lessThan60 = new ArrayList<>();
        ArrayList<ReserveDetailResponse.ProductRate> lessThan90 = new ArrayList<>();
        ArrayList<ReserveDetailResponse.ProductRate> lessThanMax = new ArrayList<>();

        if (null == reserveResponse.getRateList()) {
            return  lessThan60;
        }

        for (ReserveDetailResponse.ProductRate productRate:reserveResponse.getRateList()) {
            if (productRate.getTerm() <=60) {
                lessThan60.add(productRate);
                lessThan90.add(productRate);
                lessThanMax.add(productRate);
            } else if(productRate.getTerm() <=90) {
                lessThan90.add(productRate);
                lessThanMax.add(productRate);
            } else {
                lessThanMax.add(productRate);
            }
        }

        if (term <= 60) {
            return lessThan60;
        } else if (term <=90) {
            return lessThan90;
        }
        return lessThanMax;
    }

    /**
     * 去预约购买产品列表
     * 去除此处的入口
     */
//    @OnClick(R.id.year_rate_container)
    public void onYearRateContainerClick() {
        Intent intent = new Intent(this, ReserveListActivity.class);
        intent.putExtra("productId", reserveResponse.getProduct().getProductId());
        intent.putExtra("horizon", getHorizonNum());
        startActivity(intent);
    }

    /**
     * 预约记录
     *
     */
    @OnClick(R.id.llProduct)
    public void onProductClick() {
//        ProductInvestRecordActivity.startActivity(ReserveActivity.this,reserveResponse.getProduct().getProductId());
    }

    /**
     * 去预约说明页
     */
    @OnClick(R.id.reserve_detail)
    void toReserveDetail() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        String url = reserveResponse.getMoreDetailUrl();
        if (!TextUtils.isEmpty(url)) {
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.TITLE, "更多详情");
            dataMap.put(WebViewActivity.URL, url);
            dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "true");
            MyIntent.startActivity(this, WebViewActivity.class, dataMap);
        }
    }

    /**
     * 确认预约
     */
    @OnClick(R.id.reserve_confirmed)
    public void onConfirmClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!NoSetSafeCardHint.isShowHintDialog(this)) {
            Intent intent = new Intent(ReserveActivity.this, ReserveCashierDeskActivity.class);
            intent.putExtra("ReserveDetailResponse", reserveResponse);
            intent.putExtra("term", getHorizonNum());
            intent.putExtra("reservePwd", reservePwd);
            intent.putExtra("yearRateZone",getYearRateZone());
            startActivity(intent);
        }
    }

    /***
     * 点击底部确认按钮，获取当前用户所选利率的区间，形如：7.2%~7.9%
     * @return
     */
    private String getYearRateZone(){
        String zone = "";
        List<ReserveDetailResponse.ProductRate> productRates = yearRateLayoutContainer.getProductRates();
        if(productRates == null || productRates.size() == 0){
            return zone;
        }
        int size = productRates.size();
        String firstRate = productRates.get(0).getRate();
        if(size == 1){
            zone = firstRate;
        }else{
            if (firstRate.equals(productRates.get(productRates.size() - 1).getRate())){
                zone = firstRate;
            }else{
                firstRate = firstRate.replace("%","~");
                zone = firstRate + productRates.get(productRates.size() - 1).getRate();
            }

        }
        return zone;
    }

    //------------------------------------------------------------------------------------------口令相关-------------------------------------------------------------------------------------------

    /**
     * 点击口令预约(在开始预约和预约额度已满的状态下，且时间点为14点-24点之间才展示改入口)
     */
    @OnClick(R.id.rl_reserve_command)
    public void reserveCommandClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!NoSetSafeCardHint.isShowHintDialog(this)) {
            showReservePwd();
            initReservePwdStatus();
        }
    }

    private TextWatcher reservePwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            tvError.setVisibility(View.INVISIBLE);
            if (TextUtils.isEmpty(text)) {
                //输入被清空
                btnUse.setEnabled(false);
                tvClear.setVisibility(View.GONE);
            } else {
                tvClear.setVisibility(View.VISIBLE);
                if (text.length() >= 6) {
                    //大于等于6个字符
                    btnUse.setEnabled(true);
                } else {
                    //小于6个字符
                    btnUse.setEnabled(false);
                }
            }
        }
    };

    /**
     * 点击空白区域
     */
    @OnClick(R.id.view_reserve_pwd_bg)
    public void dismissReservePwd() {
        if (isCheckPwdIng) {
            //正在验证中
            return;
        }
        hideReservePwd();
    }

    /**
     * 口令点击清空按钮
     */
    @OnClick(R.id.tv_clear)
    public void clearTextClick() {
        etInput.setText("");
    }

    /**
     * 口令点击使用
     */
    @OnClick(R.id.btn_use)
    public void useClick() {
        String checkCommand = etInput.getText().toString();
        if (TextUtils.isEmpty(checkCommand)) {
            showMyToast("请输入口令");
            return;
        }
        presenter.checkReserveCommand(checkCommand);
        checkCommandIng();
    }

    /**
     * 显示口令弹层
     */
    private void showReservePwd() {
        rlRootReservePwd.setVisibility(View.VISIBLE);
        etInput.requestFocus();
        SystemUtil.showKeyboard(etInput);
        float distance = DeviceUtil.getPixelFromDip(this, 172);
        ObjectAnimator inAnim = ObjectAnimator.ofFloat(llReservePwdContent, "translationY", -distance, 0f);
        inAnim.setDuration(360);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(rlRootReservePwd, "alpha", 0f, 1f);
        alphaAnim.setDuration(360);
        AnimatorSet set = new AnimatorSet();
        set.play(inAnim).with(alphaAnim);
        set.start();
    }

    /**
     * 隐藏口令弹层
     */
    private void hideReservePwd() {
        etInput.clearFocus();
        SystemUtil.hideKeyboard(etInput);
        float distance = DeviceUtil.getPixelFromDip(this, 172);
        ObjectAnimator outAnim = ObjectAnimator.ofFloat(llReservePwdContent, "translationY", 0f, -distance);
        outAnim.setDuration(360);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(rlRootReservePwd, "alpha", 1f, 0f);
        alphaAnim.setDuration(360);
        outAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rlRootReservePwd.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(outAnim).with(alphaAnim);
        set.start();
    }

    /**
     * 初始化口令弹层的View状态
     */
    private void initReservePwdStatus() {
        isCheckPwdIng = false;
        etInput.getText().clear();
        etInput.setEnabled(true);
        tvError.setText("");
        tvError.setVisibility(View.INVISIBLE);
        btnUse.setEnabled(false);
        btnUse.setText("使用");
        tvClear.setEnabled(true);
        tvClear.setVisibility(View.GONE);
    }

    /**
     * 校验中
     */
    public void checkCommandIng() {
        isCheckPwdIng = true;
        etInput.setEnabled(false);
        btnUse.setEnabled(false);
        btnUse.setText("验证中");
        tvClear.setEnabled(false);
        tvError.setText("");
        tvError.setVisibility(View.INVISIBLE);
    }

    /**
     * 校验口令失败
     *
     * @param errorInfo
     */
    public void checkCommandError(String errorInfo) {
        isCheckPwdIng = false;
        tvError.setText(errorInfo);
        tvError.setVisibility(View.VISIBLE);
        btnUse.setEnabled(true);
        btnUse.setText("使用");
        etInput.setEnabled(true);
        tvClear.setEnabled(true);
        this.reservePwd = "";
    }

    /**
     * 校验口令成功
     */
    public void checkCommandSuccess(String reservePwd) {
        isCheckPwdIng = false;
        this.reservePwd = reservePwd;
        etInput.setEnabled(true);
        btnUse.setEnabled(true);
        btnUse.setText("使用");
        tvClear.setEnabled(true);
        hideReservePwd();
        //改变成可以预约的状态
//        reserveResponse.setStatus(2);
//        initReserveStatus();
        onConfirmClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleReserveAvtivityFinishEvent(ReserveAvtivityFinishEvent event) {
        finish();
    }
}
