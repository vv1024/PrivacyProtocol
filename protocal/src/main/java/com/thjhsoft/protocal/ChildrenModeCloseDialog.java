package com.thjhsoft.protocal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Map;
import java.util.Objects;

public class ChildrenModeCloseDialog extends DialogFragment {
    private static final String TAG = ChildrenModeCloseDialog.class.getSimpleName();

    private EditText etPwd;

    public static ChildrenModeCloseDialog newInstance() {
        return new ChildrenModeCloseDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Pro_Round_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_children_close_protocol, container);

        etPwd = rootView.findViewById(R.id.et_password);

        rootView.findViewById(R.id.btn_close).setOnClickListener(view -> {
            String password = etPwd.getText().toString();
            if(ChildrenModeUtils.getInstance().closeChildrenMode(getContext(), password)){
                Toast.makeText(getContext(), getString(R.string.pro_quit_child_mode), Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();
                if(listener != null){
                    listener.closed();
                }
            }else{
                Toast.makeText(getContext(), getString(R.string.pro_pwd_wrong), Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.btn_back).setOnClickListener(view -> {
            if(listener != null){
                listener.ignore();
            }
            Objects.requireNonNull(getDialog()).dismiss();
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
        public void closed();
        public void ignore();
    }

}
