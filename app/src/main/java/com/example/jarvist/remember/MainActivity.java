package com.example.jarvist.remember;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int status;//判断读写
    public static final int WRITE = 1;// 1是新增便签，2是读取已有便签
    public static final int READ = 2;
    private RecyclerView recyclerView;
    private List<AVObject> noteList = new ArrayList<>();
    private NoteAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView nav_View;
    private TextView nickName;
    private TextView email;
    private FloatingActionButton fab;
    private View nav_headerLayout;
    private  ImageButton deletebtn;
    private String currentUserName ;
    private String currentEmail ;
    private boolean showboxTag = false;//判断是否显示CheckBox
    private long mExitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            currentUserName = AVUser.getCurrentUser().getUsername();
            currentEmail = AVUser.getCurrentUser().getEmail();
            nickName.setText(currentUserName);
            email.setText(currentEmail);
        }
        else{
            startActivity(new Intent(MainActivity.this,launchActivity.class));
        }
        nav_View.setCheckedItem(R.id.nav_account);
        nav_View.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_account:
                        Toast.makeText(MainActivity.this,"nav_account",Toast.LENGTH_SHORT);
                        break;
                    case R.id.nav_notification:
                        break;
                    case R.id.nav_logOut:
                        AVUser.logOut();// 清除缓存用户对象
                        Intent intent = new Intent(MainActivity.this,launchActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                        break;// 现在的 currentUser 是 null 了
                    case R.id.finish:
                        System.exit(0);
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                status = 1;
                intent.putExtra("Status", status);//传递信息
                startActivity(intent);
            }
        });

        deletebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //delete键按下后背景发生变化
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    deletebtn.setBackgroundColor(getResources().getColor(R.color.colorbg1));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    deletebtn.setBackgroundColor(getResources().getColor(R.color.colorbg2));
                return false;
            }
        });

        //创建adapter
        adapter = new NoteAdapter(noteList);
        //设置监听
        adapter.setRecycleViewOnItemClickListener(new NoteAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                AVObject note = noteList.get(position);
                status = 2;
                Bundle bundle = new Bundle();
                bundle.putInt("Status", status);
                bundle.putString("Content",note.getString("Content"));
                bundle.putString("objectId",note.getObjectId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
            //长按时间
            @Override
            public boolean onLongClickListener(View view, int position) {
                //长按显示CheckBox，并且长按位置选中该便签吖
                adapter.setSelection(position);
                adapter.setCheckBox();
                adapter.notifyDataSetChanged();
                showboxTag = true;
                return true;
            }
        });
        //瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    protected void initViews(){
        deletebtn = (ImageButton)findViewById(R.id.delete);
        deletebtn.setVisibility(View.VISIBLE);
        deletebtn.setOnClickListener(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        nav_View = (NavigationView)findViewById(R.id.nav_view);
        nav_headerLayout = nav_View.inflateHeaderView(R.layout.nav_header);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        nickName = (TextView)nav_headerLayout.findViewById(R.id.nickname);
        email = (TextView)nav_headerLayout.findViewById(R.id.mail);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            //左上角菜单键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu3);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData(){
        noteList.clear();
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("UserName",currentUserName);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    noteList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
                else
                    e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);//创建dialog
                dialog.setTitle("删除便签");
                dialog.setMessage("您确定要删除所选便签？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取保存通过CheckBox选中的便签，选中状态由adapter中的map保存，通过getMap()获取
                        Map<Integer,Boolean> map = adapter.getMap();
                        //从后往前删除
                        for(int i=map.size()-1; i>=0; i--){
                            if(map.get(i)){
                                String objectId = "'"+noteList.get(i).getObjectId()+"'";
                                String cql = "delete from Note where objectId="+objectId;
                                AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //i为CheckBox选中的view的position
                                //noteList中移除
                                noteList.remove(i);
                                //adapter重新设置item
                                adapter.notifyItemRemoved(i);
                            }
                        }
                        //adapter长度重新设置
                        adapter.notifyItemRangeChanged(0,noteList.size());
                        //删除后回到正常状态，CheckBox不显示，map重新归false
                        adapter.MUL_tag  = false;
                        adapter.initMaps();
                        showboxTag = false;
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//取消dialog
                    }
                });
                dialog.show();
        }
    }

    //resume
    @Override
    protected void onResume(){
        super.onResume();
        status = 0;
        initData();
        //将新list加入到noteList中
        //adapter中CheckBox初始化
        //status归零
        adapter.MUL_tag  = false;
        adapter.initMaps();
        showboxTag = false;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            else if(showboxTag){
                adapter.MUL_tag  = false;
                adapter.initMaps();
                adapter.notifyDataSetChanged();
                showboxTag = false;
            }

            else{
                if(System.currentTimeMillis() - mExitTime > 2000){
                    Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                }
                else{
                    System.exit(0);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
