package com.ailicai.app.ui.bankcard.presenter;


import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.BankCardLDetailRequest;
import com.ailicai.app.model.request.account.AccountRequest;
import com.ailicai.app.model.response.BankCardDetailResponse;
import com.ailicai.app.model.response.account.AccountResponse;
import com.ailicai.app.ui.bankcard.BankCardDetailActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserInfo;

/**
 * name: BankCardDetailPresenter <BR>
 * description: 银行卡详情Presenter <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardDetailPresenter {

    private BankCardDetailActivity detailActivity;

    public BankCardDetailPresenter(BankCardDetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    public void init() {
        detailActivity.initData();
        httpForBankCardDetail();
    }

    public void httpForBankCardDetail() {
        BankCardLDetailRequest request = new BankCardLDetailRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        ServiceSender.exec(detailActivity, request, new IwjwRespListener<BankCardDetailResponse>() {
            @Override
            public void onStart() {
                detailActivity.showLoadView();
            }

            @Override
            public void onJsonSuccess(BankCardDetailResponse response) {
                if(response != null) {
                    detailActivity.bindViewData(response);
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

    public int getBankBgColorId(String bankCode) {
        if("ABC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankNongYe;
        } else  if("BOC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankZhongGuo;
        } else  if("BOS".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankShangHai;
        }  else  if("HXB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankHuaXia;
        } else  if("CCB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankJianShe;
        } else  if("ICBC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankGongShang;
        } else  if("NBCB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankNingBo;
        } else  if("CIB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankXingYe;
        } else  if("PSBC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankYouZheng;
        } else  if("CMBC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankMinSheng;
        } else  if("COMM".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankJiaoTong;
        } else  if("SZPAB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankPingAn;
        } else  if("CMB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankZhaoShang;
        } else  if("GDB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankGuangFa;
        } else  if("SPDB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankPuFa;
        }else if("CITIC".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankZhongXin;
        } else if ("SHRCB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankShangHaiNongShang;
        } else if ("JSCB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankJiangSu;
        } else if ("DLCB".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankDaLian;
        } else if ("ORDOS".equalsIgnoreCase(bankCode)) {
            return R.color.colorBankEErDuoSi;
        }
        return R.color.colorBankDefault;
    }
}
