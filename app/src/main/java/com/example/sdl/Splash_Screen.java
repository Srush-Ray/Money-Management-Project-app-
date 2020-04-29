package com.example.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        logo=findViewById(R.id.logoss);
        Animation myanimation= AnimationUtils.loadAnimation(this,R.anim.myanim);
        logo.startAnimation(myanimation);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(Splash_Screen.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(Splash_Screen.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        },1500);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }

}
