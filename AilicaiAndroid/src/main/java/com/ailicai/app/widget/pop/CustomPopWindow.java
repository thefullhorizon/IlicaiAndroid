package com.ailicai.app.widget.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ailicai.app.R;

import java.lang.reflect.Method;

/**
 * Created by Owen on 2016/1/19
 */
public abstract class CustomPopWindow {

    private static PopupWindow mPopupWindow = null;

    public static void showPopWindow(final Context context, final CustomPopWindowInterface mInterface, CustomPopWindowParams params) {
        if(params.getCustomWidth() > 0) {
            mPopupWindow = new PopupWindow(params.getCustomWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow = new PopupWindow(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        // 自定义宽度的摸旁边activity不想接收事件
        if(params.getCustomWidth() > 0) {
            setPopupWindowTouchModal(mPopupWindow, true);
        } else {
            setPopupWindowTouchModal(mPopupWindow, false);
        }
        Drawable dw = context.getResources().getDrawable(R.color.transparent);
        mPopupWindow.setBackgroundDrawable(dw);

        if (params.getAnimLocation() == 1) {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnLeft);
        } else if (params.getAnimLocation() == 2) {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnRight);
        } else {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnMiddle);
        }
        View popView = initPopViewAndSetData(context, mInterface, params);
        mPopupWindow.setContentView(popView);
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(mInterface.popShowAsLocation(), 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setPopBackgroundAlpha((Activity) context, 1f);
                mInterface.popDismissListener();
            }
        });


    }

    public static void showPopWindowWithAView(final Context context, final CustomPopWindowInterface mInterface, CustomPopWindowParams params,final View view) {
        if(params.getCustomWidth() > 0) {
            mPopupWindow = new PopupWindow(params.getCustomWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow = new PopupWindow(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        // 自定义宽度的摸旁边activity不想接收事件
        if(params.getCustomWidth() > 0) {
            setPopupWindowTouchModal(mPopupWindow, true);
        } else {
            setPopupWindowTouchModal(mPopupWindow, false);
        }
        Drawable dw = context.getResources().getDrawable(R.color.transparent);
        mPopupWindow.setBackgroundDrawable(dw);

        if (params.getAnimLocation() == 1) {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnLeft);
        } else if (params.getAnimLocation() == 2) {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnRight);
        } else {
            mPopupWindow.setAnimationStyle(R.style.CustomPopAnimOnMiddle);
        }
        View popView = initPopViewAndSetData(context, mInterface, params);
        mPopupWindow.setContentView(popView);
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(mInterface.popShowAsLocation(), 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setPopBackgroundAlpha((Activity) context, 1f);
                mInterface.popDismissListener();
                view.setVisibility(View.INVISIBLE);
            }
        });
        view.setVisibility(View.VISIBLE);


    }


    private static View initPopViewAndSetData(Context context, final CustomPopWindowInterface mInterface, CustomPopWindowParams params) {
        View view = null;
        ListView listView = null;
        if (params.isStyle()) {
//            setPopBackgroundAlpha((Activity) context, 0.7f);

            view = View.inflate(context, R.layout.pop_custom_view2, null);
            listView = (ListView) view.findViewById(R.id.listView);

            CustomPopWindowStyleAdapter styleAdapter = new CustomPopWindowStyleAdapter(context, mInterface);
            styleAdapter.addAllData(mInterface.popListData());
            listView.setAdapter(styleAdapter);
            listView.setSelection(params.getSelection());
            styleAdapter.setCurrentListType(params.getWhichOne());
        } else {
            view = View.inflate(context, R.layout.pop_custom_view, null);
            listView = (ListView) view.findViewById(R.id.listView);

            CustomPopWindowAdapter adapter;
            if(params.getCustomWidth() > 0) {
                adapter = new CustomPopWindowAdapter(context, mInterface,true);
            } else {
                adapter = new CustomPopWindowAdapter(context, mInterface,false);
            }

            adapter.addAllData(mInterface.popListData());
            listView.setAdapter(adapter);
            listView.setSelection(params.getSelection());
            adapter.setCurrentListType(params.getWhichOne());
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            listView.setSelector(R.drawable.bg_black_a10_selector);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                mInterface.popOnItemClickListener(position, mInterface.popListData().get(position));
            }
        });

        if (params.getWhichOne() == 1) {
            view.findViewById(R.id.triangleOne).setVisibility(View.VISIBLE);
        } else if (params.getWhichOne() == 2) {
            view.findViewById(R.id.triangleTwo).setVisibility(View.VISIBLE);
        } else if (params.getWhichOne() == 3) {
            view.findViewById(R.id.triangleThree).setVisibility(View.VISIBLE);
        } else if (params.getWhichOne() == 4) {
            view.findViewById(R.id.triangleFour).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.llTriangle).setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        int times5ItemHeight = 5 * context.getResources().getDimensionPixelOffset(R.dimen._46) + context.getResources().getDimensionPixelOffset(R.dimen._46) / 2;
        if (mInterface.popListData().size() > 5) {
            layoutParams.height = times5ItemHeight;
            listView.setLayoutParams(layoutParams);
        }
        return view;
    }

    /**
     * 通过反射设置PopupWindow的Flag，让事件能传递到下面的Activity
     */
    private static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setPopBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    private static long lastClickTime = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setNull() {
        mPopupWindow = null;
    }

}
