package com.ailicai.app.ui.bankcard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.BankcardModel;
import com.ailicai.app.ui.account.OpenAccountWebViewActivity;
import com.ailicai.app.ui.bankcard.presenter.BankCardListPresenter;
import com.ailicai.app.ui.base.BaseBindActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * name: BankCardListActivity <BR>
 * description: 我的银行卡  列表  首页<BR>
 * create date: 2016/1/6
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardListActivity extends BaseBindActivity {

    public static boolean NEED_MANUAL_REFRESH_LIST = false;
    public static int CHECK_PAY_PWD_REQUEST_CODE = 80;
    public static int SET_PAY_PWD_REQUEST_CODE = 81;

    BankCardListPresenter presenter;

    @Bind(R.id.listViewBankCard)
    ListView listViewBankCard;

    private BankCardListAdapter listAdapter;
    private List<BankcardModel> bankCardList = new ArrayList<>();
    private View footView;

    @Override
    public int getLayout() {
        return R.layout.activity_bankcard_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter = new BankCardListPresenter(this);
        presenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NEED_MANUAL_REFRESH_LIST) {
            reloadData();
            NEED_MANUAL_REFRESH_LIST = false;
        }
    }

    public void initData() {
        listAdapter = new BankCardListAdapter(bankCardList, this);
        footView = getLayoutInflater().inflate(R.layout.footview_bankcard_list, null);
        listViewBankCard.addFooterView(footView);
        listViewBankCard.setAdapter(listAdapter);
    }

    public void refreshList(List<BankcardModel> bankCardList) {
        listAdapter.refresh(bankCardList);
        this.bankCardList = bankCardList;
        if(bankCardList != null && bankCardList.size() > 0) {
            footView.setVisibility(View.GONE);
        } else {
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void installListener() {
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBindCard();
            }
        });
    }

    private void goToBindCard() {
        OpenAccountWebViewActivity.goToOpenAccount(this);
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.reload();
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }
}
