/**
 * 版权所有：中兴通讯股份有限公司
 * 文件名称：CustomToast.java
 * 文件作者：Xiezhuoxun
 * 开发时间：2013-4-15
 * <p>
 * 修改者：maj
 * 修改时间：2015-05-19
 * 修改内容：显示内容改为自定义视图
 */
package com.zxc.walk.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxc.walk.R;
import com.zxc.walk.core.application.BaseApp;
import com.zxc.walk.framework.utils.UIUtils;


/**
 * 自定义的Toast
 *
 * @author wp
 */
public class CustomToast {

    /**
     * The m toast.
     */
    private static Toast mToast;
    private static Object mObj = new Object();
    private static LayoutInflater mInflate;

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    private static void showToast(final Context context, final CharSequence text,
                                  final int duration, final int gravity) {
        if (context == null || TextUtils.isEmpty(text)) {
            return;
        }
        UIUtils.runInMainThread(new Runnable() {

            @Override
            public void run() {
                synchronized (mObj) {
                    if (mToast != null) {
                        final View view = mToast.getView();
                        TextView tvText = view
                                .findViewById(R.id.customtoast_tv_text);
                        tvText.setText(TextUtils.isEmpty(text) ? "" : text);
                        mToast.setDuration(duration);
                        int yOffSet = 0;
                        if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                            yOffSet = 200;
                        }
                        mToast.setGravity(gravity, 0, yOffSet);
                    } else {
                        Context pContext = context;
                        if (context instanceof Activity) {
                            pContext = BaseApp.getApplication();
                        }
                        mToast = Toast.makeText(pContext, TextUtils.isEmpty(text) ? "" : text, duration);

                        int yOffSet = 0;
                        if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                            yOffSet = 200;
                        }
                        mToast.setGravity(gravity, 0, yOffSet);
                        mToast.setView(getContentView(pContext, TextUtils.isEmpty(text) ? "" : text));
                    }
                    try {
                        //show()里面报NullPointerException，这个地方try一下
                        //Attempt to invoke interface method void android.app.INotificationManager.enqueueToast on a null object reference
                        mToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        showToast(context, text, duration, Gravity.CENTER);
    }

    /**
     * Show toast.
     *
     * @param mContext the m context
     * @param resId    the res id
     * @param duration the duration
     * @version
     */
    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

    /**
     * toast在中间位置显示
     *
     * @param context
     * @param text
     */
    public static void showToastCenter(Context context, CharSequence text,
                                       int duration) {
        showToast(context, text, duration, Gravity.CENTER);
    }

    /**
     * <p>
     * Description: Toast显示在靠近顶部的位置
     * <p>
     *
     * @param context
     * @param text
     * @param duration
     * @date 2013-11-26 下午4:50:32
     * @author XieZhuoxun
     */
    public static void showToastNearTop(final Context context,
                                        final CharSequence text, final int duration) {
        showToast(context, text, duration, Gravity.TOP);
    }

    /**
     * <p>
     * Description: Toast显示在靠近中下的位置
     * <p>
     *
     * @param context
     * @param text
     * @param duration
     * @date 2013-11-26 下午4:50:32
     * @author XieZhuoxun
     */
    public static void showToastNearButtom(final Context context,
                                           final CharSequence text, final int duration) {
        showToast(context, text, duration, Gravity.BOTTOM);
    }

    /**
     * Description: 获取自定义toast视图
     * <p>
     *
     * @param context
     * @param text
     * @return
     * @date 2015年5月19日
     * @author maj
     */
    private static View getContentView(Context context, CharSequence text) {
        if (mInflate == null) {
            mInflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View contentView = mInflate.inflate(R.layout.customtoast, null);
        TextView tvText = contentView.findViewById(R.id.customtoast_tv_text);
        tvText.setText(TextUtils.isEmpty(text) ? "" : text);

        return contentView;
    }

    /**
     * 为什么有这个方法，坑爹的华为高版本在连续toast会出问题，所以我们在第二次toast时重置
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
