package com.thjhsoft.privacyprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thjhsoft.protocal.ChildrenModeCloseDialog;
import com.thjhsoft.protocal.ChildrenModeDialog;
import com.thjhsoft.protocal.ChildrenModeTimeoutDialog;
import com.thjhsoft.protocal.ChildrenModeUtils;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_child_mode).setOnClickListener(v -> {
            if (ChildrenModeUtils.getInstance().isChildrenMode()) {
                ChildrenModeCloseDialog childrenModeCloseDialog = ChildrenModeCloseDialog.newInstance();
                childrenModeCloseDialog.show(getSupportFragmentManager(), "childrenModeCloseDialog");
            } else {
                ChildrenModeDialog childrenModeDialog = ChildrenModeDialog.newInstance(null);
                childrenModeDialog.show(getSupportFragmentManager(), "childrenModeDialog");
            }
        });

        tvTime = findViewById(R.id.tv_time);

        findViewById(R.id.btnSub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SubActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        //long usingTime = ChildrenModeUtils.getInstance().readUsingTimeToday(this);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
       //tvTime.setText(MessageFormat.format("{0} 秒", usingTime / 1000));
        tvTime.setText(MessageFormat.format("{0} hours", hour24));

        if (!ChildrenModeUtils.getInstance().inUsingTime(MainActivity.this)) {
            ChildrenModeTimeoutDialog childrenModeTimeoutDialog = ChildrenModeTimeoutDialog.newInstance();
            childrenModeTimeoutDialog.setListener(new ChildrenModeTimeoutDialog.IListener() {
                @Override
                public void closed() {
                    //ChildrenModeCloseDialog.newInstance().show(getSupportFragmentManager(), "childModeClose");
                }

                @Override
                public void quit() {
                    finish();
                }

                @Override
                public void delayed() {
                    // 延时
                }
            });
            childrenModeTimeoutDialog.setCancelable(false);

            childrenModeTimeoutDialog.show(getSupportFragmentManager(), "childModeTimeOut");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        ChildrenModeUtils.getInstance().saveUsingTime(this);
        super.onDestroy();
    }
}
