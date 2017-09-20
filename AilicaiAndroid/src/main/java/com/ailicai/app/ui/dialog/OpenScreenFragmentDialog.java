package com.ailicai.app.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MainIntentUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.OpenScreenPopUtils;
import com.ailicai.app.model.bean.OpenScreenPopModel;
import com.ailicai.app.ui.base.FragmentHelper;
import com.huoqiu.framework.imageloader.core.LoadParam;

import java.util.List;

import butterknife.Bind;

/**
 * 弹窗页面
 * Created by jeme on 2017/5/25.
 */
public class OpenScreenFragmentDialog extends MyBaseDialog implements View.OnClickListener{
    public static final String IS_FIRST_OPEN = "isFirstOpen_pos_";
    @Bind(R.id.root)
    View mVRoot;
    @Bind(R.id.riv_open_screen)
    ImageView mIvOpenScreen;
    @Bind(R.id.tv_close)
    TextView mTvClose;

    private Activity mContext;
    private OpenScreenPopModel mModel;
    private FragmentHelper mFragmentHelper;

    /***
     * 根据具体的位置信息确定是否弹窗，
     * 并在弹窗成功后删除数据，设置已弹的标志位
     */
    public static void showByPosition(FragmentActivity fragmentActivity, int popPosition){
        List<Long> popIds = OpenScreenPopUtils.getOpenScreenSupportId(popPosition);
        if(popIds == null){
            return;
        }
        String isFirstOpenTag ;
        for(Long popId : popIds){
            isFirstOpenTag = OpenScreenPopUtils.getOpenScreenKey(IS_FIRST_OPEN,popId,popPosition);
            if(MyPreference.getInstance().read(isFirstOpenTag,true)) {
                OpenScreenPopModel model = OpenScreenPopUtils.getOpenScreenSupport(popId,popPosition);
                if (model != null && !TextUtils.isEmpty(model.getAdPopupImgUrl())) {
                    OpenScreenFragmentDialog dialog = new OpenScreenFragmentDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("openScreenPopModel", model);
                    dialog.setArguments(bundle);
                    dialog.show(fragmentActivity.getSupportFragmentManager());
                    MyPreference.getInstance().write(isFirstOpenTag,false);
                    OpenScreenPopUtils.removeOpenScreenSupport(model.getAdPopupId(),model.getPopPosition());
                    //每次只弹一个弹窗
                    break;
                }
            }
        }


    }

    @Override
    public int getTheme() {
        return MYTHEME4;
    }

    @Override
    public int displayWindowLocation() {
        return Gravity.CENTER;
    }

    @Override
    public boolean cancelable() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        mModel = (OpenScreenPopModel)getArguments().getSerializable("openScreenPopModel");
    }

    @Override
    public void setupView(View rootView, Bundle bundle) {
        setDialogSize((DeviceUtil.getScreenSize()[0] * 4) / 5, DeviceUtil.getScreenSize()[0]);

        mIvOpenScreen.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
    }

    private void setDialogSize(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        mVRoot.setLayoutParams(params);
    }


    @Override
    public void setupData(Bundle bundle) {
        if(mModel != null) {
            LoadParam param = new LoadParam();
            param.setEmptyPicId(R.drawable.img_failed);
            param.setFailPicId(R.drawable.img_failed);
            param.setLoadingPicId(R.drawable.img_no);
            ImageLoaderClient.display(mContext, mIvOpenScreen, mModel.getAdPopupImgUrl(), param);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.open_screen_dialog_layout;
    }

    public void show(FragmentManager manager){
        if(mFragmentHelper == null){
            mFragmentHelper = new FragmentHelper(manager);
        }
        mFragmentHelper.showDialogAllowStateLoss(getArguments(), this);

    }

    public void goOpenScreenDetailPage() {
        if(mModel == null){
            return;
        }
        MainIntentUtil.processFunctionClick(getActivity(), mModel);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
            case R.id.riv_open_screen:
                if(mModel == null){
                    return;
                }
                goOpenScreenDetailPage();
                //如果是无跳转，则不退出对话框
                // v7.0所有弹窗都点击后关闭
//                if(mModel.getAdPopupUrlType() != OpenScreenPopModel.POP_TYPE_NONE){
                    dismiss();
//                }
                break;
        }
    }


}
