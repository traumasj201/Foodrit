package app.cap.foodreet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.cap.foodreet.SignIn.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int time_over = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences mPref = getSharedPreferences("isFirst", MODE_PRIVATE);
                Boolean bfirst = mPref.getBoolean("isFirst", true);
                if (bfirst){
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putBoolean("isFirst", false).apply();
                    //처음 접속인 경우 선택
                    startActivity(new Intent(SplashActivity.this, FirstActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    //처음 접속이 아닌 경우 선택
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }, time_over);
    }
}
