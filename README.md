# Android 政策协议及青少年模式封装库
用于简化操作，针对中国国内市场的 Android APP 隐私协议，动态授权提示以及青少年模式时间控制。

## 项目名称及简介
为了方便政策要求减少繁琐的UI和内容编写，政策协议及青少年模式封装库可以方便实现这些功能。

## 项目特点
简化外围政策功能性设计

## 项目安装指南
下载 aar 包，放入libs中

## 项目功能及使用指南
```
// 青少年模式初始化设置每天限制时间（40分钟）
ChildrenModeUtils.getInstance().init(WelcomeActivity.this, 40);
```

```
//需要计时的 Activity 继承此 BaseActivity
public class BaseActivity extends AppCompatActivity {

    long startTime;
    @Override
    protected void onResume() {
        startTime = SystemClock.elapsedRealtime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long lastTime = SystemClock.elapsedRealtime() - startTime;
        ChildrenModeUtils.getInstance().addUsingTime(lastTime);
    }
}

//在最后退出的出口 Activity 中设置，用于保存总时间
@Override
protected void onDestroy() {
    ChildrenModeUtils.getInstance().saveUsingTime(this);
    super.onDestroy();
}

//程序中获取使用总时间：
long usingTime = ChildrenModeUtils.getInstance().readUsingTimeToday(this);
tvTime.setText(MessageFormat.format("{0} 秒", usingTime / 1000));
```

```
// 青少年模式打开关闭切换
findViewById(R.id.btn_child_mode).setOnClickListener(v -> {
    if (ChildrenModeUtils.getInstance().isChildrenMode()) {
        ChildrenModeCloseDialog childrenModeCloseDialog = ChildrenModeCloseDialog.newInstance();
        childrenModeCloseDialog.setListener(new ChildrenModeCloseDialog.IListener() {
            @Override
            public void closed() {

            }

            @Override
            public void ignore() {

            }
        });
        childrenModeCloseDialog.setCancelable(false);
        childrenModeCloseDialog.show(getSupportFragmentManager(), "childrenModeCloseDialog");
    } else {
        ChildrenModeDialog childrenModeDialog = ChildrenModeDialog.newInstance(null);
        childrenModeDialog.setListener(new ChildrenModeDialog.IListener() {
            @Override
            public void opened() {

            }

            @Override
            public void ignore() {

            }
        });
        childrenModeDialog.setCancelable(false);
        childrenModeDialog.show(getSupportFragmentManager(), "childrenModeDialog");
    }
});
```

## 使用范例

<img src="https://raw.githubusercontent.com/vv1024/PrivacyProtocol/master/Screenshot_1572359751.png" width="200px" />

<img src="https://raw.githubusercontent.com/vv1024/PrivacyProtocol/master/Screenshot_1572359759.png" width="200px" />

<img src="https://raw.githubusercontent.com/vv1024/PrivacyProtocol/master/Screenshot_1572359772.png" width="200px" />

<img src="https://raw.githubusercontent.com/vv1024/PrivacyProtocol/master/Screenshot_1572359776.png" width="200px" />

## 其他说明
