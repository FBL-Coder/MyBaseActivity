package com.example.abc.mybaseactivity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.andview.refreshview.XRefreshView;
import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.Dialog_Select_Bottom.Dialog_Bottom;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.MyApplication.MyApplication;
import com.example.abc.mybaseactivity.Notifications.NotificationUtils;
import com.example.abc.mybaseactivity.OtherUtils.DialogUtil;
import com.example.abc.mybaseactivity.OtherUtils.LogUtil;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private String Tag = this.getClass().getName();
    private XRefreshView xrefreshview;
    private ImageView iv_0;
    private ImageView iv_1;
    private ImageView iv_2;

    @Override
    public void initView() {
        setLayout(R.layout.main);
        setTitleImageBtn(true, R.drawable.back_image_select, true, R.drawable.ic_launcher_round);

        setTitleText(getString(R.string.TestClass), 20, R.color.white);
        setTitleViewVisible(true, R.color.colorAccent);
        setStatusColor(R.color.colorAccent);

        xrefreshview = setPullLoadData(true, true);
        iv_0 = (ImageView) findViewById(R.id.iv_0);
        iv_0.setOnClickListener(this);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_1.setOnClickListener(this);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_2.setOnClickListener(this);
    }

    @Override
    public void initData() {

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //模拟数据加载失败的情况
                        Random random = new Random();
                        boolean success = random.nextBoolean();
                        if (success) {
                            xrefreshview.stopRefresh();
                        } else {
                            xrefreshview.stopRefresh(false);
                        }
                        //或者
                        //xRefreshView.stopRefresh(success);
                    }
                }, 1500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //模拟数据加载失败的情况
                        Random random = new Random();
                        boolean success = random.nextBoolean();
                        if (success) {
                            xrefreshview.stopLoadMore();
                        } else {
                            xrefreshview.setLoadComplete(true);
                        }
                    }
                }, 1500);
            }
        });

        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showText("你要退出，但是我不同意！！！");
            }
        });
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(R.style.ActionSheetStyleIOS7);
                Dialog_Bottom menuView = new Dialog_Bottom(MainActivity.this);
                menuView.setCancelButtonTitle("取消");// before add items
                menuView.addItems("编 辑", "删 除", "新 增", "收 藏");
                menuView.setItemClickListener(new Dialog_Bottom.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {
                        ToastUtil.showText("点击的是第" + itemPosition + "条！");
                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
            }
        });
    }

    @Override
    protected void onDestroy() {
        MyApplication.finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_0:
                Map<String, String> params = new HashMap<>();
                params.put("username", "long");
                params.put("password", "123456");
                DialogUtil.showDialogLoading(MainActivity.this);

                OkHttpUtils.postAsyn("http://119.147.115.203:18980/index.php/Webapi/Index/login/", params, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        LogUtil.e(Tag, "数据请求成功   返回结果：" + "返回码 " + resultDesc.getError_code() + "返回说明" + resultDesc.getReason() + "返回数据" + resultDesc.getResult());
                        DialogUtil.hideDialogLoading();

                    }
                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        LogUtil.e(Tag, "数据请求失败   失败原因：" + message);
                    }

                    @Override
                    public void onProgress(long currentTotalLen, long totalLen) {
                        super.onProgress(currentTotalLen, totalLen);
                    }
                });
                LogUtil.e(Tag, "异步请求数据这里继续进行");
                break;
            case R.id.iv_1:
                ToastUtil.showText("你好啊我是你的好朋友");
                break;
            case R.id.iv_2:
                NotificationUtils.createNotif(
                        this, R.mipmap.ic_launcher, "你好，我是Notification",
                        "标题", "内容", new Intent(this, TestClass.class), 0, 0);
                break;
        }
    }
}
