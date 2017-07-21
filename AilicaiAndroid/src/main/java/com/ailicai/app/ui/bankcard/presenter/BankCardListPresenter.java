package com.ailicai.app.ui.bankcard.presenter;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.BankcardListRequest;
import com.ailicai.app.model.request.account.AccountRequest;
import com.ailicai.app.model.response.BankcardListResponse;
import com.ailicai.app.model.response.account.AccountResponse;
import com.ailicai.app.ui.bankcard.BankCardListActivity;
import com.ailicai.app.ui.login.AccountInfo;

/**
 * name: BankCardListPresenter <BR>
 * description: 银行卡列表Presenter <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardListPresenter {

    private BankCardListActivity listActivity;

    private boolean request01Ok = false;
    private boolean request02Ok = false;

    public BankCardListPresenter(BankCardListActivity bankCardListActivity) {
        this.listActivity = bankCardListActivity;
    }

    public void init() {
        listActivity.initData();
        listActivity.installListener();
        httpAction();
    }

    public void httpAction() {
        httpForBankCardList();
        httpForOpenAccountInfo();
    }

    public void httpForBankCardList() {

        BankcardListRequest request = new BankcardListRequest();
        request.setType(0); // 类型 0：我的银行卡列表  1：账单使用银行卡支付时的已绑定的银行卡列表
        ServiceSender.exec(listActivity, request, new IwjwRespListener<BankcardListResponse>() {
            @Override
            public void onStart() {
                listActivity.showLoadView();
            }

            @Override
            public void onJsonSuccess(BankcardListResponse response) {
                if (response != null) {
                    request01Ok = true;
                    listActivity.refreshList(response.getBankcardList());
                    bothSuccessToShowContentView();
                }
            }

            @Override
            public void onFailInfo(String error) {
                listActivity.showErrorView(error);
            }
        });
    }

    public void httpForOpenAccountInfo() {
        AccountRequest request = new AccountRequest();
        ServiceSender.exec(listActivity, request, new IwjwRespListener<AccountResponse>() {

            @Override
            public void onStart() {
                listActivity.showLoadView();
            }

            @Override
            public void onJsonSuccess(AccountResponse response) {
                request02Ok = true;
                AccountInfo.getInstance().saveAccountInfo(response);
                bothSuccessToShowContentView();
            }

            @Override
            public void onFailInfo(String error) {
                listActivity.showErrorView(error);
            }
        });
    }

    // 同时成功才显示
    private void bothSuccessToShowContentView() {
        if (request01Ok && request02Ok) {
            listActivity.showContentView();
        }
    }

    public void reload() {
        if (request01Ok && !request02Ok) {
            httpForOpenAccountInfo();
        } else if (!request01Ok && request02Ok) {
            httpForBankCardList();
        } else {
            httpAction();
        }
    }
}
