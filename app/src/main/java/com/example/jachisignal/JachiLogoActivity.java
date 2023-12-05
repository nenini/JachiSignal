package com.example.jachisignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.jachisignal.databinding.ActivityJachiLogoBinding;

public class JachiLogoActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2500;
    private static final int IMAGE_CHANGE_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityJachiLogoBinding binding=ActivityJachiLogoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 이미지를 변경하고 싶은 리소스 ID로 수정
                binding.plug.setImageResource(R.drawable.plugin);

                // 1초 후에 MainActivity로 이동
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(JachiLogoActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish(); // 현재 액티비티 종료
                    }
                }, IMAGE_CHANGE_DELAY);
            }
        }, SPLASH_DISPLAY_LENGTH - IMAGE_CHANGE_DELAY);
    }
}