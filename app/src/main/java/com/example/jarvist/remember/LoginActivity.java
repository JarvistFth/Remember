package com.example.jarvist.minilock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText accountText;
    private EditText passwordText;
    private Button loginBtn;
    private Button forgetBtn;
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountText = (EditText)findViewById(R.id.editAccount);
        passwordText = (EditText)findViewById(R.id.editPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        forgetBtn = (Button)findViewById(R.id.forgetBtn);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = accountText.getText().toString();
                password = passwordText.getText().toString();
                AVUser.logInInBackground(account, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(e == null){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this,forgetPswActivity.class);
                startActivity(intent1);
                LoginActivity.this.finish();
            }
        });

    }
}
