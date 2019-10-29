package com.thjhsoft.privacyprotocol;

import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.thjhsoft.protocal.ChildrenModeUtils;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        ChildrenModeUtils.getInstance().setStartTime(SystemClock.elapsedRealtime());
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        ChildrenModeUtils.getInstance().updateUsingTime();
        super.onPause();
    }
}
