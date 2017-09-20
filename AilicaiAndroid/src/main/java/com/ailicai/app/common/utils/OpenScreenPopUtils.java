package com.ailicai.app.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.eventbus.ShowScreenPopEvent;
import com.ailicai.app.model.bean.OpenScreenPopModel;
import com.ailicai.app.model.request.OpenScreenPopRequest;
import com.ailicai.app.model.response.OpenScreenPopResponse;
import com.ailicai.app.ui.dialog.OpenScreenFragmentDialog;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jeme on 2017/9/18.
 */

public class OpenScreenPopUtils {

    /***
     * 获取开屏弹窗的数据
     */
    public static void getOpenScreenSupport(final FragmentActivity fragmentActivity){
        OpenScreenPopRequest request = new OpenScreenPopRequest();
        ServiceSender.exec(fragmentActivity.getApplicationContext(), request, new IwjwRespListener<OpenScreenPopResponse>() {
            @Override
            public void onJsonSuccess(OpenScreenPopResponse jsonObject) {
                //保存所有开屏弹窗的数据
                setOpenScreenSupport(jsonObject);
                EventBus.getDefault().post(new ShowScreenPopEvent());

            }
        });
    }

    public static String getOpenScreenKey(String keyPrefix,long adPopId,int adPopPos){
        return new StringBuffer(new StringBuffer(keyPrefix)
                .append(adPopId).append("_").append(adPopPos)).toString();
    }
    /***
     * 此方法会保存两种形式，首先会保存同一页面的所有popId,
     * 再根据popId和popPosition保存单个弹窗对象
     * 弹窗对象会在弹窗弹出后删除数据，再次进入也不会重复写入
     */
    public static void setOpenScreenSupport(OpenScreenPopResponse response){
        ArrayList<Long> tempList;
        HashMap<String,ArrayList<Long>> tempMap = new HashMap<>();
        String mapKey;
        for(OpenScreenPopModel model : response.getOpenScreenPopList()){
            //保存的形式如："adPopPos=5" = "[1,2,3]","adPopPos=6" = "[4,5]"
            mapKey = "adPopPos=" + model.getPopPosition();
            if(tempMap.containsKey(mapKey)){
                tempList = tempMap.get(mapKey);
            }else{
                tempList = new ArrayList<>();
            }
            tempList.add(model.getAdPopupId());
            tempMap.put(mapKey, tempList );

            //先判断该弹窗是否已经弹出过，如果已经弹出过，则不再本地保存，减少io的次数
            if(MyPreference.getInstance().read(
                    getOpenScreenKey(OpenScreenFragmentDialog.IS_FIRST_OPEN,model.getAdPopupId(),model.getPopPosition()),true)){

                MyPreference.getInstance().writeObjectByName(
                        getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,model.getAdPopupId(),model.getPopPosition()),model);
            }
        }
        for(String key : tempMap.keySet()){
            //存在的形式如：<adPopPos=5>{[1,2,3]}</adPopPos=5>,5表示位置，1，2,3表示弹窗id,city=2表示城市编号
            //表示在位置为5的地方，可能存在3个弹窗，需分批次弹出
            MyPreference.getInstance().write(key, JSON.toJSONString(tempMap.get(key)));
        }

    }

    public static List<Long> getOpenScreenSupportId(int popPosition){
        String adPopPosIdStr = MyPreference.getInstance().read(
                "adPopPos="+popPosition,"");
        if(TextUtils.isEmpty(adPopPosIdStr)){
            return null;
        }
        return JSON.parseArray(adPopPosIdStr,Long.class);
    }
    public static OpenScreenPopModel getOpenScreenSupport(long popId, int popPosition){
        return MyPreference.getInstance().read(getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,popId,popPosition),OpenScreenPopModel.class);
    }

    public static void removeOpenScreenSupport(long popId,int popPosition){
        MyPreference.getInstance().remove(getOpenScreenKey(OpenScreenPopModel.OPENSCREENPOP_TAG,popId,popPosition));
    }
}
