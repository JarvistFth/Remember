package com.example.jarvist.remember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

public class launchActivity extends AppCompatActivity {

    private Button registerBtn;
    private Button loginBtn;
    private String currentUserName;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(launchActivity.this,RegisterActivity.class);
                startActivity(intent);
                launchActivity.this.finish();


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(launchActivity.this,LoginActivity.class);
                startActivity(intent);
                launchActivity.this.finish();
            }
        });



    }

    @Override
    protected void onResume(){
        super.onResume();
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 跳转到首页
            Intent intent = new Intent(launchActivity.this,MainActivity.class);
            currentUserEmail = AVUser.getCurrentUser().getUsername();
            currentUserEmail = AVUser.getCurrentUser().getEmail();
            Bundle bundle = new Bundle();
            bundle.putString("currentUserName",currentUserName);
            bundle.putString("currentUserEmail",currentUserEmail);
            intent.putExtras(bundle);
            startActivity(intent);


        }
    }
}
