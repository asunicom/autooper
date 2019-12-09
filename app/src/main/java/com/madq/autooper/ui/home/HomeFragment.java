package com.madq.autooper.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.madq.autooper.BuildConfig;
import com.madq.autooper.R;
import com.madq.autooper.service.VideoAccessibilityService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private Button button;
    private TextView textView;
    private TextView tvVersion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tvVersion = root.findViewById(R.id.tv_version);
        textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VideoAccessibilityService.isStart()) {
                    try {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } catch (Exception e) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        e.printStackTrace();
                    }
                }
            }
        });

        tvVersion.setText("版本号：" + BuildConfig.VERSION_NAME);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (VideoAccessibilityService.isStart()) {
            textView.setText("辅助已打开");
            button.setText("快去刷视频吧");
        } else {
            textView.setText("辅助已关闭");
            button.setText("去打开辅助");
        }
        checkValid();
    }

    private void checkValid() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            button.setVisibility(View.INVISIBLE);
            textView.setText("不支持你手机，用不了用不了");
        }
    }
}