package com.ailicai.app.receiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.eventbus.SmsCodeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gerry on 2015/5/27.
 */
public class SmsObserver extends ContentObserver {

    public static final String senderIW = "106905658866220011";//不是固定的
    public static final String messageIW = "爱屋吉屋";
    public static Uri SMS_INBOX = Uri.parse("content://sms/");
    private Context mContext;
    public static final String MESSAGE_COMEBACK = "message_comeback";

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        getSmsFromPhone();
    }


    /**
     * sms主要结构：
     * <p/>
     * 　　_id：短信序号，如100
     * <p/>
     * 　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
     * <p/>
     * 　　address：发件人地址，即手机号，如+86138138000
     * <p/>
     * 　　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
     * <p/>
     * 　　date：日期，long型，如1346988516，可以对日期显示格式进行设置
     * <p/>
     * 　　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
     * <p/>
     * 　　read：是否阅读0未读，1已读
     * <p/>
     * 　　status：短信状态-1接收，0complete,64pending,128failed
     * <p/>
     * 　　type：短信类型1是接收到的，2是已发出
     * <p/>
     * 　　body：短信具体内容
     * <p/>
     * 　　service_center：短信服务中心号码编号，如+8613800755500
     */

    public void getSmsFromPhone() {
        ContentResolver contentResolver = mContext.getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
        String where = " body like '%" + messageIW + "%' AND date >  "
                + (System.currentTimeMillis() - 1 * 60 * 1000);
        Cursor cur = contentResolver.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur) {
            Log.i("SmsObserver", "Cursor=====null");
            return;
        }
        if (cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndex("body"));
            if (body.startsWith(messageIW) || body.contains(messageIW)) {
                onEventBus(body);
            }
            //【爱屋吉屋】爱屋吉屋登录验证码：879311，有效时间30分钟，为保护您的账号安全，请勿转发他人。
            Log.i("SmsObserver", "==========Cursor============" + ":::::" + body);
        }
    }


    public void onEventBus(String body) {
        String msgStr = UIUtils.shortenMessage(body);
        //String code = getSMSCode(msgStr);
        String code = getNumbers(msgStr);
        SmsCodeEvent smsEvent = new SmsCodeEvent();
        smsEvent.setCode(code);
        EventBus.getDefault().post(smsEvent);
        /*短信接收后,获取验证码时间不再继续,
         只用于获取验证码按钮点击后立即返回,再次进入页面是否继续走时间的问题*/
        MyPreference.getInstance().write(MESSAGE_COMEBACK, true);
    }

    /**
     * 去除30分钟的30数字
     * 截取数字
     *
     * @return
     */
    public String getNumbers(String content) {
        String regEx = "\\d+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 获取短信中的数字
     *
     * @param msgStr
     * @return
     */
    public String getSMSCode(String msgStr) {
        //String regEx = " [a-zA-Z0-9]{10}";
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(msgStr);
        return m.replaceAll("").trim();
    }

}
