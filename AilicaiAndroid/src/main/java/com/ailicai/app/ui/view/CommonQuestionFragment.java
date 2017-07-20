package com.ailicai.app.ui.view;

import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseActivity;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.FoldingTextLayout;

import butterknife.Bind;

/**
 * 常见问题
 * Created by liyanan on 16/4/7.
 */
public class CommonQuestionFragment extends BaseBindFragment {
    @Bind(R.id.fl_1)
    FoldingTextLayout fl1;
    @Bind(R.id.fl_2)
    FoldingTextLayout fl2;
    @Bind(R.id.fl_3)
    FoldingTextLayout fl3;
    @Bind(R.id.fl_4)
    FoldingTextLayout fl4;
    @Bind(R.id.fl_5)
    FoldingTextLayout fl5;
    @Bind(R.id.fl_6)
    FoldingTextLayout fl6;
    @Bind(R.id.fl_7)
    FoldingTextLayout fl7;
    private FoldingTextLayout currentShowTextLayout;

    @Override
    public int getLayout() {
        return R.layout.fragment_common_question;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initFoldingText();
    }

    private void initFoldingText() {
        fl1.initData(getString(R.string.common_question_title_1), getString(R.string.common_question_answer_1), null);
        fl2.initData(getString(R.string.common_question_title_2), getString(R.string.common_question_answer_2), null);
        fl3.initData(getString(R.string.common_question_title_3), getString(R.string.common_question_answer_3), null);
        fl4.initData(getString(R.string.common_question_title_4), getString(R.string.common_question_answer_4), null);
        fl5.initData(getString(R.string.common_question_title_5), getString(R.string.common_question_answer_5), null);
        fl6.initData(getString(R.string.common_question_title_6), getString(R.string.common_question_answer_6), null);
        fl7.initData(getString(R.string.common_question_title_7), getString(R.string.common_question_answer_7), new FoldingTextLayout.ServiceCallClickListener() {
            @Override
            public void serviceCallClick() {
                BaseActivity activity = (BaseActivity) getActivity();
                activity.showCallPhoneDialog(UserInfo.getInstance().getServicePhoneNumber());
            }
        });
        addContentShowListener();
    }

    private void addContentShowListener() {
        fl1.setOnContentShowListener(onContentShowListener);
        fl2.setOnContentShowListener(onContentShowListener);
        fl3.setOnContentShowListener(onContentShowListener);
        fl4.setOnContentShowListener(onContentShowListener);
        fl5.setOnContentShowListener(onContentShowListener);
        fl6.setOnContentShowListener(onContentShowListener);
        fl7.setOnContentShowListener(onContentShowListener);
    }

    FoldingTextLayout.OnContentShowListener onContentShowListener = new FoldingTextLayout.OnContentShowListener() {
        @Override
        public void onContentShow(FoldingTextLayout v) {
            if (currentShowTextLayout != null && currentShowTextLayout != v && currentShowTextLayout.isExpand()) {
                currentShowTextLayout.hideContent();
            }
            currentShowTextLayout = v;
        }
    };

}
