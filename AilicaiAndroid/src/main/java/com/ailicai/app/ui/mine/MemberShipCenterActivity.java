package com.ailicai.app.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.bean.MemberLevel;
import com.ailicai.app.model.bean.Reward;
import com.ailicai.app.model.response.MemberInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.mine.adapter.CustPagerTransformer;
import com.ailicai.app.ui.mine.adapter.MemberShipCardViewPageAdapter;
import com.ailicai.app.ui.mine.presenter.MemberShipCenterPersenter;
import com.huoqiu.framework.imageloader.core.LoadParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * name: MemberShipCenterActivity <BR>
 * description: 会员中心 <BR>
 * create date: 2017/9/5
 *
 * @author: IWJW zhouxuan
 */
public class MemberShipCenterActivity extends BaseBindActivity {

    MemberShipCenterPersenter persenter;
    MemberInfoResponse response;

    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvCurrentLevel)
    TextView tvCurrentLevel;
    @Bind(R.id.pbMyPoint)
    ProgressBar pbMyPoint;
    @Bind(R.id.tvCurrentPoint)
    TextView tvCurrentPoint;
    @Bind(R.id.tvNextLevelPoint)
    TextView tvNextLevelPoint;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.llBg)
    LinearLayout llBg;
    @Bind(R.id.levelFragmentContainer)
    LinearLayout levelFragmentContainer;
    @Bind(R.id.tvLevelRewardTitle)
    TextView tvLevelRewardTitle;

    @Override
    public int getLayout() {
        return R.layout.activity_membership_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        persenter = new MemberShipCenterPersenter(this);
        persenter.httpForMemberShipInfo();
    }

    public void bindData(MemberInfoResponse response) {
        this.response = response;
        setTopMemberInfo();
        switchResponse(response);
        bindViewPageData(response.getLevels());
    }

    private void setTopMemberInfo() {
        pbMyPoint.setMax(response.getNextLevelScore());
        pbMyPoint.setProgress(response.getScore());
        tvUserName.setText(UserInfo.getInstance().getmName());
        tvCurrentLevel.setText(response.getMemberLevel().getName());
        setLevelTagBgColor(response);
        tvCurrentLevel.setTypeface(getVipNumberFont());
        tvCurrentPoint.setText(response.getScore()+"");
        tvNextLevelPoint.setText("/"+response.getNextLevelScore()+"");
    }

    private void setLevelTagBgColor(MemberInfoResponse response) {
        if("V0".equalsIgnoreCase(response.getMemberLevel().getName())) {
            tvCurrentLevel.setBackgroundResource(R.drawable.solid_db9c68_radius_360dp);
        } else if("V1".equalsIgnoreCase(response.getMemberLevel().getName())) {
            tvCurrentLevel.setBackgroundResource(R.drawable.solid_7d8194_radius_360dp);
        } else if("V2".equalsIgnoreCase(response.getMemberLevel().getName())) {
            tvCurrentLevel.setBackgroundResource(R.drawable.solid_4f5869_radius_360dp);
        } else if("V3".equalsIgnoreCase(response.getMemberLevel().getName())) {
            tvCurrentLevel.setBackgroundResource(R.drawable.solid_b1985e_radius_360dp);
        }
    }

    // 记录当前用的等级
    int myCurrentPosition = 0;
    // 获得哪个是当前等级，给title赋值，给当前等级有效期赋值
    private void switchResponse(MemberInfoResponse response) {

        int i = 0;
        List<MemberLevel> levels = response.getLevels();
        MemberLevel currentLevel = response.getMemberLevel();
        for(MemberLevel level:levels) {
            if(level.equals(currentLevel)) {
                level.setValidLevelTill(response.getValidLevelTill());
                myCurrentPosition = i;
            }
            i++;
            if(level.getName().compareTo(currentLevel.getName()) < 0) {
                level.setTitleDesc("会员等级");
            } else if(level.getName().compareTo(currentLevel.getName()) == 0) {
                level.setTitleDesc("当前会员等级");
            } else {
                level.setTitleDesc("未达到会员等级");
            }
        }
    }

    public void bindViewPageData(final List<MemberLevel> levels) {

        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));

        MemberShipCardViewPageAdapter adapter = new MemberShipCardViewPageAdapter(this,levels);
        viewPager.setAdapter(adapter);

        // 初始化设置
        tvLevelRewardTitle.setText(levels.get(0).getName()+"会员特权");
        setRewardItemForLevel(levels.get(0).getRewards());

        setViewPagerLayoutParams();
        setLLbgLayoutParams();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MemberLevel level = levels.get(position);
                setLevelRewardTitle(level);
                setRewardItemForLevel(level.getRewards());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(myCurrentPosition);
    }

    private void setLLbgLayoutParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llBg.getLayoutParams();
        int[] screenSize = DeviceUtil.getScreenSize();
        int screenWidth = screenSize[0];
        int cardWidth = screenWidth- UIUtils.dipToPx(this,64);
        params.height = cardWidth * 328 / 622;
        llBg.setLayoutParams(params);
    }

    private void setViewPagerLayoutParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        int[] screenSize = DeviceUtil.getScreenSize();
        int screenWidth = screenSize[0];
        int cardWidth = screenWidth- UIUtils.dipToPx(this,64);
        params.height = cardWidth * 328 / 622;
        viewPager.setLayoutParams(params);
    }

    private void setLevelRewardTitle(MemberLevel level) {
        tvLevelRewardTitle.setText(level.getName()+"会员特权");
        if("V0".equalsIgnoreCase(level.getName())) {
            tvLevelRewardTitle.setTextColor(Color.parseColor("#db9c68"));
        } else if("V1".equalsIgnoreCase(level.getName())) {
            tvLevelRewardTitle.setTextColor(Color.parseColor("#7d8194"));
        } else if("V2".equalsIgnoreCase(level.getName())) {
            tvLevelRewardTitle.setTextColor(Color.parseColor("#4f5869"));
        } else if("V3".equalsIgnoreCase(level.getName())) {
            tvLevelRewardTitle.setTextColor(Color.parseColor("#b1985e"));
        }
    }

    private void setRewardItemForLevel(List<Reward> rewards) {
        levelFragmentContainer.removeAllViews();
        if(rewards != null && rewards.size() != 0) {
            for(Reward reward:rewards) {
                View itemView = getLayoutInflater().inflate(R.layout.item_level_reward,null);
                ImageView ivRewardItem = (ImageView) itemView.findViewById(R.id.ivRewardItem);
                TextView tvRewardItemTitle = (TextView) itemView.findViewById(R.id.tvRewardItemTitle);
                TextView tvRewardItemDesc = (TextView) itemView.findViewById(R.id.tvRewardItemDesc);

                LoadParam param = new LoadParam();
                param.setImgUri(reward.getIconUrl());
                ImageLoaderClient.display(this,ivRewardItem,param);

                tvRewardItemTitle.setText(reward.getName());
                tvRewardItemDesc.setText(reward.getMemo());

                if(!TextUtils.isEmpty(reward.getAdditionalInfo())) {

                    String jsonStr = reward.getAdditionalInfo();
                    String url = "";
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        url = jsonObject.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SpannableString spanableInfo = new SpannableString(reward.getMemo() + " " +"查看详情");
                    String desc = reward.getMemo();
                    int start = +desc.length();
                    desc = desc + " " +"查看详情";
                    int end = desc.length();
                    spanableInfo.setSpan(new Clickable(url), start, end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvRewardItemDesc.setText(spanableInfo);
                    //setMovementMethod()该方法必须调用，否则点击事件不响应
                    tvRewardItemDesc.setMovementMethod(LinkMovementMethod.getInstance());

                    avoidHintColor(tvRewardItemDesc);
                }

                levelFragmentContainer.addView(itemView);
            }
        } else {
            View itemView = getLayoutInflater().inflate(R.layout.item_v0_hint,null);
            levelFragmentContainer.addView(itemView);
        }
    }

    @OnClick(R.id.llSeePointDetail)
    void onSeePointDetailClick() {
        Intent intent = new Intent(this,IntegralDetailActivity.class);
        intent.putExtra("score",response.getScore());
        startActivity(intent);
    }

    public Typeface getVipNumberFont() {
        return  Typeface.createFromAsset(getAssets(), "fonts/iwlicai_vip_number_regular.ttf");
    }


    class Clickable extends ClickableSpan {

        String url;

        public Clickable(String url) {
            super();
            this.url = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.bgColor = getResources().getColor(R.color.transparent);
            ds.setColor(Color.parseColor("#005ebd"));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            avoidHintColor(v);
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.URL, url);
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.USEWEBTITLE, "true");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
            MyIntent.startActivity(MemberShipCenterActivity.this, WebViewActivity.class, dataMap);
        }
    }

    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void reloadData() {
        super.reloadData();
        persenter.httpForMemberShipInfo();
    }
}
