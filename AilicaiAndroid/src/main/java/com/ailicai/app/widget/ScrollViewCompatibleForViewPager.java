/**
 * All rights Reserved, Copyright (C) HAOWU LIMITED 2012-2015
 * FileName: ScrollViewCompatibleForViewPager.java
 * Version:  $Revision$
 * Modify record:
 * NO. |		Date		|		Name		|		Content
 * 1   |	2013-9-4		|	LEWU)ZhouXuan	|	original version
 */
package com.ailicai.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * class name:ScrollViewCompatibleForViewPager <BR>
 * class description: 解决ScrollView嵌套ViewPager出现的滑动冲突问题 <BR>
 * Remark: <BR>
 * 
 * @version 1.00 2013-9-4
 * @author LEWU)ZhouXuan
 */
public class ScrollViewCompatibleForViewPager extends ScrollView {

	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	public ScrollViewCompatibleForViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

}
