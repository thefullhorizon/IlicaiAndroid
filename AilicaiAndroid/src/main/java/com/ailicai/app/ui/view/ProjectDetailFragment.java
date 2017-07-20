package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.response.FinanceProductDetailResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.base.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房产宝项目详情
 * Created by liyanan on 16/4/7.
 */
public class ProjectDetailFragment extends BaseBindFragment {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_marry_info)
    TextView tvMarryInfo;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.tv_company)
    TextView tvCompany;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.gl_info_audit)
    GridLayout gLInfoAudit;
    private String id;
    private String protocolUrl;//居间借款协议地址
    private ProjectDetailPresenter presenter;

    @Override
    public int getLayout() {
        return R.layout.fragment_project_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        id = getArguments().getString(RegularFinancingDetailActivity.PROD_ID);
        presenter = new ProjectDetailPresenter(this);
    }

    public void onRefresh() {
        presenter.requestData(id);

    }

    @Override
    public void reloadData() {
        super.reloadData();
        onRefresh();
    }

    /**
     * 请求成功初始化数据
     *
     * @param response
     */
    public void initData(FinanceProductDetailResponse response) {
        showContentView();
        initGridLayout();
        tvName.setText(response.getUserName());
        tvSex.setText(response.getGender());
        tvMarryInfo.setText(response.getMaritaltatus());
        tvEducation.setText(response.getEducation());
        tvCompany.setText(response.getCompanyType());
        tvTotalPrice.setText(response.getPrice());
        tvAddress.setText(response.getHouseAddress());
        tvArea.setText(response.getSpace());
        tvStatus.setText(response.getTradeStatus());
        protocolUrl = response.getProtocolUrl();
    }

    /**
     * 初始化GridLayout,设置信息审查数据
     */
    private void initGridLayout() {
        List<String> list = new ArrayList<>();
        list.add("身份认证");
        list.add("产证调查");
        list.add("房屋抵押合同");
        list.add("征信报告");
        list.add("收入证明");
        list.add("房屋买卖合同");
        int screenWidth = DeviceUtil.getScreenSize()[0];
        gLInfoAudit.removeAllViews();
        gLInfoAudit.setColumnCount(2);
        gLInfoAudit.setRowCount(3);
        for (int i = 0; i < 6; i++) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_project_detail_audit, null);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvContent.setText(list.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT));
            gLInfoAudit.addView(view);
        }

    }

    /**
     * 点击借款协议
     */
    @OnClick(R.id.tv_protocol)
    void clickProtocol() {
        if (!TextUtils.isEmpty(protocolUrl)) {
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.TITLE, "借款协议");
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.URL, protocolUrl);
            dataMap.put(WebViewActivity.TOPVIEWTHEME, String.valueOf(true));
            MyIntent.startActivity(getActivity(), WebViewActivity.class, dataMap);
            EventLog.upEventLog("354","fuwuxieyi");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.removeFragment();
    }
}
