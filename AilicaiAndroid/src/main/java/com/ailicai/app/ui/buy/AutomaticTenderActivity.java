package com.ailicai.app.ui.buy;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseMvpActivity;

/**
 * 自动投标页面
 * Created by jeme on 2017/8/18.
 */
public class AutomaticTenderActivity extends BaseMvpActivity<AutomaticTenderPresenter>{

    @Override
    public AutomaticTenderPresenter initPresenter() {
        return new AutomaticTenderPresenter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_automatic_tender;
    }
}
