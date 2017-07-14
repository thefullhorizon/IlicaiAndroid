package com.ailicai.app.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.Map;

/**
 * Created by Administrator on 2015/6/18.
 */
public class MinePresenter {
    private static final int SHOW_AND_HIDE = 3;

//    public void gotoSettings(FragmentActivity activity) {
//        if (CheckDoubleClick.isFastDoubleClick()) {
//            return;
//        }
//        SettingsFragment mSettingsFragment = GeneratedClassUtils.getInstance(SettingsFragment.class);
//        mSettingsFragment.setDefaultContainerId();
//        mSettingsFragment.setDefaultAnimations();
//        mSettingsFragment.setTag(mSettingsFragment.getClass().getCanonicalName());
//        mSettingsFragment.setManager(activity.getSupportFragmentManager());
//        mSettingsFragment.show(SHOW_AND_HIDE);//SHOW_ADD_HIDE
//    }

    public void gotoSettings(Context context) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        //MyIntent.startActivity(context, SettingsActivity.class, null);
    }


    /**
     * 跳转至帮助中心
     *
     * @param activity
     */
    public void gotoHelpCenter(FragmentActivity activity, String buyNoticeUrl, int needRefresh) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, activity.getResources().getString(R.string.mine_help_center));
        dataMap.put(WebViewActivity.URL, buyNoticeUrl);
        dataMap.put(WebViewActivity.NEED_REFRESH, needRefresh + "");
        //MyIntent.startActivity(activity, HelpCenterActivity.class, dataMap);
    }

  /*  public void gotoChangeAgent(FragmentActivity activity) {
        ManyiAnalysis.getInstance().onEvent("mine_mine_changeperson");
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        ChangeAgentFragment changeAgentFragment = GeneratedClassUtils.getInstance(ChangeAgentFragment.class);
        changeAgentFragment.setDefaultContainerId();
        changeAgentFragment.setDefaultAnimations();
        changeAgentFragment.setTag(ChangeAgentFragment.class.getCanonicalName());
        changeAgentFragment.setManager(activity.getSupportFragmentManager());
        changeAgentFragment.show(SHOW_AND_HIDE);//SHOW_ADD_HIDE
    }
*/

    /**
     * 我的投诉
     *
     * @param context
     */
    public void gotoMyComplain(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, ComplainListActivity.class);
//                context.startActivity(intent);
            }
        }
    }

    /**
     * 我的经纪人
     *
     * @param context
     */
    public void gotoMyConsultantList(Context context) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        //MyIntent.startActivity(context, ConsultantListActivity.class, null);
    }

    /**
     * 我委托的房源
     *
     * @param context
     */
    public void gotoEntrustManageList(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, EntrustManagerListActivity.class);
//                context.startActivity(intent);
            }
        }
    }

    /**
     * 我委托的房源，并跳转指定的委托详情页
     *
     * @param context
     */
//    public void gotoEntrustManageList(Context context, SubmitHouseInfo submitHouseInfo) {
//        Intent intent = new Intent(context, EntrustManagerListActivity.class);
//        intent.putExtra("SubmitHouseInfo", submitHouseInfo);
//        context.startActivity(intent);
//    }

    /**
     * 银行卡
     *
     * @param context
     */
    public void gotoMyBankCrad(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                //MyIntent.startActivity(context, BankCardListActivity.class, "");
            }
        }
    }

    /**
     * 交易密码
     *
     * @param context
     */
    public void gotoPassWordManage(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                //MyIntent.startActivity(context, PayPwdManageActivity.class, "");
            }
        }
    }

    /**
     * 我的关注页面
     *
     * @param context
     */
    public void gotoAttentionList(Context context) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        ////MyIntent.startActivity(context, GeneratedClassUtils.getInstance(AttentionActivity.class).getClass(), new HashMap<String, String>());
        //MyIntent.startActivity(context, HouseAttentionActivity.class, null);
        ////MyIntent.startActivity(context, ModularAttentionActivity.class, null);
    }

    /**
     * 购房须知
     *
     * @param activity
     */
    public void goPurchaseNotice(FragmentActivity activity, String buyNoticeUrl, int needRefresh) {
//        Map<String, String> dataMap = ObjectUtil.newHashMap();
//        dataMap.put(WebViewActivity.TITLE, activity.getResources().getString(R.string.purchase_notice_text));
//        dataMap.put(WebViewActivity.URL, buyNoticeUrl);
//        dataMap.put(WebViewActivity.NEED_REFRESH, needRefresh + "");
//        MyIntent.startActivity(activity, WebViewActivity.class, dataMap);
    }

    /**
     * 购房百科
     *
     * @param activity
     */
    public static void goEncyclopedias(Activity activity, String buyNoticeUrl, int needRefresh) {
//        Intent intent = new Intent(activity, PurchaseNoticeActivity.class);
//        intent.putExtra(PurchaseNoticeActivity.URL, buyNoticeUrl);
//        activity.startActivity(intent);
    }

  /*  public void gotoMyCollect(FragmentActivity activity, Fragment fragment, int mCollectNum) {
        ManyiAnalysis.getInstance().onEvent("mine_mine_collect");
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (mCollectNum == 0) return;
        Bundle bundle = new Bundle();
        bundle.putInt("NUMBER", mCollectNum);
        MyCollectFragment myCollectFragment = GeneratedClassUtils.getInstance(MyCollectFragment.class);
        myCollectFragment.setDefaultAnimations();
        myCollectFragment.setTargetFragment(fragment, 1);
        myCollectFragment.setArguments(bundle);
        myCollectFragment.setTag("MY_COLLECT");
        myCollectFragment.setManager(activity.getSupportFragmentManager());
        myCollectFragment.setDefaultContainerId();
        myCollectFragment.show(SHOW_AND_HIDE);//SHOW_ADD_HIDE
    }*/

    public void callPhone(Activity activity) {
        ManyiAnalysis.getInstance().onEvent("mine_mine_service");
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        String serverNumber = UserInfo.getInstance().getServicePhoneNumber();
        //SystemUtil.callPhone(activity, serverNumber);
    }

    /**
     * 卡券
     */
    public void gotoCardCoupon(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                //TODO:跳转至红包列表页面
                //MyIntent.startActivity(context, CouponWebViewActivity.class, null);
            }
        }
    }

    /**
     * 约看清单
     */
    public void gotoCartList(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                //MyIntent.startActivity(context, CartListActivity.class, null);
            }
        }
    }

    public static String IS_FROM_SETIING = "fromSetting";

    /**
     * 看房日程
     */
    public void gotoAgendaList(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(IS_FROM_SETIING,"1");
                ////MyIntent.startActivity(context, AgendaTabActivity.class, dataMap);
                //MyIntent.startActivity(context, AgendaCardListActivity.class, dataMap);
            }
        }
    }

    /**
     * 我的房产宝
     *
     * @param context
     */
    public void gotoMyRegular(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, CapitalActivity.class);
//                context.startActivity(intent);
            }
        }
    }

    /***
     * 我的资产
     * @param context
     */
    public void gotoMyAssets(Context context){
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, MyAssetsActivity.class);
//                context.startActivity(intent);
            }
        }
    }

    /**
     * 爱理财首页
     *
     * @param context
     */
    public void gotoFinanceHome(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, FinanceHomeActivity.class);
//                context.startActivity(intent);
            }
        }
    }

    /**
     * 品牌公寓约看记录
     *
     * @param context
     */
    public void gotoSeeRecord(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
//                Intent intent = new Intent(context, FlatSeeRecordActivity.class);
//                context.startActivity(intent);
            }
        }
    }

}
