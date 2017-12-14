package com.example.jarvist.minilock;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText numberText;
    private EditText passwordText;
    private EditText mailText;
    private String nickName;
    private String password;
    private String email;
    private Button registerEnsure;
    private TextView sendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        numberText = (EditText)findViewById(R.id.editnum);
        mailText = (EditText)findViewById(R.id.mail) ;
        passwordText = (EditText)findViewById(R.id.password);
        registerEnsure = (Button)findViewById(R.id.registerEnsure);
        sendMail = (TextView)findViewById(R.id.sendMailTextView) ;
        sendMail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        registerEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName = numberText.getText().toString();
                password = passwordText.getText().toString();
                email = mailText.getText().toString();

                AVUser user = new AVUser();
                user.setUsername(nickName);
                user.setPassword(password);
                user.setEmail(email);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                            Toast.makeText(RegisterActivity.this,"注册成功……即将跳转到主页面",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.requestEmailVerifyInBackground(email,new RequestEmailVerifyCallback(){

                    @Override
                    public void done(AVException e){
                        if(e == null){
                            Toast.makeText(RegisterActivity.this,"发送邮件成功，请前往邮箱进行激活",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"邮件发送失败，请点击重新发送邮件",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

}
