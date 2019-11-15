package com.madq.autooper.service;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import com.madq.autooper.util.ToastUtil;

public class VideoAccessibilityService extends AccessibilityService {
    private final String TAG = getClass().getName();
    Handler handler = new Handler();
    public static VideoAccessibilityService mService;
    int height = 1920;
    int width = 1080;
    Random random = new Random();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<AccessibilityNodeInfo> list = findViewByIdList("com.kuaishou.nebula:id/view_pager");
            if (list.size() == 1) {
                doing();
            }
        }
    };

    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        ToastUtil.toast("O(∩_∩)O~~\r\n快手辅助启动了...去打开快手吧");
        Log.e("madq", "onServiceConnected");
        mService = this;
    }

    private Toast mToast;

    private void showToast(String toast) {
        if (mToast != null) {
            mToast.setText(toast);
            mToast.show();
        }
    }

    private Runnable toastTask = new Runnable() {
        @Override
        public void run() {
            showToast("监控中。。。。");
        }
    };

    /**
     * TYPE_WINDOW_CONTENT_CHANGED 2048
     * TYPE_VIEW_SCROLLED 4096
     *
     * @param event
     */
    //实现辅助功能
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("madq", event.getEventType() + "," + event.getClassName());
        handler.removeCallbacks(toastTask);
        handler.postDelayed(toastTask, 3000);
        if (event != null && (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED || event.getEventType() == TYPE_WINDOW_STATE_CHANGED)) {

            List photos = findViewByIdList("com.kuaishou.nebula:id/view_pager_photos");
            if (photos.size() >= 1) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doing();
                    }
                }, 1000);
            }

            List<AccessibilityNodeInfo> list = findViewByIdList("com.kuaishou.nebula:id/view_pager");
            if (list.size() == 1) {
                Rect b = new Rect();
                getRootInActiveWindow().getBoundsInScreen(b);
                height = b.bottom;
                width = b.right;
                Log.e("mdq", "screen" + b.top + "," + b.bottom + "," + b.left + "," + b.right);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, getRandom(8 * 1000, 13 * 1000));
            }
        }
    }

    private int getRandom(int min, int max) {

        return random.nextInt(max) % (max - min + 1) + min;
    }


    float bottom = 0.16f;
    float header = 0.1f;
    float middle = 0.5f;
    float f = 0.06f;

    private int getBottomY() {
        return (int) (height * (1 - bottom) + getRandom(-(int) (height * f), (int) (height * f)));
    }

    private int getHeader() {
        return (int) (height * header + getRandom(-(int) (height * f), (int) (height * f)));
    }

    private int getMiddle() {
        return (int) (width * middle + getRandom(-(int) (width * f), (int) (width * f)));
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void doing() {
        if (!isStart()) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            ToastUtil.toast("手机不支持");
            return;
        }
        Path path = new Path();
        path.moveTo(getMiddle(), getBottomY());
        path.lineTo(getMiddle(), getHeader());
        Log.e("madq", getMiddle() + "," + getBottomY() + "," + getHeader());
        final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, getRandom(300, 700));
        mService.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                ToastUtil.toast("ok");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                ToastUtil.toast("error");
            }
        }, null);
    }

    @Override
    public void onInterrupt() {
        ToastUtil.toast("(；′⌒`)\r\n红包功能被迫中断");
        mService = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 公共方法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 辅助功能是否启动
     */
    public static boolean isStart() {
        return mService != null;
    }

    /**
     * 根据getRootInActiveWindow查找当前id的控件集合(类似listview这种一个页面重复的id很多)
     *
     * @param idfullName id全称:com.android.xxx:id/tv_main
     */
    public List<AccessibilityNodeInfo> findViewByIdList(String idfullName) {
        try {
            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
            if (rootInfo == null) return new ArrayList<>();
            List<AccessibilityNodeInfo> list = rootInfo.findAccessibilityNodeInfosByViewId(idfullName);
            rootInfo.recycle();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 此方法效率相对较低,建议使用之后保存id然后根据id进行查找
     */
    public List<AccessibilityNodeInfo> findViewByContentDescription(String contentDescription) {
        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
        if (rootInfo == null) return list;
        findViewByContentDescription(list, rootInfo, contentDescription);
        rootInfo.recycle();
        return list;
    }

    /**
     * 此方法效率相对较低,建议使用之后保存id然后根据id进行查找
     */
    public static void findViewByContentDescription(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo parent, String contentDescription) {
        if (parent == null) return;
        for (int i = 0; i < parent.getChildCount(); i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child == null) continue;
            CharSequence cd = child.getContentDescription();
            if (cd != null && contentDescription.equals(cd.toString())) {
                list.add(child);
            } else {
                findViewByContentDescription(list, child, contentDescription);
                child.recycle();
            }
        }
    }

    /**
     * 由于太多,最好回收这些AccessibilityNodeInfo
     */
    public static void recycleAccessibilityNodeInfo(List<AccessibilityNodeInfo> listInfo) {
        if (ToastUtil.isEmptyArray(listInfo)) return;

        for (AccessibilityNodeInfo info : listInfo) {
            info.recycle();
        }
    }
}