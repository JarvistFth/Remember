package com.example.jarvist.remember;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int status;
    public static final int WRITE = 1;
    public static final int READ = 2;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private List<Note> deleteList;
    private NoteAdapter adapter;
    private ActionBar actionBar;
    private boolean showboxTag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        final ImageButton deletebtn = (ImageButton)findViewById(R.id.menu1);
        TextView title = (TextView)findViewById(R.id.title);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setBackgroundColor(Color.YELLOW);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu3);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        noteList = DataSupport.order("date desc").find(Note.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                status = 1;
                intent.putExtra("Status", status);
                startActivity(intent);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("删除便签");
                dialog.setMessage("您确定要删除所选便签？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<Integer,Boolean> map = adapter.getMap();
                        for(int i=map.size()-1; i>=0; i--){
                            if(map.get(i)){
                                int id = noteList.get(i).getId();
                                deleteData(id);
                                noteList.remove(i);
                                adapter.notifyItemRemoved(i);

                            }

                        }
                        adapter.notifyItemRangeChanged(0,noteList.size());
                        adapter.MUL_tag  = false;
                        adapter.initMaps();
                        showboxTag = false;
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                dialog.show();



            }
        });

        deletebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    deletebtn.setBackgroundColor(getResources().getColor(R.color.colorbg1));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    deletebtn.setBackgroundColor(getResources().getColor(R.color.colorbg2));
                return false;
            }
        });

        adapter = new NoteAdapter(noteList);
        adapter.setRecycleViewOnItemClickListener(new NoteAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                Note note = noteList.get(position);
                status = 2;
                intent.putExtra("Status", status);
                intent.putExtra("Content",note);
                startActivity(intent);


            }

            @Override
            public boolean onLongClickListener(View view, int position) {
                adapter.setSelection(position);
                adapter.setCheckBox();
                adapter.notifyDataSetChanged();
                showboxTag = true;
                return true;
            }
        });
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume(){
        super.onResume();
        status = 0;
        noteList.clear();
        List<Note> newList = DataSupport.order("date desc").find(Note.class);
        noteList.addAll(newList);
        adapter.MUL_tag  = false;
        adapter.initMaps();
        showboxTag = false;
        adapter.notifyDataSetChanged();
    }

    public void deleteData(int id){
        DataSupport.deleteAll(Note.class,"id = ?",String.valueOf(id));

    }
    @Override
    public void onBackPressed(){
        if(showboxTag){
            adapter.MUL_tag  = false;
            adapter.initMaps();
            adapter.notifyDataSetChanged();
            showboxTag = false;
        }
        else
            super.onBackPressed();
    }


}
