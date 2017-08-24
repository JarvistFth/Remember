package com.example.jarvist.remember;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NAME = 0;//MainActivity
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter adapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        TextView title = (TextView)findViewById(R.id.title);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        noteList = DataSupport.order("date desc").find(Note.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                intent.putExtra("ActivityName",NAME);
                startActivity(intent);
            }
        });

        adapter = new NoteAdapter(noteList);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        noteList.clear();
        List<Note> newList = DataSupport.order("date desc").find(Note.class);
        noteList.addAll(newList);
        adapter.notifyDataSetChanged();
    }


}
