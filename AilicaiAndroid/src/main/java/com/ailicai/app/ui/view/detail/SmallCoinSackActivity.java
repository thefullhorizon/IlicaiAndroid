package com.ailicai.app.ui.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.model.response.ProductSimpleInfoResponse;
import com.ailicai.app.ui.asset.treasure.ProductCategory;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;
import com.ailicai.app.widget.CapitalProductProgressBar;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewDinFont;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by nanshan on 2017/5/4.
 */

public class SmallCoinSackActivity extends BaseBindActivity {

    @Bind(R.id.iw_title_back)
    IWTopTitleView mTitleBack;

    @Bind(R.id.purchase_progress_bar)
    CapitalProductProgressBar mPurchaseProgressBar;

    @Bind(R.id.coin_sack_title_left)
    TextView mCoinSackTitleLeft;
    @Bind(R.id.coin_sack_value_left)
    TextViewDinFont mCoinSackValueLeft;
    @Bind(R.id.coin_sack_title_right)
    TextView mCoinSackTitleRight;
    @Bind(R.id.coin_sack_value_right)
    TextViewDinFont mCoinSackValueRight;

    @Bind(R.id.apply_time_layout)
    View mApplyTimeLaout;
    @Bind(R.id.apply_time_title)
    TextView mApplyTimeTitle;
    @Bind(R.id.apply_time_value)
    TextView mApplyTimeValue;//申购时间
    @Bind(R.id.investment_deadline_layout)
    View mInvestmentDeadlineLayout;
    @Bind(R.id.investment_deadline_title)
    TextView mInvestmentDeadlineTitle;
    @Bind(R.id.investment_deadline_value)
    TextView mInvestmentDeadlineValue;//投资期限
    @Bind(R.id.hold_capital_layout)
    View mHoldCapitalLayout;
    @Bind(R.id.hold_capital_title)
    TextView mHoldCapitalTitle;
    @Bind(R.id.hold_capital_value)
    TextView mHoldCapitalValue;//持有金额
    @Bind(R.id.predict_year_rate_layout)
    View mPredictYearRateLayout;
    @Bind(R.id.predict_year_rate_title)
    TextView mPredictYearRateTitle;
    @Bind(R.id.predict_year_rate_value)
    TextView mPredictYearRateTitleValue;//预计年化（包含两个值）
    @Bind(R.id.add_rate_explain)
    TextView mAddRateExplain;

    @Bind(R.id.predict_income_layout)
    View mPredictIncomeLayout;
    @Bind(R.id.predict_income_title)
    TextView mPredictIncomeTitle;
    @Bind(R.id.predict_income_value)
    TextView mPredictIncomeTitleValue;//预计收益

    @Bind(R.id.previous_coin_sack_detail_layout)
    View mPreviousCoinSackDetailLayout;
    @Bind(R.id.coin_sack_product_list_layout)
    View mCoinSackProductlistLayout;
    @Bind(R.id.product_coin_value)
    TextView mCoinSackProductlistValue;

    @Bind(R.id.tv_coin_feature)
    TextView tvCoinFeature;


    private SmallCoinSackPresenter coinSackPresenter;
    private String productDetailUrl = "";//小钱袋详情页h5url
    private String smallCoinSackName = "";
    private String smallCoinSackStatus = "";
    private String bidOrderNo = "";

    public static final String PRODUCT_ID = "product_id";
    public static final String CATEGORY = "product_category";

    private String productId = "";
    private String coinSackId = "";
    private int type;//根据类型生成不同的页面  申请 持有 回购


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        coinSackPresenter = new SmallCoinSackPresenter();
        coinSackPresenter.setView(this);

        mSpannableUtil = new SpannableUtil(this);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt(CATEGORY, ProductCategory.Apply.getType());
        productId = bundle.getString(PRODUCT_ID, "");

        initialViewByProductCategory();
        coinSackPresenter.getProductDetailData(productId);
    }



    private void initialViewByProductCategory() {

        switch (type){

            case 1 ://Apply
                mCoinSackTitleLeft.setText("申购金额（元）");
                mCoinSackTitleRight.setText("申购进度");

                mApplyTimeLaout.setVisibility(View.VISIBLE);
                mHoldCapitalLayout.setVisibility(View.GONE);
                tvCoinFeature.setVisibility(View.GONE);
                break;

            case 2 ://Hold
                mCoinSackTitleLeft.setText("预计回款（元）");
                mCoinSackTitleRight.setText("预计回款日");

                mPurchaseProgressBar.setVisibility(View.VISIBLE);
                mApplyTimeLaout.setVisibility(View.GONE);
                break;

            case 3 ://Expired
                mCoinSackTitleLeft.setText("回款（元）");
                mCoinSackTitleRight.setText("回款日期");

                mPurchaseProgressBar.setVisibility(View.VISIBLE);
                mApplyTimeLaout.setVisibility(View.GONE);
                mPredictYearRateTitle.setText("实际年化");
                mPredictIncomeTitle.setText("实际收益");
                tvCoinFeature.setVisibility(View.GONE);
                break;

            default:

                break;
        }

    }


    @Override
    public int getLayout() {
        return R.layout.activity_coin_sack;
    }


    @OnClick(R.id.previous_coin_sack_detail_layout)
    void toPreviousCoinSackDetailLayout() {

        //原小钱袋详情 参考CapitalListProductDetailActivity中的跳转原房产报详情（toOldRegularDetail）
        if (TextUtils.isEmpty(productDetailUrl)) return;
        Intent intent = new Intent(this, RegularFinanceDetailH5Activity.class);
        intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, productDetailUrl);
        startActivity(intent);

    }

    @OnClick(R.id.coin_sack_product_list_layout)
    void toCoinSackProductlistLayout() {

        Intent intent = new Intent(this, SmallCoinListActivity.class);
        intent.putExtra(SmallCoinListActivity.PRODUCT_ID, coinSackId);
        intent.putExtra(SmallCoinListActivity.PRODUCT_NAME, smallCoinSackName);
        intent.putExtra(SmallCoinListActivity.PRODUCT_CATEGORY, type);
        intent.putExtra(SmallCoinListActivity.PRODUCT_STATE, smallCoinSackStatus);
        intent.putExtra(SmallCoinListActivity.BID_ORDER_NO, bidOrderNo);
        startActivity(intent);

    }

    /**
     * 处理申购，持有，到期的产品状态
     */
    public void disposeProductInfo(ProductSimpleInfoResponse response) {
        coinSackId = response.getSubjectNo();
        bidOrderNo = response.getBidOrderNo();
        productDetailUrl = response.getProductDetailUrl();
        smallCoinSackStatus = response.getMatchStatus();
        smallCoinSackName = response.getProductName();
        mTitleBack.setTitleText(smallCoinSackName);
        if (type == 1) {//申购
            mCoinSackValueLeft.setText(response.getBidAmount());
            mCoinSackValueRight.setText(response.getHasBuyPrecentStr());
            mApplyTimeValue.setText(response.getBidTimeStr());
            mInvestmentDeadlineValue.setText(response.getHorizonStr());

            if("Y".equals(response.getMatchStatus())){
                if (TextUtils.isEmpty(response.getProfitAddStr())) {
                    mPredictIncomeTitleValue.setText(response.getProfitStr());
                } else {
                    mPredictIncomeTitleValue.setText(getSpannableString(response.getProfitStr(), response.getProfitAddStr()));
                }
                mCoinSackProductlistValue.setText(response.getPennyNumber()+"");

            }else{
                mPredictIncomeTitleValue.setText("申购完成后再查看收益");
                mCoinSackProductlistValue.setText("申购中");
            }


        } else if (type == 2) {//持有
            mCoinSackValueLeft.setText(response.getBackAmount());
            mCoinSackValueRight.setText(response.getBackTime());

            ArrayList<String> titles = new ArrayList<>();
            titles.add("申购日");
            titles.add("起息日");
            titles.add("预计到期日");
            ArrayList<String> values = new ArrayList<>();
            values.add(response.getSubDateMMDD());
            values.add(response.getInterestDateMMDD());
            values.add(response.getBackDateMMDD());
            mPurchaseProgressBar.updateState(titles, values, response.getInterestTotal(), response.getFullDay(), response.getTotalDay(), response.getPassDay());

            mInvestmentDeadlineValue.setText(response.getHorizonStr());
            mHoldCapitalValue.setText(response.getBidAmount());
            mCoinSackProductlistValue.setText(response.getPennyNumber()+"");

            if (TextUtils.isEmpty(response.getProfitAddStr())) {
                mPredictIncomeTitleValue.setText(response.getProfitStr());
            } else {
                mPredictIncomeTitleValue.setText(getSpannableString(response.getProfitStr(), response.getProfitAddStr()));
            }

        } else {//回款

            mCoinSackValueLeft.setText(response.getBackAmount());
            mCoinSackValueRight.setText(response.getBackTime());

            ArrayList<String> titles = new ArrayList<>();
            titles.add("申购日");
            titles.add("起息日");
            titles.add("到期日");
            ArrayList<String> values = new ArrayList<>();
            values.add(response.getSubDateMMDD());
            values.add(response.getInterestDateMMDD());
            values.add(response.getBackDateMMDD());
            mPurchaseProgressBar.updateState(titles, values, response.getInterestTotal(), response.getFullDay(), response.getTotalDay(), response.getPassDay());

            mInvestmentDeadlineValue.setText(response.getHorizonStr());
            mHoldCapitalValue.setText(response.getBidAmount());
            mCoinSackProductlistValue.setText(response.getPennyNumber()+"");
            if (TextUtils.isEmpty(response.getProfitAddStr())) {
                mPredictIncomeTitleValue.setText(response.getProfitStr());
            } else {
                mPredictIncomeTitleValue.setText(getSpannableString(response.getProfitStr(), response.getProfitAddStr()));
            }

        }

        if (!TextUtils.isEmpty(response.getAddRateStr())) {
            mAddRateExplain.setVisibility(View.VISIBLE);
            mAddRateExplain.setText(response.getAddRateStr());
        }

        if (TextUtils.isEmpty(response.getYearInterestRateAddStr())) {
            mPredictYearRateTitleValue.setText(response.getYearInterestRateStr());
        } else {
            mPredictYearRateTitleValue.setText(getSpannableString(response.getYearInterestRateStr(), response.getYearInterestRateAddStr()));
        }

    }

    @Override
    public void reloadData() {
        coinSackPresenter.getProductDetailData(productId);
    }

    private SpannableUtil mSpannableUtil;
    private SpannableStringBuilder getSpannableString(String one, String two) {
        return mSpannableUtil.getSpannableString(one, two, R.style.text_14_757575, R.style.text_14_e84a01);

    }

}
