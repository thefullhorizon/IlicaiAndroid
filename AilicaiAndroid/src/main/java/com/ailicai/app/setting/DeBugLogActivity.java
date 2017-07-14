package com.ailicai.app.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.widget.DialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeBugLogActivity extends AppCompatActivity {

    TextView currentdiffTimeView;

    public static boolean isClean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRelease()) {
            finish();
        }
        CommonUtil.miDarkSystemBar(this);
        setContentView(R.layout.activity_de_bug_log);
        currentdiffTimeView = (TextView) findViewById(R.id.currentdiff_time_view);
        findViewById(R.id.title_left_back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        currentdiffTimeView.setText(getDiffTime() + "");
        TextView logTextview = (TextView) findViewById(R.id.log_textview);
        logTextview.setText(getLogDiffTime() + "");
        View cleanlogbtn = findViewById(R.id.clean_log_btn);
        cleanlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.showSimpleDialog(DeBugLogActivity.this, null, "将删除日志下次启动生效", "取消", null, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isClean = true;
                    }
                });
            }
        });
    }


    public static void saveDiffTime(long difftime) {
        if (isRelease()) {
            return;
        }
        MyPreference.getInstance().write("SyncDiffTime", difftime);
    }

    public static long getDiffTime() {
        if (isRelease()) {
            return -1;
        }
        return MyPreference.getInstance().read("SyncDiffTime", 0L);
    }

    public static void saveADiffTime(long difftime) {
        if (isRelease()) {
            return;
        }
        MyPreference.getInstance().write("SyncADiffTime", difftime);
    }

    public static long getADiffTime() {
        if (isRelease()) {
            return -1;
        }
        return MyPreference.getInstance().read("SyncADiffTime", 0L);
    }

/*

    public static void saveBDiffTime(long difftime) {
        MyPreference.getInstance().write("SyncADiffTime", "");
    }

    public static long getBDiffTime() {
        return MyPreference.getInstance().read("SyncBDiffTime", 0);
    }
*/

    public static void saveLogDiffTime(long difftime) {
        if (isRelease()) {
            return;
        }
        String oldLog = MyPreference.getInstance().read("SyncLogDiffTime", "");
        if (TextUtils.isEmpty(oldLog)) {
            oldLog = getTimeLogString() + "的时差:" + difftime;
        } else {
            oldLog = oldLog + "\n" + " " + getTimeLogString() + "的时差:" + difftime;
        }
        MyPreference.getInstance().write("SyncLogDiffTime", oldLog);
    }

    public static void saveOtheLog(String log) {
        if (isRelease()) {
            return;
        }
        String oldLog = MyPreference.getInstance().read("SyncLogDiffTime", "");
        if (TextUtils.isEmpty(oldLog)) {
            oldLog = getTimeLogString() + "发生log:" + log;
        } else {
            oldLog = oldLog + "\n" + getTimeLogString() + "发生log:" + log;
        }
        MyPreference.getInstance().write("SyncLogDiffTime", oldLog);
    }

    public static String getLogDiffTime() {
        if (isRelease()) {
            return "";
        }
        return MyPreference.getInstance().read("SyncLogDiffTime", "");
    }

    public static void logClean() {
        if (isRelease()) {
            return;
        }
        saveDiffTime(0L);
        saveADiffTime(0L);
        MyPreference.getInstance().write("SyncLogDiffTime", "");
    }


    public static String getTimeLogString() {
        if (isRelease()) {
            return "";
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static boolean isRelease() {
        return AILICAIBuildConfig.isProduction();
    }
}
