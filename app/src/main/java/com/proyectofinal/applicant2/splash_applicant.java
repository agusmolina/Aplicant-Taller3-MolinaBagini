package com.proyectofinal.applicant2;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_applicant extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_applicant);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(splash_applicant.this, login_inicio.class));
            }
        },100);
    }
}

