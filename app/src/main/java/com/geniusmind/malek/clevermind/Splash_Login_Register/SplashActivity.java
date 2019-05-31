package com.geniusmind.malek.clevermind.Splash_Login_Register;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.geniusmind.malek.clevermind.HomeScreen.HomeActivity;
import com.geniusmind.malek.clevermind.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME=2000;
    FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);

        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // create expression if user was logging before then intent to home screen instead of login screen . . . ;
    if(mFirebaseUser==null){
                startActivity(LoginActivity.newIntent(SplashActivity.this));
                finish();
    }
    else {startActivity(HomeActivity.newIntent(SplashActivity.this));
    finish();
    }
            }
        },SPLASH_TIME);
    }
}
