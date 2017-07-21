package com.ailicai.app.ui.bankcard.presenter;


import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.BankCardLDetailRequest;
import com.ailicai.app.model.response.BankCardDetailResponse;
import com.ailicai.app.ui.bankcard.BankCardSafeDetailActivity;
import com.ailicai.app.ui.login.UserInfo;

/**
 * name: BankCardListPresenter <BR>
 * description: 银行卡安全卡详情Presenter <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardSafeDetailPresenter {

    BankCardSafeDetailActivity detailActivity;
    public BankCardSafeDetailPresenter(BankCardSafeDetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    public void init() {
        httpForBankCardDetail();
    }

    public void httpForBankCardDetail() {

        BankCardLDetailRequest request = new BankCardLDetailRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setBankAccountId(detailActivity.getBankCardId());
        ServiceSender.exec(detailActivity, request, new IwjwRespListener<BankCardDetailResponse>() {
            @Override
            public void onStart() {
                detailActivity.showLoadView();
            }

            @Override
            public void onJsonSuccess(BankCardDetailResponse response) {
                if(response != null) {
                    detailActivity.setData(response);
                    detailActivity.showContentView();
                }
            }

            @Override
            public void onFailInfo(String error) {
                detailActivity.showErrorView(error);
            }
        });
    }

    public int getBankIconResId(String bankCode) {
        if("ABC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_nongye_ban;
        } else  if("BOC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_zhongguo_bank_icon;
        } else  if("BOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_shanghai_bank;
        }  else  if("HXB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_huaxia_bank;
        } else  if("CCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_jianshe_bank;
        } else  if("ICBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_gongshang_bank_icon;
        } else  if("NBCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_ningbo_bank;
        } else  if("CIB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_xinye_bank;
        } else  if("PSBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_youzheng_bank;
        } else  if("CMBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_minsheng_bank_icon;
        } else  if("COMM".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_jiaotong_bank;
        } else  if("SZPAB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_pingan_bank;
        } else  if("CMB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_zhaoshang_bank_icon;
        } else  if("GDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_guangfa_bank;
        } else  if("SPDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_pufa_bank;
        } else if("CITIC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_zhongxin_bank;
        }
        return R.drawable.mycard_list_default_bankcard_icon;
    }

}
