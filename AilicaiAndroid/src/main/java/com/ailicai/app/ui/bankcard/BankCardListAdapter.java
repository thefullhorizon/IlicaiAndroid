package com.ailicai.app.ui.bankcard;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.model.bean.BankcardModel;

import java.util.List;

/**
 * name: BankCardListAdapter <BR>
 * description: 银行卡列表 <BR>
 * create date: 2016/1/7
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardListAdapter extends LibraryBaseAdapter<BankcardModel>{

    public BankCardListAdapter(List<BankcardModel> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_bankcard_list,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BankcardModel itemBean = getData().get(position);

        // 设置银行卡ICON
        final String bankCode = itemBean.getBankCode();
        int bankIconResId = getBankIconResId(bankCode);
        holder.imageViewBankIcon.setBackgroundResource(bankIconResId);

        // 银行卡号和银行名
        holder.textViewBankName.setText(itemBean.getBankName());
        holder.textViewBankNumber.setText(itemBean.getCardNo());

        //储蓄卡还是信用卡
        String cardTypeDesc = getBankTypeDesc(itemBean.getCardType());
        holder.textViewBankType.setText(cardTypeDesc);

        // 是否是安全卡
       final boolean isSafeCard = isSafeCard(itemBean.getIsSafeCard());
        if(isSafeCard) {
            holder.textViewIfSafeCard.setVisibility(View.VISIBLE);
        } else {
            holder.textViewIfSafeCard.setVisibility(View.INVISIBLE);
        }

        // 银行半透明背景
        int bgResId = getBankBgResId(itemBean.getBankCode());
        holder.imageViewBankBg.setBackgroundResource(bgResId);

        // 根据不同的银行设置不同的背景色
        int cardColorId = getCardResId(itemBean.getBankCode());
        holder.relativeCardContainer.setBackgroundResource(cardColorId);

        final String bankCardId = itemBean.getBankAccountId();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSafeCard) {
                    goToSafeBankCardDetail(bankCardId,bankCode);
                } else {
                    goToNormalBankCardDetail(bankCardId,bankCode);
                }
            }
        });

        return convertView;
    }

    private void goToSafeBankCardDetail(String bankCardId, String bankCode) {
        Intent intent = new Intent(getContext(),BankCardSafeDetailActivity.class);
        intent.putExtra("bankCardId",bankCardId);
        intent.putExtra("bankCode",bankCode);
        getContext().startActivity(intent);
    }

    private void goToNormalBankCardDetail(String bankCardId, String bankCode) {
        Intent intent = new Intent(getContext(),BankCardSafeDetailActivity.class);
        intent.putExtra("bankCardId",bankCardId);
        intent.putExtra("bankCode",bankCode);
        getContext().startActivity(intent);
    }

    private class ViewHolder {

        public View relativeCardContainer;
        public ImageView imageViewBankIcon;
        public TextView textViewBankName;
        public TextView textViewBankType;
        public TextView textViewIfSafeCard;
        public TextView textViewBankNumber;
        public ImageView imageViewBankBg;

        public ViewHolder(View convertView) {
            relativeCardContainer = convertView.findViewById(R.id.relativeCardContainer);
            imageViewBankIcon = (ImageView) convertView.findViewById(R.id.imageViewBankIcon);
            textViewBankName = (TextView) convertView.findViewById(R.id.textViewBankName);
            textViewBankType = (TextView) convertView.findViewById(R.id.textViewBankType);
            textViewIfSafeCard = (TextView) convertView.findViewById(R.id.textViewIfSafeCard);
            textViewBankNumber = (TextView) convertView.findViewById(R.id.textViewBankNumber);
            imageViewBankBg = (ImageView) convertView.findViewById(R.id.imageViewBankBg);
        }
    }

//    ABC 农业银行
//    GNXS 广州市农信社
//    BCCB 北京银行
//    GZCB 广州市商业银行
//    BJRCB 北京农商行
//    HCCB 杭州银行
//    BOC  中国银行
//    HKBCHINA 汉口银行
//    BOS 上海银行
//    HSBANK 徽商银行
//    CBHB 渤海银行
//    HXB 华夏银行
//    CCB 建设银行
//    ICBC 工商银行
//    CCQTGB  重庆三峡银行
//    NBCB 宁波银行
//    CEB 光大银行
//    NJCB 南京银行
//    CIB 兴业银行
//    PSBC 中国邮储银行
//    CITIC 中信银行
//    CMB 招商银行
//    SHRCB 上海农村商业银行
//    CMBC 民生银行
//    SNXS 深圳农村商业银行
//    COMM 交通银行
//    SPDB 浦东发展银行
//    CSCB 长沙银行
//    SXJS 晋城市商业银行
//    CZB 浙商银行
//    SZPAB 平安银行
//    CZCB 浙江稠州商业银行
//    UPOP 银联在线支付
//    GDB 广东发展银行
//    WZCB 温州市商业银行
//    JSCB	江苏银行
//    DLCB	大连银行
//    ORDOS	鄂尔多斯银行


    private int getBankIconResId(String bankCode) {
        if("ABC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_nongye_bank;
        } else  if("BOC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_zhongguo_bank;
        } else  if("BOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_shanghai_bank;
        }  else  if("HXB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_huaxia_bank;
        } else  if("CCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_jianshe_bank;
        } else  if("ICBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_gongshang_bank;
        } else  if("NBCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_ningbo_bank;
        } else  if("CIB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_xinye_bank;
        } else  if("PSBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_youzheng_bank;
        } else  if("CMBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_minsheng_bank;
        } else  if("COMM".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_jiaotong_bank;
        } else  if("SZPAB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_pingan_bank;
        } else  if("CMB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_zhaoshang_bank;
        } else  if("GDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_guangfa_bank;
        } else  if("SPDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_pufa_bank;
        } else if("CITIC".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_zhongxin_bank;
        } else if ("SHRCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_shanghai_nongshang_bank;
        } else if ("JSCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_jiangsu_bank;
        } else if ("DLCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_dalian_bank;
        } else if ("ORDOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.mycard_list_eerduosi_bank;
        }
        return R.drawable.mycard_list_default_bankcard_icon;
    }

    //【1：储蓄卡 、2：信用卡 、3：存折 、4：其它 】
    private String getBankTypeDesc(int bankType) {
        if(bankType == 1) {
            return "储蓄卡";
        } else if(bankType == 2) {
            return "信用卡";
        } else if(bankType == 3) {
            return "存折";
        } else if(bankType == 4) {
            return "其它";
        }
        return "其它";
    }

//    是否安全卡 0-否 1-是
    private boolean isSafeCard(int safeCardLabel) {
        if(safeCardLabel == 0) {
            return false;
        } else if(safeCardLabel == 1) {
            return  true;
        }
        return  false;
    }

    private int getCardResId(String bankCode) {
        if("ABC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_nongye_radius;
        } else  if("BOC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_zhongguo_radius;
        } else  if("BOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_shanghai_radius;
        }  else  if("HXB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_huaxia_radius;
        } else  if("CCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_jianshe_radius;
        } else  if("ICBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_gongshang_radius;
        } else  if("NBCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_ningbo_radius;
        } else  if("CIB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_xinye_radius;
        } else  if("PSBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_youzheng_radius;
        } else  if("CMBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_minsheng_radius;
        } else  if("COMM".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_jiaotong_radius;
        } else  if("SZPAB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_pingan_radius;
        } else  if("CMB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_zhaoshang_radius;
        } else  if("GDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_guangfa_radius;
        } else  if("SPDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_pufa_radius;
        }else if("CITIC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_zhongxin_radius;
        } else if ("SHRCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_shanghainongshang_radius;
        } else if ("JSCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_jiangsu_radius;
        } else if ("DLCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_dalian_radius;
        } else if ("ORDOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.bankcard_eerduosi_radius;
        }
        return R.drawable.bankcard_default_radius;
    }

    private int getBankBgResId(String bankCode) {
        if("ABC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_nongye_bank_icon;
        } else  if("BOC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_zhongguo_bank_icon;
        } else  if("BOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_shanghai_bank_icon;
        }  else  if("HXB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_huaxia_bank_icon;
        } else  if("CCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_jianshe_bank_icon;
        } else  if("ICBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_gongshang_bank_icon;
        } else  if("NBCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_ningbo_bank_icon;
        } else  if("CIB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_xinye_bank_icon;
        } else  if("PSBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_youzheng_bank_icon;
        } else  if("CMBC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_minsheng_bank_icon;
        } else  if("COMM".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_jiaotong_bank_icon;
        } else  if("SZPAB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_pingan_bank_icon;
        } else  if("CMB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_zhaoshang_bank_icon;
        } else  if("GDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_guangfa_bank_icon;
        } else  if("SPDB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_pufa_bank_icon;
        } else if("CITIC".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_zhongxin_bank_icon;
        } else if ("SHRCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_shanghai_bank_icon;
        } else if ("JSCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_jiangsu_bank_icon;
        } else if ("DLCB".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_dalian_bank_icon;
        } else if ("ORDOS".equalsIgnoreCase(bankCode)) {
            return R.drawable.bg_eerduosi_bank_icon;
        }
        return R.drawable.bg_default_bankcard_icon;
    }
}
