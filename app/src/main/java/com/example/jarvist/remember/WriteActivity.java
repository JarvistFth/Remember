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

import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private int noteID;
    private int status;
    private EditText contentText;
    private ImageButton menuView;

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
        if(status == MainActivity.READ){
            Note note = (Note)intent.getSerializableExtra("Content");
            contentText.setText(note.getContent());
            contentText.setSelection(note.getContent().length());
            noteID = note.getId();
        }

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
                if(status == MainActivity.WRITE){
                    Intent intent1 = new Intent(WriteActivity.this,MainActivity.class);
                    startActivity(intent1);
                    saveData();
                }
                else if(status == MainActivity.READ){
                    Intent intent1 = new Intent(WriteActivity.this,MainActivity.class);
                    startActivity(intent1);
                    updateData();

                }
                finish();
            }
        });
    }

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
            Note note = new Note();
            note.setDate(new Date());
            note.setContent(contentText.getText().toString());
            note.update(noteID);
        }

    }

    public void saveData(){
        if(!(contentText.getText().toString().equals("")) ){
            Note note = new Note();
            note.setDate(new Date());
            note.setContent(contentText.getText().toString());
            note.save();
        }

    }
}
