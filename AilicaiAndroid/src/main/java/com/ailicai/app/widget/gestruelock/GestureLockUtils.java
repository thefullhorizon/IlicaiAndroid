package com.ailicai.app.widget.gestruelock;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势库工具类
 * Created by jeme on 2017/8/7.
 */

public class GestureLockUtils {

    private GestureLockView[] mViews;
    private List<Integer> mAnswer;

    public GestureLockUtils(){
        mAnswer = new ArrayList<>();
    }

    /***
     *
     * @param views 需要判断的控件
     */
    public void setLockViews(GestureLockView[] views){
        this.mViews = views;
    }

    public void setAnswer(List<Integer> answer){
        // 直接用赋值语句传递的是内存地址，会导致mChoose一改mAnswer就跟着改了
        this.mAnswer.clear();
        for (int i = 0; i < answer.size(); i++) {
            this.mAnswer.add(answer.get(i));
        }
    }

    /**
     * 设置答案
     */
    public void setAnswer(String answer) {
        mAnswer.clear();
        for (int i = 0; i < answer.length(); i++) {
            mAnswer.add((int) answer.charAt(i) - 48);
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     */
    public boolean checkAnswer(List<Integer> chooses) {
        return mAnswer != null && chooses.equals(mAnswer);
    }

    /**
     * 通过x,y获得落入的GestureLockView
     * @param zone 到控件的距离
     */
    public GestureLockView  getChildIdByPos(int x, int y,int zone) {
        if(mViews == null){
            return null;
        }
        for (GestureLockView gestureLockView : mViews) {
            if (checkPositionInChild(gestureLockView, x, y,zone)) {
                return gestureLockView;
            }
        }

        return null;

    }
    /**
     * 检查当前左边是否在child中
     *
     */
    private boolean checkPositionInChild(View child, int x, int y,int zone) {

        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (zone * 0.15);

        if (x >= child.getLeft() + padding && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    // 处理手势密码的字符串转换
    public String listToStr(List<Integer> list) {
        String str = "";
        if (list == null || list.size() <= 0) {
            return str;
        }
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + "";
        }
        return str;
    }


    /***
     * 检查此id是否是已经选中
     * n * n的阵列，首位从0起算，计算公式：cId = x + n*y + 1
     */
    public int checkChoose(int cId,List<Integer> choose,int count) {
        if (choose == null || choose.size() < 1 || count == 0) {
            return -1;
        }
        int lastX, lastY;
        int nowX, nowY;
        int lastId = choose.get(choose.size() - 1);

        lastX = (lastId - 1) % count;
        lastY = (lastId - 1) / count;

        nowX = (cId - 1) % count;
        nowY = (cId - 1) / count;

        int signX = compare(lastX, nowX);
        int signY = compare(lastY, nowY);
        // 比较x轴y轴间距
        int copiesX = (nowX - lastX) * signX;
        int copiesY = (nowY - lastY) * signY;
        int copies = copiesX > copiesY ? copiesY : copiesX;

        if (copiesX == 1 || copiesY == 1) {
            return -1;
        }

        if (signX == 0 || signY == 0) {
            return lastX + signX + (lastY + signY) * count + 1;
        }

        if (copies > 1 && copiesX % copies == 0 && copiesY % copies == 0) {
            return lastX + copiesX / copies * signX
                    + (lastY + copiesY / copies * signY) * count + 1;
        }

        return -1;
    }

    private int compare(int last, int now) {
        if (now > last) {
            return 1;
        } else if (now < last) {
            return -1;
        } else {
            return 0;
        }
    }
}
