package com.ailicai.app.common.logCollect;


import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.utils.MyPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanshan on 5/19/2017.
 * 新增pv_id埋点 处理类
 */

public class PVIDHandler {

    public final static String PV_ID_KEY = "Pv_id";

    public static Map<String,String> pageType = new HashMap<String,String>();

    static{
        pageType.put("IndexFragment","homepage");//首页
        pageType.put("AttentionFragement2","collect");//关注
        pageType.put("MessageFragment","message");//消息
        pageType.put("PersonalCenterFragment","my");//我的

        //MapListContainerFragment 包含 BusinessListFragment 列表,BusinessMapFragment 地图
        //列表页（二手房、租房、品牌公寓、新房）
        pageType.put("BusinessListFragment","list");//二手房、租房、品牌公寓
        pageType.put("NewHouseListActivity","list");//新房
        pageType.put("EstateListActivity","list");//查小区

        //地图页（二手房、租房、品牌公寓、新房）
        pageType.put("BusinessMapFragment","map");

        //详情页（二手房、租房、品牌公寓、新房、吉爱财）
        pageType.put("HouseDetailFragment","detail");//品牌公寓
        pageType.put("FlatHouseDetailFragment","detail");//租房,品牌公寓
        pageType.put("NewHouseDetailActivity","detail");//新房
        pageType.put("CapitalListProductDetailActivity","detail");//吉爱财--房产宝
        pageType.put("SmallCoinSackActivity","detail");//吉爱财--小钱袋
        //小区详情页(二手房、租房、品牌公寓)
        pageType.put("HouseEstateDetailCoordinatorFragment","estate_detail");//二手房

        pageType.put("MyWalletActivity","wallet");//钱包
        pageType.put("H5OrderListActivity","order");//订单页
        pageType.put("CartListActivity","seek");//约看清单页
        pageType.put("AgendaCardListActivity","appointment");//看房日程

        pageType.put("LoginActivity","login");//登录
        //搜索页 -->全局搜SearchA SearchF
        pageType.put("IndexSearchActivity","search");
        pageType.put("EstateSearchActivity","search");
        pageType.put("HouseSearchFragment","search");
        pageType.put("NewHouseSearchActivity","search");//新房搜索页
        //v6.9新增
        pageType.put("BaseMessageListActivity" + PushMessage.INFOTYPE,"msg_advsory");
        pageType.put("BaseMessageListActivity" + PushMessage.REMINDTYPE,"msg_remind");
        pageType.put("BaseMessageListActivity" + PushMessage.ACTIVITYTYPE,"msg_activity");
    }

    public static String getPageType(String simpleClassName){
        return pageType.containsKey(simpleClassName)?pageType.get(simpleClassName):"other";//其他other（没有定义页面类型的页面都归为其他 ）
    }

    public static int getPVID(){
        return MyPreference.getInstance().read(PV_ID_KEY, 0);
    }

    public static void setPVID(int value){
        MyPreference.getInstance().write(PV_ID_KEY,value);
    }

    public static void uploadPVIDLogical(String className){
        int lastPVID = PVIDHandler.getPVID();
        PVIDHandler.setPVID(++lastPVID);
        EventLog.upEventLctLog(className,lastPVID+"",PVIDHandler.getPageType(className));
//        LogUtil.d("nanshan","："+className+"--"+PVIDHandler.getPVID()+""+PVIDHandler.getPageType(className));
    }

    /**
     *
     * @param className
     * @param pageType 可以指定ClassNamed对应的pageType
     */
    public static void uploadPVIDLogical(String className, String pageType){
        int lastPVID = PVIDHandler.getPVID();
        PVIDHandler.setPVID(++lastPVID);
        EventLog.upEventLctLog(className,lastPVID+"",pageType);
    }
}
