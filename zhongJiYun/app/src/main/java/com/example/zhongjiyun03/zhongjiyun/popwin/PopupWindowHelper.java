package com.example.zhongjiyun03.zhongjiyun.popwin;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.zhongjiyun03.zhongjiyun.R;

import java.util.List;

/**
 * @Package com.qianfeng.popwindowdemo
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 三月
 * @修 改 人:
 * @日 期:
 */
public class PopupWindowHelper {
    private static PopupWindowHelper popHelper;
    private PopupWindow popupWindow;
    private static Context context;

    public static void init(Context context) {
        PopupWindowHelper.context = context;
    }

    public static PopupWindowHelper getInstance() {
        if (popHelper == null) {
            popHelper = new PopupWindowHelper();
        }
        return popHelper;
    }

    private PopupWindowHelper() {

    }

    /**
     *
     */
    private void initPopupWindow() {
        if (context == null) {

        } else {
            if (popupWindow == null) {
                popupWindow = new PopupWindow(context);
            }
        }
    }

    private void initPopupWindow(int width, int heigth) {
        this.initPopupWindow();
        popupWindow.setWidth(width);
        popupWindow.setHeight(heigth);
    }


    private void initPopupWindow(View contontView, int width, int heigth) {
        this.initPopupWindow(width, heigth);
        popupWindow.setContentView(contontView);
    }

    public void showPopListView(View anchor, List<String> list, int width, int heigth) {
        if (popupWindow == null) {
            this.initPopupWindow(width, heigth);
        }
        View view = careatContentView(list);
        popupWindow.setContentView(view);
        //设置popwindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(R.color.ic_white));
        //点击popwindow之外的界面是否消失
        popupWindow.setOutsideTouchable(true);
//        popupWindow.showAsDropDown(anchor);
//        popupWindow.showAsDropDown(anchor, 120, 120);
        popupWindow.showAtLocation(anchor, Gravity.TOP, 200, 250);
    }

    private View careatContentView(List<String> list) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_content_layout, null);
        ListView listView = (ListView) view.findViewById(R.id.pop_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_layout, R.id.item_text, list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("listjdjfj",i+"");
                dismiss();

            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


}
