package com.example.jarvist.remember;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NAME = 0;//MainActivity
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private List<Note> deleteList;
    private NoteAdapter adapter;
    private ActionBar actionBar;
    public int mul_Tag;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        ImageButton deletebtn = (ImageButton)findViewById(R.id.menu1);
        TextView title = (TextView)findViewById(R.id.title);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        cb = (CheckBox)findViewById(R.id.checkbox);
        CardView cardView = (CardView)findViewById(R.id.cardview);
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

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<noteList.size();i++)
                {

                }

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

    /*@Override
    public void onBackPressed(){
        Intent intent = getIntent();
        mul_Tag = intent.getIntExtra("MUL_TAG",1);
        if(mul_Tag == NoteAdapter.MUL_TAG)
        {
            cb.setVisibility(View.INVISIBLE);
            mul_Tag = 0;
        }
        else
        super.onBackPressed();

    }*/


}
