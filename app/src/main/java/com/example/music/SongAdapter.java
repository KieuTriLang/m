package com.example.music;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import phucdv.android.musichelper.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private List<Song> listSong = new ArrayList<>();

    private int selectedIndex = -1;

    private OnClickSongItem onClickSongItem;

    public void setListSong(List<Song> list){
        this.listSong = list;
    }

    public void setOnClickSongItem(OnClickSongItem onClickSongItem){
        this.onClickSongItem = onClickSongItem;
    }

    public void setSelectedIndex (int index){
        this.selectedIndex = index;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item,parent,false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.bindView(listSong.get(position),position);
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{

        private ImageView ivThumbnail;
        private TextView tvSongName;
        private TextView tvSongArtist;
        private ImageView ivPlay;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail_music);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSongArtist = itemView.findViewById(R.id.tv_song_artist);
            ivPlay = itemView.findViewById(R.id.iv_play);
            itemView.setOnClickListener(v ->{
                if(onClickSongItem != null){
                    onClickSongItem.onClick(this,getLayoutPosition());
                }
            });
        }

        public void bindView(Song song,int pos){
            tvSongName.setText(song.getTitle());
            tvSongArtist.setText(song.getArtist());
            ivThumbnail.setImageURI(song.getAlbumUri());
            if(selectedIndex == pos){
                ivPlay.setImageResource(R.drawable.musical_note);
            }else{
                ivPlay.setImageDrawable(null);
            }
        }

        public void clearIvPlay(){
            ivPlay.setImageDrawable(null);
        }
        public void setPlayBtn(){
            ivPlay.setImageResource(R.drawable.musical_note); }

        public void startAnimation(int pos){
            setPlayBtn();
//            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.infinity_rotate_animation);
//            animation.setDuration(listSong.get(pos).getMillisTimes());
//            ivPlay.startAnimation(animation);
        }
        public void clearAnimation(){
            ivThumbnail.clearAnimation();
        }
    }

    public interface OnClickSongItem{
        void onClick(SongHolder holder,int pos);
    }
}
