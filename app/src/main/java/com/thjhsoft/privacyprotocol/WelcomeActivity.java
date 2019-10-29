package com.thjhsoft.privacyprotocol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.thjhsoft.protocal.ChildrenModeDialog;
import com.thjhsoft.protocal.ChildrenModeUtils;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ChildrenModeUtils.getInstance().init(WelcomeActivity.this, 2);

        if(ChildrenModeUtils.getInstance().isFirstShow(WelcomeActivity.this)){
            ChildrenModeDialog childrenModeDialog = ChildrenModeDialog.newInstance(null);
            childrenModeDialog.setListener(new ChildrenModeDialog.IListener() {
                @Override
                public void opened() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void ignore() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            });
            childrenModeDialog.setCancelable(false);
            childrenModeDialog.show(getSupportFragmentManager(), "childrenModeDialog");


        }else{
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }
}
