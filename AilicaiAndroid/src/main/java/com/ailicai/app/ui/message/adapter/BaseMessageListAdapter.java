package com.ailicai.app.ui.message.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.ailicai.app.common.constants.EventStr;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.message.Notice;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.message.MessageDetailWebViewActivity;
import com.ailicai.app.ui.voucher.CouponWebViewActivity;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.TextViewTF;
import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.imageloader.core.LoadParam;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //TODO  默认图片设置
//        loadParam.setEmptyPicId(R.drawable.img_message_no);
//        loadParam.setFailPicId(R.drawable.img_message_no);
//        loadParam.setLoadingPicId(R.drawable.img_message_no);

    }

    public void setListData(List<Notice> data) {
        if (null != data) {
            this.data = data;
        }
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

    public int getMessageType() {
        return messageType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Notice notice = data.get(position);
        final int remindType = notice.getRemindType();
        if (null != getContext()) {
            switch (getMessageType()) {
                case PushMessage.REMINDTYPE:
                    final ViewHolderRemind viewHolderRemind;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_remind_item_layout, null);
                        viewHolderRemind = new ViewHolderRemind(convertView);
                        convertView.setTag(viewHolderRemind);
                    } else {
                        viewHolderRemind = (ViewHolderRemind) convertView.getTag();
                    }

                    viewHolderRemind.messageDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogBuilder.showSimpleDialog(getContext(), "您确定要删除吗？", null, "取消", null, "确定", new
                                    DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OndeleteListener.onDeleteItem(position);
                                        }
                                    });
                        }
                    });
                    showRemindView(viewHolderRemind, remindType, notice);
                    break;

                case PushMessage.INFOTYPE:
                    final ViewHolderInfo viewHolderInfo;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout
                                .message_infomation_item_layout, null);
                        viewHolderInfo = new ViewHolderInfo(convertView);
                        convertView.setTag(viewHolderInfo);
                    } else {
                        viewHolderInfo = (ViewHolderInfo) convertView.getTag();
                    }

                    viewHolderInfo.messageDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogBuilder.showSimpleDialog(getContext(), "您确定要删除吗？", null, "取消", null, "确定", new
                                    DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OndeleteListener.onDeleteItem(position);
                                        }
                                    });
                        }
                    });

                    showInfoView(viewHolderInfo, remindType, notice);
                    break;
                case PushMessage.ACTIVITYTYPE:
                    final ViewHolderActivity viewHolderActivity;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout
                                .message_infomation_item_layout, null);
                        viewHolderActivity = new ViewHolderActivity(convertView);
                        convertView.setTag(viewHolderActivity);
                    } else {
                        viewHolderActivity = (ViewHolderActivity) convertView.getTag();
                    }

                    viewHolderActivity.messageDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogBuilder.showSimpleDialog(getContext(), "您确定要删除吗？", null, "取消", null, "确定", new
                                    DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OndeleteListener.onDeleteItem(position);
                                        }
                                    });
                        }
                    });

                    showActivityView(viewHolderActivity, remindType, notice);
                    break;
            }
        }

        return convertView;
    }


    private void showRemindView(ViewHolderRemind viewHolderRemind, final int remindType, final
    Notice notice) {

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

         /*if (remindType == PushMessage.REMINDTYPESCHEDULE) {
            viewHolderRemind.iconLeft.setText(R.string.schedule);

            viewHolderRemind.messageRemindContent.setVisibility(View.GONE);
            viewHolderRemind.messageRemindSch.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(notice.getAppointmentTime())) {
                viewHolderRemind.messageDate.setText(notice.getAppointmentTime());
            }

            if (!TextUtils.isEmpty(notice.getAppointmentPlace())) {
                viewHolderRemind.messagePosition.setText(notice.getAppointmentPlace());
            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    ScheduleModel model = new ScheduleModel();
                    model.setRentOrSale(notice.getBizType());
                    model.setAppointmentId(notice.getAppointmentId());
                    model.setState(notice.getStatus());
                    AgendaHouseActivity.gotoThisActivity((FragmentActivity)getContext(),model);

                    //v7.0修改入口页面
                    /*Intent intent = new Intent(getContext(), AgendaDeatilActivity.class);
                    intent.putExtra(AgendaDetailFragment.APPOINTMENT_ID, notice
                            .getAppointmentId());
                    intent.putExtra(AgendaDetailFragment.APPOINTMENT_BIZTYPE, notice
                            .getBizType());
                    intent.putExtra(AgendaDetailFragment.APPOINTMENT_STAUTUS, notice
                            .getStatus());
                    intent.putExtra(AgendaDetailFragment.APPOINTMENT_FROM, true);
                    getContext().startActivity(intent);*//*
                    ManyiAnalysis.getInstance().onEvent(EventStr.MESSAGE_CLICK_REMIND_ITEM);
                }
            });
        }else if (remindType == PushMessage.REMINDTYPECOMMISSON
                || remindType == PushMessage.REMINDTYPECOMMISSONUPDATE) {
            viewHolderRemind.iconLeft.setText(R.string.commisson_key);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);

            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    Intent intent = new Intent(getContext(), EntrustDetailActivity.class);
                    //业主委托biztype1-2 请求0-1
                    if (notice.getBizType() == 0) {
                        intent.putExtra(EntrustDetailFragment.ENTRUST_BIZ, 0);
                    } else {
                        intent.putExtra(EntrustDetailFragment.ENTRUST_BIZ, 1);
                    }
                    intent.putExtra(EntrustDetailFragment.ENTRUST_HOUSEID, notice
                            .getHouseId());
                    intent.putExtra(EntrustDetailFragment.ENTRUST_HASHOUSEID, notice
                            .getHasHouseId());

                    getContext().startActivity(intent);
                    ManyiAnalysis.getInstance().onEvent(EventStr.MESSAGE_CLICK_REMIND_ITEM);
                }
            });
        }else if (remindType == PushMessage.REMINDTYPEIWJWORDER ||
                remindType == PushMessage.REMINDTYPEFANGGUANFANGORDER ||
                remindType == PushMessage.REMINDTYPEFANGGUANFANGBILL ||
                remindType == PushMessage.REMINDTYPEPUZUHETONG ||
                remindType == PushMessage.REMINDTYPEPUZHUQS) {
            viewHolderRemind.iconLeft.setText(R.string.bill);
            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    if (remindType == PushMessage.REMINDTYPEIWJWORDER) {
                        Intent intent = new Intent(getContext(), H5OrderListActivity.class);
                        intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_RENT_ORDER_DETAIL);
                        intent.putExtra(H5OrderListActivity.TYPE, 1);
                        intent.putExtra(OrderDetailActivity.ORDER_ID, notice.getContractId());
                        getContext().startActivity(intent);
                    } else if (remindType == PushMessage.REMINDTYPEFANGGUANFANGORDER) {
                        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                        intent.putExtra(OrderDetailActivity.TYPE, 2);
                        intent.putExtra(OrderDetailActivity.ORDER_ID, notice.getContractId());
                        getContext().startActivity(intent);
                    } else if (remindType == PushMessage.REMINDTYPEFANGGUANFANGBILL) {
                        Intent intent = new Intent(getContext(), BillDetailActivity.class);
                        intent.putExtra(BillDetailActivity.BILL_ID, notice.getContractId());
                        intent.putExtra(BillDetailActivity.TYPE, 2);
                        getContext().startActivity(intent);
                    } else if (remindType == PushMessage.REMINDTYPEPUZUHETONG) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(H5PayRentActivity.PAGE_TYPE, H5PayRentActivity.TYPE_DETAIL);
                        bundle.putLong(H5PayRentActivity.BILL_ID, notice.getBillId());
                        bundle.putInt(H5PayRentActivity.TYPE, 11);
                        H5PayRentActivity.gotoThisPage(getContext(), bundle);
                    } else if (remindType == PushMessage.REMINDTYPEPUZHUQS) {
                        H5PayRentActivity.gotoThisPage(getContext());
                    }

                    ManyiAnalysis.getInstance().onEvent(EventStr.MESSAGE_CLICK_REMIND_ITEM);
                }
            });
        } else if (remindType == PushMessage.REMINDTYPECOMPLAINSTATUS) {
            viewHolderRemind.iconLeft.setText(R.string.message_complaints);
            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    Intent intent = new Intent(getContext(), ComplainListActivity.class);
                    getContext().startActivity(intent);
                    ManyiAnalysis.getInstance().onEvent(EventStr.MESSAGE_CLICK_REMIND_ITEM);
                }
            });
        } else */ if (remindType == PushMessage.REMINDTYPENEWVOUCHER
                || remindType == PushMessage.REMINDTYPECOUPONBANNER
                || remindType == PushMessage.REMINDTYPETIYANJI) {
            //新的现金券通知
            viewHolderRemind.iconLeft.setText(R.string.mine_ticket);
            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    if (remindType == PushMessage.REMINDTYPECOUPONBANNER) {
                        Intent intent = new Intent(getContext(), MessageDetailWebViewActivity.class);
                        intent.putExtra(BaseWebViewActivity.URL, SupportUrl.getTradeEnsureCardUrl());
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), CouponWebViewActivity.class);
                        getContext().startActivity(intent);
                    }
                }
            });

        }/* else if (remindType == PushMessage.REMINDTYPEIWJWBILL) {
            viewHolderRemind.iconLeft.setText(R.string.bill);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    Intent intent = new Intent(getContext(), BillNewDetailActivity.class);
                    intent.putExtra(BillNewDetailActivity.BILL_ID, notice.getBillId());
                    intent.putExtra(BillNewDetailActivity.ORDER_ID, notice.getOrderId());
                    intent.putExtra(BillNewDetailActivity.TYPE, 3);
                    getContext().startActivity(intent);
                }
            });
        }else if (remindType == PushMessage.REMINDTYPEJUJIAN ||
                remindType == PushMessage.REMINDTYPEWANGQIAN ||
                remindType == PushMessage.REMINDTYPEGUOHU ||
                remindType == PushMessage.REMINDTYPEPIDAI) {

            viewHolderRemind.iconLeft.setText(R.string.notice_trade);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    Intent intent = new Intent(getContext(), H5OrderListActivity.class);
                    intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_DETAIL);
                    intent.putExtra(H5OrderListActivity.ORDER_ID, notice.getOrderId());
                    intent.putExtra(H5OrderListActivity.TYPE, 3);
                    getContext().startActivity(intent);
                }
            });

        }  */else if (remindType == PushMessage.REMINDTYPERESERVESUCCESS) {

            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    //todo 预约记录
                    /*Intent intent = new Intent(getContext(), ReserveRecordListActivity.class);
                    getContext().startActivity(intent);*/
                }
            });

        } else if (remindType == PushMessage.REMINDTYPERESERVEFAIL ||
                remindType == PushMessage.REMINDTYPELIUBIAO) {

            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
            if (remindType == PushMessage.REMINDTYPERESERVEFAIL) {
                viewHolderRemind.messageViewDetailText.setText("重新购买");
            } else {
                viewHolderRemind.messageViewDetailText.setText("查看其它产品");
            }
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    IndexActivity.startIndexActivityToTab(getContext(),1);
                    /*Intent intent = new Intent(getContext(), FinanceHomeActivity.class);
                    getContext().startActivity(intent);*/
                }
            });

        }else if (remindType == PushMessage.REMINDTYPEHUANKUAN
                ||remindType == PushMessage.REMINDTYPEZHUANRANG
                || remindType == PushMessage.REMINDTYPEMUZIJIXI) {
            viewHolderRemind.iconLeft.setText(R.string.tab_financial);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    //todo 网贷资产
                    /*Intent intent = new Intent(getContext(), CapitalActivity.class);
                    intent.putExtra(CapitalActivity.TAB, remindType == PushMessage.REMINDTYPEHUANKUAN
                            ? CapitalActivity.EXPIRED : CapitalActivity.HOLD);
                    getContext().startActivity(intent);*/
                }
            });
        } else if (remindType == PushMessage.REMINDTYPETYJTOFIHOME) {

            viewHolderRemind.iconLeft.setText(R.string.mine_ticket);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    IndexActivity.startIndexActivityToTab(getContext(),1);
                    /*Intent intent = new Intent(getContext(), FinanceHomeActivity.class);
                    getContext().startActivity(intent);*/
                }
            });
        }else if (remindType == PushMessage.REMINDTYPEIW400) {
            viewHolderRemind.iconLeft.setText(R.string.remind_default);
            viewHolderRemind.messageViewDetail.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Do Nothing
                }
            });
        } else if (remindType == PushMessage.REMINDTYPEBANKRECEIPTFAIL) {
            viewHolderRemind.iconLeft.setText(R.string.personal_wallet);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    // todo 活期宝
//                    MyWalletActivity.goMywallet(getContext());
                }
            });
        }/* else if (remindType == PushMessage.REMINDTYPEFANLISHENHE) {
            viewHolderRemind.iconLeft.setText(R.string.mine_ticket);
            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEventLog(notice);
                    Intent intent = new Intent(getContext(), CouponWebViewActivity.class);
                    getContext().startActivity(intent);
                }
            });
        }*/else {
            viewHolderRemind.iconLeft.setText(R.string.remind_default);
            viewHolderRemind.messageViewDetailText.setText("查看详情");
            viewHolderRemind.messageViewDetail.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(notice.getContent())) {
                viewHolderRemind.messageRemindContent.setVisibility(View.VISIBLE);
                viewHolderRemind.messageRemindContent.setText(notice.getContent());
                viewHolderRemind.messageRemindSch.setVisibility(View.GONE);
            }

            viewHolderRemind.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogBuilder.showSimpleDialog(getContext(), "更新最新版app，可查看更多内容", null, "取消", null, "确定", new
                            DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    VersionUtil.check(getContext());
                                }
                            });
                }
            });

        }
    }

    private void showInfoView(ViewHolderInfo viewHolderInfo, final int remindType, final Notice
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

        if (TextUtils.isEmpty(notice.getContent())) {
        } else {
            viewHolderInfo.messageContent.setText(StringUtil.ToDBC(notice.getContent()));
        }

        if (TextUtils.isEmpty(notice.getPicUrl())) {
            viewHolderInfo.messageImg.setVisibility(View.GONE);
        } else {
            viewHolderInfo.messageImg.setVisibility(View.VISIBLE);
            loadParam.setImgUri(notice.getPicUrl());
            ImageLoaderClient.display(getContext(), viewHolderInfo.messageImg, loadParam);
        }

        switch (remindType) {
            case PushMessage.NOTICETYPETOWEBVIEW:
            case PushMessage.NOTICETYPETONOTICELIST:

                if (TextUtils.isEmpty(notice.getUrl())) {
                    viewHolderInfo.messageViewDetail.setVisibility(View.GONE);
                    viewHolderInfo.getView().setOnClickListener(null);
                } else {
                    viewHolderInfo.messageViewDetail.setVisibility(View.VISIBLE);
                    viewHolderInfo.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEventLog(notice);
                            Intent intent = new Intent(getContext(), MessageDetailWebViewActivity
                                    .class);
                            intent.putExtra(BaseWebViewActivity.URL, notice.getUrl());
                            ManyiAnalysis.getInstance().onEvent(EventStr
                                    .MESSAGE_CLICK_NOTICE_ITEM);
                            getContext().startActivity(intent);
                        }
                    });
                }
                break;
            case PushMessage.NOTICETYPETOFINANCE:
                viewHolderInfo.messageViewDetail.setVisibility(View.VISIBLE);
                viewHolderInfo.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEventLog(notice);
                        IndexActivity.startIndexActivityToTab(getContext(),1);
                        /*Intent intent = new Intent(getContext(), FinanceHomeActivity.class);
                        getContext().startActivity(intent);*/
                    }
                });
                break;
            /*case PushMessage.NOTICETYPEHOUSEDETAIL:
                viewHolderInfo.messageViewDetail.setVisibility(View.VISIBLE);
                viewHolderInfo.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEventLog(notice);
                        if (notice.getRentOrSale() == 2) {
                            Intent intent = new Intent(getContext(), NewHouseDetailActivity.class);
                            intent.putExtra(CommonTag.PROPERTY_ID, notice.getHouseId());
                            getContext().startActivity(intent);
                        } else if (notice.getRentOrSale() == 0 || notice.getRentOrSale() == 1) {
                            Intent intent = new Intent(getContext(), HouseDetailActivity.class);
                            intent.putExtra(CommonTag.HOUSE_ID, notice.getHouseId());
                            intent.putExtra(CommonTag.RENT_OR_SELL, notice.getRentOrSale());
                            getContext().startActivity(intent);
                        }
                    }
                });
                break;*/
            default:
                viewHolderInfo.messageViewDetail.setVisibility(View.VISIBLE);
                viewHolderInfo.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.showSimpleDialog(getContext(), "更新最新版app，可查看更多内容", null, "取消", null, "确定", new
                                DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VersionUtil.check(getContext());
                                    }
                                });
                    }
                });
                break;
        }
    }

    private void showActivityView(ViewHolderActivity viewHolderActivity, final int remindType,
                                  final Notice notice) {
        if (!TextUtils.isEmpty(notice.getTitle())) {
            viewHolderActivity.messageTitle.setText(notice.getTitle());
        }

        if (!TextUtils.isEmpty(notice.getCreateDate())) {
            if (notice.getReadStatus() == 0) {
                viewHolderActivity.messageTimeNewLayout.setVisibility(View.VISIBLE);
                viewHolderActivity.messageTimeNormal.setVisibility(View.GONE);
                viewHolderActivity.messageTimeNew.setText(notice.getCreateDate());
            } else {
                viewHolderActivity.messageTimeNewLayout.setVisibility(View.GONE);
                viewHolderActivity.messageTimeNormal.setVisibility(View.VISIBLE);
                viewHolderActivity.messageTimeNormal.setText(notice.getCreateDate());
            }

        }

        if (TextUtils.isEmpty(notice.getContent())) {
        } else {
            viewHolderActivity.messageContent.setText(StringUtil.ToDBC(notice.getContent()));
        }

        if (TextUtils.isEmpty(notice.getPicUrl())) {
            viewHolderActivity.messageImg.setVisibility(View.GONE);
        } else {
            viewHolderActivity.messageImg.setVisibility(View.VISIBLE);
            loadParam.setImgUri(notice.getPicUrl());
            ImageLoaderClient.display(getContext(), viewHolderActivity.messageImg, loadParam);
        }


        switch (remindType) {
            case PushMessage.NOTICETYPETOWEBVIEW:
            case PushMessage.NOTICETYPETONOTICELIST:
                if (TextUtils.isEmpty(notice.getUrl())) {
                    viewHolderActivity.messageViewDetail.setVisibility(View.GONE);
                    viewHolderActivity.getView().setOnClickListener(null);
                } else {
                    viewHolderActivity.messageViewDetail.setVisibility(View.VISIBLE);
                    viewHolderActivity.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEventLog(notice);
                            Intent intent = new Intent(getContext(), MessageDetailWebViewActivity
                                    .class);
                            intent.putExtra(BaseWebViewActivity.URL, notice.getUrl());
                            ManyiAnalysis.getInstance().onEvent(EventStr
                                    .MESSAGE_CLICK_NOTICE_ITEM);
                            getContext().startActivity(intent);
                        }
                    });
                }
                break;
            case PushMessage.NOTICETYPETOFINANCE:
                viewHolderActivity.messageViewDetail.setVisibility(View.VISIBLE);
                viewHolderActivity.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEventLog(notice);
                        IndexActivity.startIndexActivityToTab(getContext(),1);
                        /*Intent intent = new Intent(getContext(), FinanceHomeActivity.class);
                        getContext().startActivity(intent);*/
                    }
                });
                break;
            default:
                viewHolderActivity.messageViewDetail.setVisibility(View.VISIBLE);
                viewHolderActivity.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.showSimpleDialog(getContext(), "更新最新版app，可查看更多内容", null, "取消", null, "确定", new
                                DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VersionUtil.check(getContext());
                                    }
                                });
                    }
                });
                break;
        }
    }

    private void sendEventLog(Notice notice){
        // todo 统计
        if(notice != null) {
            Map<String, String> map = new HashMap<>();
            map.put("noticeId", notice.getId() + "");
            map.put("title", notice.getTitle());
            EventLog.upEventLog("164", JSON.toJSONString(map));
        }
    }


    //提醒类型的消息
    class ViewHolderRemind {
        @Bind(R.id.message_icon)
        TextViewTF iconLeft;
        @Bind(R.id.message_title)
        TextView messageTitle;
        @Bind(R.id.message_delete)
        TextViewTF messageDel;
        @Bind(R.id.message_date)
        TextView messageDate;
        @Bind(R.id.message_position)
        TextView messagePosition;
        @Bind(R.id.message_remind_content)
        TextView messageRemindContent;
        @Bind(R.id.message_time)
        TextView messageTimeNormal;
        @Bind(R.id.message_time_new)
        TextView messageTimeNew;
        @Bind(R.id.message_time_new_layout)
        LinearLayout messageTimeNewLayout;
        @Bind(R.id.message_view_detail)
        View messageViewDetail;
        @Bind(R.id.message_remind_schedule)
        View messageRemindSch;
        @Bind(R.id.message_view_detail_text)
        TextView messageViewDetailText;

        public View view;

        ViewHolderRemind(View view) {
            ButterKnife.bind(this, view);
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }

    //资讯类型的消息
    class ViewHolderInfo {
        @Bind(R.id.message_content_image)
        ImageView messageImg;
        @Bind(R.id.message_title)
        TextView messageTitle;
        @Bind(R.id.message_delete)
        TextViewTF messageDel;
        @Bind(R.id.message_content)
        TextView messageContent;
        @Bind(R.id.message_time)
        TextView messageTimeNormal;
        @Bind(R.id.message_time_new)
        TextView messageTimeNew;
        @Bind(R.id.message_time_new_layout)
        LinearLayout messageTimeNewLayout;
        @Bind(R.id.message_view_detail)
        View messageViewDetail;

        View view;

        public View getView() {
            return view;
        }

        ViewHolderInfo(View view) {

            ButterKnife.bind(this, view);
            this.view = view;

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

    //活动类型的消息
    class ViewHolderActivity {
        @Bind(R.id.message_content_image)
        ImageView messageImg;
        @Bind(R.id.message_title)
        TextView messageTitle;
        @Bind(R.id.message_delete)
        TextViewTF messageDel;
        @Bind(R.id.message_content)
        TextView messageContent;
        @Bind(R.id.message_time)
        TextView messageTimeNormal;
        @Bind(R.id.message_time_new)
        TextView messageTimeNew;
        @Bind(R.id.message_time_new_layout)
        LinearLayout messageTimeNewLayout;
        @Bind(R.id.message_view_detail)
        View messageViewDetail;

        View view;

        public View getView() {
            return view;
        }

        ViewHolderActivity(View view) {

            ButterKnife.bind(this, view);
            this.view = view;

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

    public interface OndeleteListener {
        void onDeleteItem(int position);
    }
}
