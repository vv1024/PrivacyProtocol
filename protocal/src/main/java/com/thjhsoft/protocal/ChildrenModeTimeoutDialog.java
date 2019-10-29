package com.thjhsoft.protocal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * 超时对话框
 */
public class ChildrenModeTimeoutDialog extends DialogFragment {
    private static final String TAG = ChildrenModeTimeoutDialog.class.getSimpleName();


    TextView btnDelayed, btnClose, btnQuit;
    EditText etPassword;

    LinearLayout pwdView;

    private IListener listener;

    public static ChildrenModeTimeoutDialog newInstance() {
        return new ChildrenModeTimeoutDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Pro_Round_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_children_timeout, container);

        btnDelayed = rootView.findViewById(R.id.btn_delayed);
        btnClose = rootView.findViewById(R.id.btn_close);
        btnQuit = rootView.findViewById(R.id.btn_quit);
        etPassword = rootView.findViewById(R.id.et_password);
        pwdView = rootView.findViewById(R.id.password_view);

        btnQuit.setOnClickListener(view -> {
            if(listener!= null){
                listener.quit();
            }
            Objects.requireNonNull(getDialog()).dismiss();
        });

        btnClose.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(ChildrenModeUtils.CONFIG_NAME, Context.MODE_PRIVATE);
            String originalPassword = sharedPreferences.getString(ChildrenModeUtils.PWD_FIELD, null);
            String password = etPassword.getText().toString();
            if (TextUtils.isEmpty(originalPassword) || TextUtils.isEmpty(password) || !originalPassword.equals(password)) {
                Toast.makeText(getContext(), getString(R.string.pro_pwd_wrong), Toast.LENGTH_SHORT).show();
            } else {
                ChildrenModeUtils.getInstance().resetTime(sharedPreferences);
                if(listener!= null){
                    listener.closed();
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }

        });

        btnDelayed.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(ChildrenModeUtils.CONFIG_NAME, Context.MODE_PRIVATE);
            String originalPassword = sharedPreferences.getString(ChildrenModeUtils.PWD_FIELD, null);
            String password = etPassword.getText().toString();
            if (TextUtils.isEmpty(originalPassword) || TextUtils.isEmpty(password) || !originalPassword.equals(password)) {
                Toast.makeText(getContext(), getString(R.string.pro_pwd_wrong), Toast.LENGTH_SHORT).show();
            } else {
                ChildrenModeUtils.getInstance().setDelayedMode(true);
                if(listener!= null){
                    listener.delayed();
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        return rootView;
    }

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public interface IListener {
        public void closed();
        public void quit();
        public void delayed();
    }

}
