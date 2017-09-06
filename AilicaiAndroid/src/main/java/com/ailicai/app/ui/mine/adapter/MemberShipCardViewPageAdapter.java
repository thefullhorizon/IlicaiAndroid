package com.ailicai.app.ui.mine.adapter;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.bean.MemberLevel;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;

import java.util.List;
import java.util.Map;

/**
 * name: MemberShipCardViewPageAdapter <BR>
 * description: 会员中心-等级卡片 <BR>
 * create date: 2017/9/5
 *
 * @author: IWJW Zhou Xuan
 */
public class MemberShipCardViewPageAdapter extends PagerAdapter {

    List<MemberLevel> levels = null;
    private FragmentActivity mActivity;

    public MemberShipCardViewPageAdapter(FragmentActivity activity, List<MemberLevel> levels) {
        super();
        this.mActivity = activity;
        this.levels = levels;
    }

    @Override
    public int getCount() {
        if (levels == null || levels.isEmpty()) {
            return 0;
        }
        return levels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.member_ship_card,null);
        container.addView(view);
        setItemData(view,position);
        setRealCardLayoutParams(view);
        return view;
    }

    private void setItemData(View view,int position) {

        MemberLevel level = levels.get(position);

        setLevelBg(view,position);

        TextView tvTitleDesc = (TextView) view.findViewById(R.id.tvTitleDesc);
        tvTitleDesc.setText(level.getTitleDesc());

        TextView tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        LinearLayout llSeeMemberRule = (LinearLayout) view.findViewById(R.id.llSeeMemberRule);
        tvLevel.setText(level.getName());
        tvLevel.setTypeface(getVipNumberFont());

        TextView tvValidLevelTill = (TextView) view.findViewById(R.id.tvValidLevelTill);
        tvValidLevelTill.setText(level.getValidLevelTill());

        llSeeMemberRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.TITLE, "会员规则");
                dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getMemberLevleH5Url());
                dataMap.put(WebViewActivity.NEED_REFRESH, "0");
                dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
                MyIntent.startActivity(mActivity, WebViewActivity.class, dataMap);
            }
        });
    }

    private void setLevelBg(View view,int position) {
        MemberLevel level = levels.get(position);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.llContent);
        if("V0".equals(level.getName())) {
            llContent.setBackgroundResource(R.drawable.card_v_0);
        } else if("V1".equals(level.getName())) {
            llContent.setBackgroundResource(R.drawable.card_v_1);
        } else if("V2".equals(level.getName())) {
            llContent.setBackgroundResource(R.drawable.card_v_2);
        } else if("V3".equals(level.getName())) {
            llContent.setBackgroundResource(R.drawable.card_v_3);
        }
    }

    private void setRealCardLayoutParams(View view) {
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.llContent);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llContent.getLayoutParams();
        int[] screenSize = DeviceUtil.getScreenSize();
        int screenWidth = screenSize[0];
        int cardWidth = screenWidth- UIUtils.dipToPx(mActivity,64);
        layoutParams.width = cardWidth;
        layoutParams.height = cardWidth * 328 / 622;
        llContent.setLayoutParams(layoutParams);
    }

    public Typeface getVipNumberFont() {
        return  Typeface.createFromAsset(mActivity.getAssets(), "fonts/iwlicai_vip_number_regular.ttf");
    }
}
