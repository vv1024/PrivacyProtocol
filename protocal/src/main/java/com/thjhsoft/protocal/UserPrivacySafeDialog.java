package com.thjhsoft.protocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * 权限申请说明对话框
 */
public class UserPrivacySafeDialog extends DialogFragment {

    private static final String TAG = UserPrivacySafeDialog.class.getSimpleName();
    private String content;

    public static UserPrivacySafeDialog newInstance(String content) {
        UserPrivacySafeDialog dialog = new UserPrivacySafeDialog();
        if(content != null){
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            dialog.setArguments(bundle);
        }
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Pro_Round_Dialog);
        content = getArguments().getString("content", getString(R.string.pro_privacy_safe_tip));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_privacy_safe, container);

        TextView tv = rootView.findViewById(R.id.tv);
        tv.setText(content);
        rootView.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            if(listener!=null){
                listener.ok();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        return rootView;
    }

    private IListener listener;

    public void setListener(IListener listener){
        this.listener = listener;
    }

    public interface IListener{
        public void ok();
    }
}
