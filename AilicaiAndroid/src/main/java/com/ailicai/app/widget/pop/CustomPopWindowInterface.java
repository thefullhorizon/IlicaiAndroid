package com.ailicai.app.widget.pop;

import android.view.View;

import java.util.List;

/**
 * Created by Owen on 2016/1/18
 */
public interface CustomPopWindowInterface {

    List<CustomPopWindowBean> popListData();

    View popShowAsLocation();

    void popOnItemClickListener(int position, CustomPopWindowBean bean);

    void popDismissListener();

    boolean checkIsSelect(CustomPopWindowBean bean);

}
