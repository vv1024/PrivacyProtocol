package com.thjhsoft.protocal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ChildrenModeDialog extends DialogFragment {
    private static final String TAG = ChildrenModeDialog.class.getSimpleName();
    private String content;

    private NestedScrollView scrollView;
    private LinearLayout passwordView;
    private EditText etPwd;
    private TextView btnOpen;

    public static ChildrenModeDialog newInstance(String content) {
        ChildrenModeDialog dialog = new ChildrenModeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Pro_Round_Dialog);
        content = getArguments().getString("content", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_children_protocol, container);

        scrollView = rootView.findViewById(R.id.scrollView);
        passwordView = rootView.findViewById(R.id.password_view);
        etPwd = rootView.findViewById(R.id.et_password);

        passwordView.setVisibility(View.GONE);

        TextView tv = rootView.findViewById(R.id.tv);
        // support scroll move
        //tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        if(content == null){
            tv.setText(read());
        }else{
            tv.setText(content);
        }
        btnOpen = rootView.findViewById(R.id.btn_open);
        btnOpen.setOnClickListener(view -> {
            if(scrollView.getVisibility() == View.VISIBLE){
                scrollView.setVisibility(View.GONE);
                passwordView.setVisibility(View.VISIBLE);
                btnOpen.setText(R.string.pro_ok);
            }else{
                String password = etPwd.getText().toString();
                if(!TextUtils.isEmpty(password)){
                    ChildrenModeUtils.getInstance().openChildrenMode(getContext(),password);
                    if(listener!=null){
                        listener.opened();
                    }
                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }


        });

        rootView.findViewById(R.id.btn_close).setOnClickListener(view -> {
            if(listener!=null){
                listener.ignore();
            }
            Objects.requireNonNull(getDialog()).dismiss();
        });

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        getDialog().setCancelable(true);

        return rootView;
    }


    private String read(){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.children_mode), "utf-8"))) {
            sb = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private IListener listener;

    public void setListener(IListener listener){
        this.listener = listener;
    }

    public interface IListener{
        public void opened();
        public void ignore();
    }


}
