package com.ailicai.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.ailicai.app.R;

import java.util.Collection;

public class BottomSheetsDialogBuilder {

    public static BottomSheetDialog showSimpleDialog(Context context, Collection items, final ListView.OnItemClickListener onItemClickListener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppDesign_BottomSheetDialog);
        View contentView = View.inflate(context, R.layout.bottom_sheet_comm_list_layout, null);
        ListView listView = (ListView) contentView.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent, view, position, id);
                bottomSheetDialog.dismiss();
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.bottom_sheet_comm_list_item, R.id.item_text);
        arrayAdapter.addAll(items);
        listView.setAdapter(arrayAdapter);
        return setBottomSheetDialog(bottomSheetDialog, contentView);
    }

    public static BottomSheetDialog showSimpleDialog(Context context, String[] items, final ListView.OnItemClickListener onItemClickListener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppDesign_BottomSheetDialog);
        View contentView = View.inflate(context, R.layout.bottom_sheet_comm_list_layout, null);
        ListView listView = (ListView) contentView.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent, view, position, id);
                bottomSheetDialog.dismiss();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.bottom_sheet_comm_list_item, R.id.item_text);
        arrayAdapter.addAll(items);
        listView.setAdapter(arrayAdapter);
        return setBottomSheetDialog(bottomSheetDialog, contentView);
    }


    /**
     * @param context
     * @param contentView 必须继承BottomSheetsCustomView ,为了相关View能灵活和BottomSheetDialog交互
     * @return
     */
    public static BottomSheetDialog showCustomeDialog(Context context, BottomSheetsCustomView contentView) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppDesign_BottomSheetDialog);
        contentView.setDimess(new IBottomSheetEvent() {
            @Override
            public void onDismiss() {
                bottomSheetDialog.dismiss();
            }
        });
        return setBottomSheetDialog(bottomSheetDialog, contentView);
    }

    /**
     * BottomSheetDialog 对外提供相关交互的事件
     */
    public interface IBottomSheetEvent {
        void onDismiss();
    }

    private static BottomSheetDialog setBottomSheetDialog(BottomSheetDialog bottomSheetDialog, View contentView) {
        bottomSheetDialog.setContentView(contentView);
        // bottomSheetDialog.setCanceledOnTouchOutside(false);
        View parent = (View) contentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
     /*   contentView.measure(0, 0);
        behavior.setPeekHeight(contentView.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);*/
        bottomSheetDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        bottomSheetDialog.show();

        return bottomSheetDialog;
    }


    /**
     * 指定的BottomSheet的基础View
     */
    public static class BottomSheetsCustomView extends FrameLayout {
        public BottomSheetsCustomView(Context context) {
            super(context);
        }

        public BottomSheetsCustomView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BottomSheetsCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public BottomSheetsCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public IBottomSheetEvent iBottomSheetEvent;

        public void setDimess(IBottomSheetEvent iBottomSheetEvent) {
            this.iBottomSheetEvent = iBottomSheetEvent;
        }
    }


}
