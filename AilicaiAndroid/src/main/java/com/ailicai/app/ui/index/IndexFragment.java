package com.ailicai.app.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.OpenAccountFinishEvent;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewFragment;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebJumpUiAction;
import com.ailicai.app.ui.reserve.ReserveActivity;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;
import com.ailicai.app.widget.IWTopTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * name: IndexFragment <BR>
 * description: 首页 <BR>
 * create date: 2017/7/12
 *
 * @author: IWJW Zhou Xuan
 */
public class IndexFragment extends BaseWebViewFragment {


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        EventBus.getDefault().register(this);
        setLoadingStyle(BaseWebViewLayout.LoadingStyle.WHEEL);
        shouldShowLoading(true);
        showBack(false);
        setIWebListener(new BaseWebViewLayout.IWebListener() {
            @Override
            public void onWebLoadStart(BaseWebViewLayout webViewLayout) {

            }

            @Override
            public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {
                if (newProgress > 50) {
                    webViewLayout.shouldShowLoading(false);
                }
            }

            @Override
            public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
            }
        });

        addAction();
        loadUrl("http://192.168.1.44:2323/licai");

        CommonUtil.uiSystemBarTint(getActivity(), getView());
        IWTopTitleView topTitleView = (IWTopTitleView) getView().findViewById(R.id.webview_title);
        topTitleView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        startOrStopAutoRefresh(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        startOrStopAutoRefresh(false);
    }

    private void addAction() {

        addJumpUiActions(new WebJumpUiAction("regulardetail") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
                    Intent intent = new Intent(getActivity(), RegularFinanceDetailH5Activity.class);
                    intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, url);
                    startActivity(intent);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("holdtiyanbao") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                String couponId = params.get("couponId");
                Intent intent = new Intent(getActivity(), CapitalListProductDetailActivity.class);
                intent.putExtra(CapitalListProductDetailActivity.IS_TIYANBAO, true);
                intent.putExtra(CapitalListProductDetailActivity.TIYANBAO_ID, Long.parseLong(couponId));
                intent.putExtra(CapitalListProductDetailActivity.TYPE, 2);
                startActivity(intent);
            }
        });

        addJumpUiActions(new WebJumpUiAction("reserve") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                goReserve();
            }
        });

        addJumpUiActions(new WebJumpUiAction("indexmore") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
               IndexActivity.goToInvestTab(getWRActivity(),0);
            }
        });
    }

    private void goReserve() {
        Intent intent = new Intent(getActivity(), ReserveActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOpenAccountFinshEvent(OpenAccountFinishEvent finishEvent) {
        callJsRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOpenAccountFinshEvent(LoginEvent loginEvent) {
        callJsRefresh();
    }
}
