package com.example.jarvist.remember;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private String noteID;
    private int status;
    private EditText contentText;
    private ImageButton menuView;
    private AVUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        contentText = (EditText)findViewById(R.id.editText);
        menuView = (ImageButton) findViewById(R.id.menu2);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Intent intent = getIntent();
        status = intent.getIntExtra("Status",0);
        Bundle bundle = intent.getExtras();
        if(status == MainActivity.READ){
            //读操作

            //显示内容和游标放到内容末端
            if(bundle.getString("Content") != null)
            contentText.setText(bundle.getString("Content"));
            contentText.setSelection(bundle.getString("Content").length());
            //获取id
            noteID = bundle.getString("objectId");
        }
        //按钮按下背景变化
        menuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    menuView.setBackgroundColor(getResources().getColor(R.color.colorbg1));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    menuView.setBackgroundColor(getResources().getColor(R.color.colorbg2));
                return false;
            }
        });

        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增便签
                if(status == MainActivity.WRITE){
                    saveData();
                    Intent intent1 = new Intent(WriteActivity.this,MainActivity.class);
                    startActivity(intent1);

                }
                else if(status == MainActivity.READ){
                    //读取便签后更新数据
                    updateData();
                    Intent intent1 = new Intent(WriteActivity.this,MainActivity.class);
                    startActivity(intent1);


                }
                finish();
            }
        });
    }

    //左上角home键
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }


    public void updateData(){
        if(!(contentText.getText().toString().equals(""))){
            AVObject note = AVObject.createWithoutData("Note",noteID);
            currentUser = AVUser.getCurrentUser();
            note.put("Content",contentText.getText().toString());
            note.put("UserName",currentUser.getUsername());
            note.saveInBackground();
        }

    }

    public void saveData(){
        if(!(contentText.getText().toString().equals("")) ){
            AVObject note = new AVObject("Note");
            currentUser = AVUser.getCurrentUser();
            note.put("Content",contentText.getText().toString());
            note.put("UserName",currentUser.getUsername());
            note.saveInBackground();
        }

    }
}
