package com.example.jarvist.remember;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jarvist on 2017/8/24.
 */

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.ViewHolder>  {

    private List<Note> mNoteList;

    public static final int NAME = 1;
    private Context mContext;
    //multiple choice
    private boolean MUL_tag = false;
    //checkbox situation map
    private HashMap<Integer,Boolean> ischecked;

    public static final int MUL_TAG = 1;



    public void initMaps(){
        for(int i = 0; i <mNoteList.size(); i++){
            ischecked = new HashMap<Integer, Boolean>();
            ischecked.put(i,false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView contentView;
        TextView dateView;
        CheckBox checkBox;

        public ViewHolder(View v){
            super(v);
            cardView = (CardView)v.findViewById(R.id.cardview);
            contentView = (TextView)v.findViewById(R.id.content);
            dateView = (TextView)v.findViewById(R.id.date);
            checkBox = (CheckBox)v.findViewById(R.id.checkbox);

        }
    }

    public NoteAdapter (List<Note> noteList){
        mNoteList = noteList;
        initMaps();
    }




    @Override
    public ViewHolder onCreateViewHolder (final ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.note_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Note note = mNoteList.get(position);
                Intent intent = new Intent(mContext,WriteActivity.class);
                intent.putExtra("ActivityName",NAME);
                intent.putExtra("Content",note);
                mContext.startActivity(intent);

            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ischecked.put(position,true);
                viewHolder.checkBox.setChecked(ischecked.get(position));
                /*Intent intent = new Intent(mContext,MainActivity.class);
                intent.putExtra("MUL_TAG",MUL_TAG);
                mContext.startActivity(intent);*/
                MUL_tag = true;
                NoteAdapter.super.notifyDataSetChanged();
                return true;
            }
        });



        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = viewHolder.getAdapterPosition();
                Note note = mNoteList.get(position);
                note.setChecked(isChecked);
                note.update(note.getId());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int positon){
        Note note = mNoteList.get(positon);
        String values = note.getContent();
        holder.contentView.setText(values);
        holder.dateView.setText(new SimpleDateFormat("yyyy/MM/dd    HH:mm:ss").format(note.getDate()));
        if(MUL_tag) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount(){
        return mNoteList.size();
    }



}
