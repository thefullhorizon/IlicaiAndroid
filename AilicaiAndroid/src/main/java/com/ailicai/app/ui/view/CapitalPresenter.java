package com.ailicai.app.ui.view;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.AccountRequest;
import com.ailicai.app.model.request.AssetInfoRequest;
import com.ailicai.app.model.request.HasBuyProductListRequest;
import com.ailicai.app.model.response.AccountResponse;
import com.ailicai.app.model.response.AssetInfoResponse;
import com.ailicai.app.model.response.HasBuyProductListResponse;
import com.ailicai.app.ui.asset.treasure.CapitalHeaderState;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserInfo;

/**
 * Created by David on 2015/12/28.
 */
public class CapitalPresenter {


    CapitalActivity mActivity;
    AssetInfoResponse asset;

    /**
     * 请求标识，以最新请求为准，用于拦截不希望的请求返回.
     */
    long session;

    public CapitalPresenter(CapitalActivity activity) {
        this.mActivity = activity;

        //initialize data.
        asset = new AssetInfoResponse();
    }

    public void updateHeader() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            mActivity.deployPageElements(asset);
            mActivity.onRefreshFinished(true);
            mActivity.showNoDataView();
        } else {
            CapitalHeaderState state = getHeaderState();
            if (state == CapitalHeaderState.NONE) sendAccountService();
            mActivity.deployPageElements(asset);
            sendAssetService();
        }
    }

    public void updateContent(boolean dynamic) {
//        if (getHeaderState() != CapitalHeaderState.LoginAccount) {
//            mActivity.showNoDataView();
//        } else {
//            if (dynamic) mActivity.loadingState(true, true);
//            sendProductService(true, 0, 0);
//        }
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN){
            if (dynamic) mActivity.loadingState(true, true);
            sendProductService(true, 0, 0);
        }
    }

    private CapitalHeaderState getHeaderState() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) return CapitalHeaderState.UnLogin;
        if (AccountInfo.hasCacheAccountInfo()) {
            if (AccountInfo.isOpenAccount()) return  CapitalHeaderState.LoginAccount;
            else return CapitalHeaderState.UnAccount;
        } else {
            return CapitalHeaderState.NONE;
        }
    }

    public void sendAssetService() {
        ServiceSender.exec(mActivity, new AssetInfoRequest(),  new AssetCallback());
    }

    public void sendAccountService() {
        ServiceSender.exec(mActivity, new AccountRequest(),  new AccountCallback());
    }

    public void handleAccountResponse(AccountResponse account) {
        //record account info.
        AccountInfo.getInstance().saveAccountInfo(account);

        if (account.getIsOpenAccount() == 0) {
            mActivity.deployPageElements(asset);
        } else {
            mActivity.deployPageElements(asset);
        }

        mActivity.onRefreshFinished(true);
        updateContent(true);
    }

    public void handleAssetResponse(AssetInfoResponse asset) {
        this.asset = asset;
        mActivity.deployPageElements(asset);
        mActivity.onRefreshFinished(true);
    }

    public String getProductId() {
        if (asset.getProduct() == null) return null;
        return asset.getProduct().getProductId();
    }

    public void sendProductService(boolean refresh, int offset, int reserveOffset) {
        session = System.currentTimeMillis();
        long time = session;

        HasBuyProductListRequest request = new HasBuyProductListRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setType(mActivity.getCategory().getType());
        request.setPageSize(10);
        request.setOffset(offset);
        request.setReserveOffset(reserveOffset);


        ServiceSender.exec(mActivity, request, new ProductCallback(refresh, time));
    }

    public void handleProductResponse(HasBuyProductListResponse response, boolean refresh, long session) {
        mActivity.onRefreshFinished(refresh);

        if (response == null) return;
        if (this.session != session) return;
        mActivity.deployProductList(response, refresh);
    }


    /************************************** services ************************************/
     class AccountCallback extends IwjwRespListener<AccountResponse> {

        @Override
        public void onStart() {
        }

        @Override
        public void onJsonSuccess(AccountResponse response) {
            if (mActivity == null) return;
            handleAccountResponse(response);
        }

        @Override
        public void onFailInfo(String error) {

            if ( mActivity== null) return;

            ToastUtil.showInBottom(mActivity, error);
            mActivity.deployPageElements(new AssetInfoResponse());
            mActivity.onRefreshFinished(true);
        }
    }

     class AssetCallback extends IwjwRespListener<AssetInfoResponse> {

        @Override
        public void onStart() {
            mActivity.showLoadView();
        }

        @Override
        public void onJsonSuccess(AssetInfoResponse response) {
            if (mActivity == null) return;
            mActivity.showContentView();
            handleAssetResponse(response);
        }

        @Override
        public void onFailInfo(String error) {
            if (mActivity == null) return;
            ToastUtil.showInBottom(mActivity, error);
            mActivity.onRefreshFinished(true);
            mActivity.showNetworkErrorView(false,error);
        }
    }

     class ProductCallback extends IwjwRespListener<HasBuyProductListResponse> {

        boolean _refresh;
        long _session;

        public ProductCallback(boolean refresh, long session) {
            this._refresh = refresh;
            this._session = session;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onJsonSuccess(HasBuyProductListResponse response) {
            if (mActivity == null) return;
            handleProductResponse(response, _refresh, _session);
        }

        @Override
        public void onFailInfo(String error) {
            if (mActivity == null) return;
            ToastUtil.showInBottom(mActivity, error);
            mActivity.onRefreshFinished(_refresh);
            mActivity.showNetworkErrorView(false,error);
        }

    }

}
