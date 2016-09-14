package com.android.backchina.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.backchina.base.BaseApplication;

/**
 * 屏幕适配相关工具�?
 * @author changxiaofei
 * @time 2013-11-28 下午2:14:22
 */
public class DisplayUtil {
	
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) BaseApplication.context().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }
    
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(float pxValue) {
		float density = getDisplayMetrics().density;
		return (int) (pxValue / density + 0.5f);
	}
	
	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		float density = getDisplayMetrics().density;
		return (int) (dipValue * density + 0.5f);
	}
	
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(float pxValue) {
		float fontScale = getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	
	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(float spValue) {
		float fontScale = getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
