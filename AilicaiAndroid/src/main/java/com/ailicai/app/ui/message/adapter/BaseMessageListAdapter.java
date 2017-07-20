package com.ailicai.app.ui.message.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.message.Notice;
import com.ailicai.app.ui.message.MessageTypeProcessUtils;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.imageloader.core.LoadParam;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by duo.chen on 2015/8/7`
 */
public class BaseMessageListAdapter extends BaseAdapter {

    private WeakReference<Activity> context;
    private List<Notice> data = new ArrayList<>();
    private int messageType;
    private OndeleteListener OndeleteListener;
    LoadParam loadParam;

    public void setOndeleteListener(OndeleteListener ondeleteListener) {
        this.OndeleteListener = ondeleteListener;
    }

    private Activity getContext() {
        return context.get();
    }

    public BaseMessageListAdapter(Activity context, int messageType) {
        this.context = new WeakReference<>(context);
        this.messageType = messageType;
        loadParam = new LoadParam();
    }

    public void setListData(List<Notice> data,boolean reload) {
        if(data == null){
            return;
        }
        if(reload){
            this.data = data;
        }else{
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }
    public void deleteListData(int pos){
        data.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Notice getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null != getContext()) {
            Notice notice = data.get(position);
            int remindType = notice.getRemindType();

            switch (messageType) {
                case PushMessage.REMINDTYPE:
                    final ViewHolderRemind viewHolderRemind;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_remind_item_layout, null);
                        viewHolderRemind = new ViewHolderRemind(convertView);
                        convertView.setTag(viewHolderRemind);
                    } else {
                        viewHolderRemind = (ViewHolderRemind) convertView.getTag();
                    }
                    viewHolderRemind.setPosition(position);

                    showRemindView(viewHolderRemind, remindType, notice);
                    break;

                case PushMessage.INFOTYPE:
                case PushMessage.ACTIVITYTYPE:
                    final ViewHolderInfo viewHolderInfo;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout
                                .message_infomation_item_layout, null);
                        viewHolderInfo = new ViewHolderInfo(convertView);
                        convertView.setTag(viewHolderInfo);
                    } else {
                        viewHolderInfo = (ViewHolderInfo) convertView.getTag();
                    }

                    viewHolderInfo.setPosition(position);

                    showInfoView(viewHolderInfo, notice);
                    break;
            }
        }

        return convertView;
    }


    private void showRemindView(ViewHolderRemind viewHolderRemind, int remindType,Notice notice) {

        if (!TextUtils.isEmpty(notice.getTitle())) {
            viewHolderRemind.messageTitle.setText(notice.getTitle());
        }

        if (!TextUtils.isEmpty(notice.getCreateDate())) {
            if (notice.getReadStatus() == 0) {
                viewHolderRemind.messageTimeNewLayout.setVisibility(View.VISIBLE);
                viewHolderRemind.messageTimeNormal.setVisibility(View.GONE);
                viewHolderRemind.messageTimeNew.setText(notice.getCreateDate());
            } else {
                viewHolderRemind.messageTimeNewLayout.setVisibility(View.GONE);
                viewHolderRemind.messageTimeNormal.setVisibility(View.VISIBLE);
                viewHolderRemind.messageTimeNormal.setText(notice.getCreateDate());
            }
        }

        if (!TextUtils.isEmpty(notice.getContent())) {
            viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
            viewHolderRemind.messageRemindContent.setText(notice.getContent());
            viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
        }
        viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);
        viewHolderRemind.messageViewDetailText.setText("查看详情");
         if (remindType == PushMessage.REMINDTYPENEWVOUCHER
                || remindType == PushMessage.REMINDTYPETIYANJI) {
            //新的现金券通知
            viewHolderRemind.iconLeft.setText(R.string.mine_ticket);
        }else if (remindType == PushMessage.REMINDTYPERESERVESUCCESS) {

            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
        } else if (remindType == PushMessage.REMINDTYPERESERVEFAIL ||
                remindType == PushMessage.REMINDTYPELIUBIAO) {

            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
            if (remindType == PushMessage.REMINDTYPERESERVEFAIL) {
                viewHolderRemind.messageViewDetailText.setText("重新购买");
            } else {
                viewHolderRemind.messageViewDetailText.setText("查看其它产品");
            }
        }else if (remindType == PushMessage.REMINDTYPEHUANKUAN
                ||remindType == PushMessage.REMINDTYPEZHUANRANG
                || remindType == PushMessage.REMINDTYPEMUZIJIXI) {
            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
        } else if (remindType == PushMessage.REMINDTYPETYJTOFIHOME) {
            viewHolderRemind.iconLeft.setText(R.string.mine_ticket);
        }else if (remindType == PushMessage.REMINDTYPEBANKRECEIPTFAIL) {
            viewHolderRemind.iconLeft.setText(R.string.personal_wallet);
        } else {
            viewHolderRemind.iconLeft.setText(R.string.remind_default);
        }
    }

    private void showInfoView(ViewHolderInfo viewHolderInfo,Notice
            notice) {

        if (!TextUtils.isEmpty(notice.getTitle())) {
            viewHolderInfo.messageTitle.setText(notice.getTitle());
        }

        if (!TextUtils.isEmpty(notice.getCreateDate())) {
            if (notice.getReadStatus() == 0) {
                viewHolderInfo.messageTimeNewLayout.setVisibility(View.VISIBLE);
                viewHolderInfo.messageTimeNormal.setVisibility(View.GONE);
                viewHolderInfo.messageTimeNew.setText(notice.getCreateDate());
            } else {
                viewHolderInfo.messageTimeNewLayout.setVisibility(View.GONE);
                viewHolderInfo.messageTimeNormal.setVisibility(View.VISIBLE);
                viewHolderInfo.messageTimeNormal.setText(notice.getCreateDate());
            }
        }
        if (!TextUtils.isEmpty(notice.getContent())) {
            viewHolderInfo.messageContent.setText(StringUtil.ToDBC(notice.getContent()));
        }

        if (TextUtils.isEmpty(notice.getPicUrl())) {
            viewHolderInfo.messageImg.setVisibility(View.GONE);
        } else {
            viewHolderInfo.messageImg.setVisibility(View.VISIBLE);
            loadParam.setImgUri(notice.getPicUrl());
            ImageLoaderClient.display(getContext(), viewHolderInfo.messageImg, loadParam);
        }

        viewHolderInfo.messageViewDetail.setVisibility(View.VISIBLE);
    }

    //提醒类型的消息
    class ViewHolderRemind extends BaseViewHolder{
        @Bind(R.id.message_icon)
        TextViewTF iconLeft;
        @Bind(R.id.message_date)
        TextView messageDate;
        @Bind(R.id.message_position)
        TextView messagePosition;
        @Bind(R.id.message_remind_content)
        TextView messageRemindContent;

        @Bind(R.id.message_remind_schedule)
        View messageRemindSch;
        @Bind(R.id.message_view_detail_text)
        TextView messageViewDetailText;

        ViewHolderRemind(View view) {
            super(view);
        }

    }

    //资讯类型的消息
    class ViewHolderInfo extends BaseViewHolder{

        @Bind(R.id.message_content_image)
        ImageView messageImg;

        @Bind(R.id.message_content)
        TextView messageContent;

        ViewHolderInfo(View view) {
            super(view);

            int nScreenWidth = DeviceUtil.getScreenSize()[0];
            LinearLayout.LayoutParams relativeLayout = (LinearLayout.LayoutParams) messageImg
                    .getLayoutParams();
            int width = nScreenWidth - MyApplication.getInstance().getResources()
                    .getDimensionPixelOffset(R.dimen._16) * 2;
            double imgHight = 0.4 * width;
            relativeLayout.height = (int) imgHight;
            messageImg.setLayoutParams(relativeLayout);

        }
    }

    class BaseViewHolder implements View.OnClickListener{

        @Bind(R.id.message_title)
        TextView messageTitle;
        @Bind(R.id.message_delete)
        TextViewTF messageDel;
        @Bind(R.id.message_time)
        TextView messageTimeNormal;
        @Bind(R.id.message_time_new)
        TextView messageTimeNew;
        @Bind(R.id.message_time_new_layout)
        LinearLayout messageTimeNewLayout;
        @Bind(R.id.message_view_detail)
        View messageViewDetail;
        View view;
        int position;

        public View getView() {
            return view;
        }

        public void setPosition(int position){
            this.position = position;
        }

        private BaseViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);

            messageDel.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int vId = v.getId();
            switch (vId){
                case R.id.message_delete:
                    DialogBuilder.showSimpleDialog(getContext(), "您确定要删除吗？", null, "取消", null, "确定", new
                            DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OndeleteListener.onDeleteItem(position);
                                }
                            });
                    break;
                default:
                    MessageTypeProcessUtils.processMessageListActivityItemClick(getContext(),messageType,getItem(position));
                    break;
            }
        }
    }

    public interface OndeleteListener {
        void onDeleteItem(int position);
    }
}
