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
import java.util.Map;

/**
 * Created by Jarvist on 2017/8/24.
 */

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.ViewHolder>
            implements View.OnClickListener,View.OnLongClickListener{

    private List<Note> mNoteList;

    private Context mContext;

    private RecyclerViewOnItemClickListener onItemClickListener;
    //multiple choice
    public boolean MUL_tag = false;
    //checkbox situation map
    private HashMap<Integer,Boolean> ischecked = new HashMap<Integer, Boolean>();


    public NoteAdapter (List<Note> noteList){
        mNoteList = noteList;
        initMaps();
    }

    public void initMaps(){
        for(int i = 0; i <mNoteList.size(); i++){
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

    @Override
    public ViewHolder onCreateViewHolder (final ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.note_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.cardView.setOnClickListener(this);
        viewHolder.cardView.setOnLongClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Note note = mNoteList.get(position);
        String values = note.getContent();
        holder.contentView.setText(values);
        holder.dateView.setText(new SimpleDateFormat("yyyy/MM/dd    HH:mm:ss").format(note.getDate()));
        if(MUL_tag) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        holder.cardView.setTag(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischecked.put(position,isChecked);
            }
        });
        if(ischecked.get(position) == null)
            ischecked.put(position,false);
        holder.checkBox.setChecked(ischecked.get(position));
    }

    @Override
    public int getItemCount(){
        return mNoteList.size();
    }

    @Override
    public void onClick(View v){
        if(onItemClickListener != null)
        {
            onItemClickListener.onItemClickListener(v,(Integer)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v){
        initMaps();
        return onItemClickListener != null && onItemClickListener.onLongClickListener(v,(Integer)v.getTag());
        }

    public void setRecycleViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setCheckBox(){
        MUL_tag = !MUL_tag;
    }

    public void setSelection(int position){
        if(ischecked.get(position))
            ischecked.put(position,false);
        else
            ischecked.put(position,true);
        notifyItemChanged(position);
    }

    public HashMap<Integer,Boolean> getMap(){
        return ischecked;
    }

    public interface RecyclerViewOnItemClickListener
    {
        void onItemClickListener(View view,int position);
        boolean onLongClickListener(View view, int position);
    }

}
