package com.ailicai.app.ui.html5;

import android.text.TextUtils;

import com.ailicai.app.common.support.SupportFinance;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duo.chen on 2016/6/3.
 */

public class SupportUrl {

    public static void setUrls(Iwjwh5UrlResponse response) {
        MyPreference.getInstance().write("aboutUsUrl", response.getAboutUrl());
        MyPreference.getInstance().write("helpCenterUrl", response.getHelpCenterUrl());
        MyPreference.getInstance().write("loanHelpUrl", response.getLoanHelpUrl());
        MyPreference.getInstance().write("orderH5Url", response.getOrderH5Url());
        MyPreference.getInstance().write("orderDetailH5Url", response.getOrderDetailH5Url());
        MyPreference.getInstance().write("rentOrderDetailH5Url", response.getRentOrderDetailH5Url());
        MyPreference.getInstance().write("rentBillDetailH5Url", response.getRentBillDetailH5Url());
        MyPreference.getInstance().write("defaultNewHouseUrl", response.getDefaultNewHouseUrl());
        MyPreference.getInstance().write("CouponUrl", response.getCardUrl());
        MyPreference.getInstance().write("tradeEnsureCardUrl", response.getTradeEnsureCardUrl());
        MyPreference.getInstance().write("rentHouseCommissionUrl", response.getRentHouseCommissionUrl());
        MyPreference.getInstance().write("ailicaiUrl", response.getAilicaiUrl());
        MyPreference.getInstance().write("ailicaiType", response.getAlicaiType());
        MyPreference.getInstance().write("supportcardsByAllUrl", response.getSupportcardsByAllUrl());
        MyPreference.getInstance().write("rebateUrl", response.getRebateUrl());//返利券详情URL
        MyPreference.getInstance().write("brandShareUrl", response.getBrandShareUrl());//返利券详情URL
        setH5PayRentUrl(response.getPayRentH5Url());
        setH5PayRentBillDetailUrl(response.getPayRentBillDetailH5Url());
        setFinanceSupport(response);
    }

    private static void setFinanceSupport(Iwjwh5UrlResponse response) {
        SupportFinance supportFinance = new SupportFinance();
        supportFinance.setSafecardExplainUrl(response.getSafecardExplainUrl());
        supportFinance.setAccountProtocol(response.getAccountProtocol());
        supportFinance.setAilicaiProtocol(response.getAilicaiProtocol());
        supportFinance.setAlicaiType(response.getAlicaiType());
        supportFinance.setProductDetailUrl(response.getProductDetailUrl());
        supportFinance.setTiyanbaoDetailUrl(response.getTiyanbaoDetailUrl());
        MyPreference.getInstance().write(supportFinance);
    }

    public static String getOpenScreenKey(String keyPrefix, long adPopId, int adPopPos){
        return new StringBuffer(new StringBuffer(keyPrefix)
                .append(adPopId).append("_").append(adPopPos)).toString();
    }
    /***
     * 此方法会保存两种形式，首先会保存同一页面的所有popId,
     * 再根据popId和popPosition保存单个弹窗对象
     * 弹窗对象会在弹窗弹出后删除数据，再次进入也不会重复写入
     */
//    public static void setOpenScreenSupport(OpenScreenPopVoResponse response){
//        ArrayList<Long> tempList;
//        HashMap<String,ArrayList<Long>> tempMap = new HashMap<>();
//        String mapKey;
//        for(OpenScreenPopModel model : response.getOpenScreenPopVoList()){
//            //保存的形式如："adPopPos=5_city=2" = "[1,2,3]","adPopPos=6" = "[4,5]"
//            mapKey = "adPopPos=" + model.getPopPosition() + "_city=" + CityManager.getInstance().getCurrentCity().getCityId();
//            if(tempMap.containsKey(mapKey)){
//                tempList = tempMap.get(mapKey);
//            }else{
//                tempList = new ArrayList<>();
//            }
//            tempList.add(model.getAdPopupId());
//            tempMap.put(mapKey, tempList );
//
//            //先判断该弹窗是否已经弹出过，如果已经弹出过，则不再本地保存，减少io的次数
//            if(MyPreference.getInstance().read(
//                    getOpenScreenKey(OpenScreenFragmentDialog.IS_FIRST_OPEN,model.getAdPopupId(),model.getPopPosition()),true)){
//
//                MyPreference.getInstance().writeObjectByName(
//                        getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,model.getAdPopupId(),model.getPopPosition()),model);
//            }
//        }
//        for(String key : tempMap.keySet()){
//            //存在的形式如：<adPopPos=5_city=2>{[1,2,3]}</adPopPos=5_city=2>,5表示位置，1，2,3表示弹窗id,city=2表示城市编号
//            //表示在位置为5的地方，可能存在3个弹窗，需分批次弹出
//            MyPreference.getInstance().write(key, JSON.toJSONString(tempMap.get(key)));
//        }
//
//    }
//
//    public static List<Long> getOpenScreenSupportId(int popPosition){
//        String adPopPosIdStr = MyPreference.getInstance().read(
//                "adPopPos="+popPosition + "_city="+ CityManager.getInstance().getCurrentCity().getCityId(),"");
//        if(TextUtils.isEmpty(adPopPosIdStr)){
//           return null;
//        }
//        return JSON.parseArray(adPopPosIdStr,Long.class);
//    }
//    public static OpenScreenPopModel getOpenScreenSupport(long popId,int popPosition){
//        return MyPreference.getInstance().read(getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,popId,popPosition),OpenScreenPopModel.class);
//    }
//
//    public static void removeOpenScreenSupport(long popId,int popPosition){
//        MyPreference.getInstance().remove(getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,popId,popPosition));
//    }

    public static String getAboutUsUrl() {
        return MyPreference.getInstance().read("aboutUsUrl", "");
    }


    public static String getHelpCenterUrl() {
        return MyPreference.getInstance().read("helpCenterUrl", "");
    }

    public static String getLoanHelpUrl() {
        return MyPreference.getInstance().read("loanHelpUrl", "");
    }

    public static String getDefaultNewHouseUrl() {
        return MyPreference.getInstance().read("defaultNewHouseUrl", "");
    }

    public static String getCardUrl() {
        return MyPreference.getInstance().read("CouponUrl", "");
    }

    public static String getTradeEnsureCardUrl() {
        return MyPreference.getInstance().read("tradeEnsureCardUrl", "");
    }

    public static String getAilicaiUrl() {
        return MyPreference.getInstance().read("ailicaiUrl", "");
    }

    public static String getOrderH5Url() {
        return MyPreference.getInstance().read("orderH5Url", "");
    }

    public static String getOrderDetailH5Url() {
        return MyPreference.getInstance().read("orderDetailH5Url", "");
    }

    public static String getRentOrderDetailH5Url() {
        return MyPreference.getInstance().read("rentOrderDetailH5Url", "");
    }

    public static String getRentBillDetailH5Url() {
        return MyPreference.getInstance().read("rentBillDetailH5Url", "");
    }

    public static String getRentHouseCommissionUrl() {
        return MyPreference.getInstance().read("rentHouseCommissionUrl", "");
    }

    public static String getSupportcardsByAllUrl() {
        return MyPreference.getInstance().read("supportcardsByAllUrl", "");
    }

    /**
     * 返利券详情
     * @return
     */
    public static String getRebateUrl() {
        return MyPreference.getInstance().read("rebateUrl", "");
    }

    /**
     * 品牌公寓分享
     * @return
     */
    public static String getBrandShareUrl() {
        return MyPreference.getInstance().read("brandShareUrl", "");
    }

    public static String getSafecardExplainUrl() {
        SupportFinance supportFinance = MyPreference.getInstance().read(SupportFinance.class);
        return supportFinance != null ? supportFinance.getSafecardExplainUrl() : "";
    }

    public static String getAccountProtocol() {
        SupportFinance supportFinance = MyPreference.getInstance().read(SupportFinance.class);
        return supportFinance != null ? supportFinance.getAccountProtocol() : "";
    }

    public static String getAilicaiProtocol() {
        SupportFinance supportFinance = MyPreference.getInstance().read(SupportFinance.class);
        return supportFinance != null ? supportFinance.getAilicaiProtocol() : "";
    }

    /**
     * 爱理财开启状态和方式
     *
     * @return
     */
    public static String getAlicaiType() {
        return MyPreference.getInstance().read("ailicaiType", "");
    }

    public static void setAilicaiUrl(String ailicaiUrl) {
        MyPreference.getInstance().write("ailicaiUrl", ailicaiUrl);
    }

    public static void setH5PayRentUrl(String h5PayRentUrl) {
        MyPreference.getInstance().write("h5PayRentUrl", h5PayRentUrl);
    }

    public static void setH5PayRentBillDetailUrl(String h5PayRentBillDetailUrl) {
        MyPreference.getInstance().write("h5PayRentBillDetailUrl", h5PayRentBillDetailUrl);
    }

    /**
     * 交房租的url
     */
    public static String getH5PayRentUrl() {
        return MyPreference.getInstance().read("h5PayRentUrl", "");
    }

    /**
     * 交房租的账单详情url
     */
    public static String getH5RentBillDetailUrl() {
        return MyPreference.getInstance().read("h5PayRentBillDetailUrl", "");
    }

    public static void setLoanUrl(String loanUrl) {
        MyPreference.getInstance().write("loanUrl", loanUrl);
    }


    public static String getLoanUrl() {
        return MyPreference.getInstance().read("loanUrl", "");
    }

    public static void setFlatHouseUrl(String loanUrl) {
        MyPreference.getInstance().write("flatHouseUrl", loanUrl);
    }

    public static String getFlatHouseUrl() {
        return MyPreference.getInstance().read("flatHouseUrl", "");
    }


}
