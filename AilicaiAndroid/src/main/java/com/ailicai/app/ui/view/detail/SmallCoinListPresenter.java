package com.ailicai.app.ui.view.detail;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.CoinListRequest;
import com.ailicai.app.model.response.CoinListResponse;
import com.ailicai.app.ui.login.UserInfo;

/**
 * Created by nanshan on 2017/5/4.
 */
public class SmallCoinListPresenter {

    private SmallCoinListActivity mView;

    public SmallCoinListPresenter(SmallCoinListActivity view){
        this.mView = view;
    }

    public void updateCoinListData(String productId, String bidOrderNo, int offset , boolean refresh){
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            CoinListRequest request = new CoinListRequest();
            request.setUserId(UserInfo.getInstance().getUserId());
            request.setBidOrderNo(bidOrderNo);
            request.setProductId(productId);
            request.setOffset(offset);
            request.setPageSize(10);
            ServiceSender.exec(mView, request, new SmallCoinListCallback(refresh));
        }
    }

    class SmallCoinListCallback extends IwjwRespListener<CoinListResponse> {
        boolean needClear;
        public SmallCoinListCallback(boolean needClear) {
            this.needClear = needClear;
        }

        @Override
        public void onStart() {
            mView.showLoadView();
        }

        @Override
        public void onJsonSuccess(CoinListResponse response) {
            mView.showContentView();
            if (response.getErrorCode() == 0) {
                mView.disposeCoinListInfo(response,needClear);
            } else {
                onFailInfo(response.getMessage());
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            mView.showErrorView(errorInfo);
        }

    }




}
