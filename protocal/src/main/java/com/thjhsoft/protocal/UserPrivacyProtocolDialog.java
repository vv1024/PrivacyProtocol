package com.thjhsoft.protocal;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * 用户安全隐私声明
 */
public class UserPrivacyProtocolDialog extends DialogFragment {

    private static final String TAG = UserPrivacyProtocolDialog.class.getSimpleName();

    private String content;
    public static UserPrivacyProtocolDialog newInstance(String content) {
        UserPrivacyProtocolDialog dialog = new UserPrivacyProtocolDialog();
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
        View rootView = inflater.inflate(R.layout.dialog_privacy_protocol, container);
        TextView tv = rootView.findViewById(R.id.tv);
        // support scroll move
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        if(content == null){
            tv.setText(read());
        }else{
            tv.setText(content);
        }

        rootView.findViewById(R.id.btn_agree).setOnClickListener(view -> {
            if(listener!=null){
                listener.agree();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        TextView tvTitle = rootView.findViewById(R.id.tv_title0);
        tvTitle.setText(R.string.pro_privacy_protocol);

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        return rootView;
    }


    private String read(){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.privacy_protocol), "utf-8"))) {
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
        public void agree();
    }
}
