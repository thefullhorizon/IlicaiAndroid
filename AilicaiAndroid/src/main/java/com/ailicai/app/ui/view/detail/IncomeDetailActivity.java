package com.ailicai.app.ui.view.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.IncomeDetailParentFragment;
import com.ailicai.app.ui.view.IncomeDetailWalletFragment;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.pop.CustomPopWindow;
import com.ailicai.app.widget.pop.CustomPopWindowBean;
import com.ailicai.app.widget.pop.CustomPopWindowInterface;
import com.ailicai.app.widget.pop.CustomPopWindowParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Owen on 16/8/10
 */
public class IncomeDetailActivity extends BaseBindActivity implements CustomPopWindowInterface, IWTopTitleView.TopTitleOnClickListener {

    public static final String TYPE = "type";
    public static final int WALLET = 0;
    public static final int REGULAR = 1;
    public static final int TRY = 2;

    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvIcon)
    TextView tvIcon;

    private String[] valueArray = {"活期宝", "网贷资产"};

    // 正在显示的fragment
    Fragment lastFragment;

    private int typeValue;
    public int changeValue;

    @Override
    public int getLayout() {
        return R.layout.activity_income_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getIntentValue();
        switchFragment();
    }

    public void getIntentValue() {
        typeValue = getIntent().getIntExtra(TYPE, 0);
        if (typeValue == WALLET) {
            tvTitle.setText(valueArray[0]);
        } else {
            tvTitle.setText(valueArray[1]);
        }
    }

    private void switchFragment() {
        // 如果未添加则添加
        if (!getFragmetWithTag().isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.main_container, getFragmetWithTag(), getFragmetWithTag().getClass().getName());
            transaction.commitAllowingStateLoss();
        }

        if (lastFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(lastFragment)
                    .commitAllowingStateLoss();
        }
        getSupportFragmentManager().beginTransaction().show(getFragmetWithTag())
                .commitAllowingStateLoss();

        lastFragment = getFragmetWithTag();
    }

    private Fragment getFragmetWithTag() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(typeValue));
        if (fragment == null) {
            switch (typeValue) {

                case WALLET:
                    fragment = new IncomeDetailWalletFragment();
                    break;
                case REGULAR:
                case TRY:
                    fragment = new IncomeDetailParentFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(IncomeDetailParentFragment.TAG_TYPE, typeValue == REGULAR ? IncomeDetailParentFragment.TAG_REGULAR : IncomeDetailParentFragment.TAG_TRY);
                    fragment.setArguments(bundle2);
                    break;
                default:
                    break;

            }
        }
        return fragment;
    }

    @OnClick(R.id.llTitle)
    void titleClick() {
        if (CustomPopWindow.isFastDoubleClick()) {
            return;
        }
        tvIcon.setText(getResources().getString(R.string.area));
        CustomPopWindowParams params = new CustomPopWindowParams();
        params.setStyle(true);
        CustomPopWindow.showPopWindow(this, this, params);
    }

    @Override
    public List<CustomPopWindowBean> popListData() {
        List<CustomPopWindowBean> list = new ArrayList<>();
        for (int i = 0; i < valueArray.length; i++) {
            CustomPopWindowBean bean = new CustomPopWindowBean();
            bean.setId(i);
            bean.setTitleName(valueArray[i]);
            list.add(bean);
        }
        return list;
    }

    @Override
    public View popShowAsLocation() {
        return llTitle;
    }

    @Override
    public void popOnItemClickListener(int position, CustomPopWindowBean bean) {
        tvTitle.setText(bean.getTitleName());
        if (position == 0) {
            typeValue = WALLET;
        } else {
            if (changeValue == 0) {
                typeValue = REGULAR;
            } else {
                typeValue = TRY;
            }
        }
        switchFragment();
    }

    @Override
    public void popDismissListener() {
        tvIcon.setText(getResources().getString(R.string.homepage_arrow_left));
    }

    @Override
    public boolean checkIsSelect(CustomPopWindowBean bean) {
        String typeName = tvTitle.getText().toString();
        return typeName.equals(bean.getTitleName());
    }

    @Override
    public boolean onBackClick() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomPopWindow.setNull();
    }
}
