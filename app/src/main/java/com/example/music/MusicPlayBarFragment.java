package com.example.music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import phucdv.android.musichelper.Song;

public class MusicPlayBarFragment extends Fragment {

    private SongViewModel songViewModel;

    private ImageView ivThumbnailSong;

    private TextView tvSongName;

    private TextView tvSongArtist;

    private TextView tvCurrentTime;

    private ProgressBar pbTimeLine;

    private TextView tvTime;

    private ImageView ivNext;

    private ImageView ivPlay;

    private ImageView ivPrev;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_play_bar, container, false);

        view.setOnClickListener(v->{

        });
        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);

        initView(view);

        songViewModel.getLiveCurrentSongIndex().observe(getViewLifecycleOwner(),curSongIndex ->{
            updatePlayBar(curSongIndex);
            updateAction(curSongIndex);
        });
        songViewModel.getLiveIsPlaying().observe(getViewLifecycleOwner(),isPlaying ->{
            if(isPlaying){
                ivPlay.setImageResource(R.drawable.pause);
            }else{
                ivPlay.setImageResource(R.drawable.play);
            }
        });
        return view;
    }

    private void initView(View view){
        ivThumbnailSong = view.findViewById(R.id.iv_thumbnail_music);
        tvSongName = view.findViewById(R.id.tv_song_name);
        tvSongArtist = view.findViewById(R.id.tv_song_artist);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        pbTimeLine = view.findViewById(R.id.pb_timeLine);
        tvTime = view.findViewById(R.id.tv_time);
        ivNext = view.findViewById(R.id.iv_next);
        ivPlay = view.findViewById(R.id.iv_play);
        ivPrev = view.findViewById(R.id.iv_prev);
    }

    private void updatePlayBar(Integer songIndex){
        Log.d("Ktl", "updatePlayBar: "+songIndex);
        Song song = songViewModel.getAtIndex(songIndex);
        if(song != null){
            ivThumbnailSong.setImageURI(song.getAlbumUri());
            tvSongName.setText(song.getTitle());
            tvSongArtist.setText(song.getArtist());
            tvCurrentTime.setText("00:00");
//        pbTimeLine;
            tvTime.setText(song.getFormatTimes());
        }else{
            tvSongName.setText("<Song Name>");
            tvSongArtist.setText("<Song Artist>");
            tvCurrentTime.setText("00:00");
//        pbTimeLine;
            tvTime.setText("00:00");
        }

    }
    private void updateAction(Integer songIndex){
        ivNext.setOnClickListener(v ->{
            songViewModel.setCurrentSongIndex(songIndex +1);
        });
        ivPlay.setOnClickListener(v ->{
            songViewModel.togglePlaying();
        });
        ivPrev.setOnClickListener(v->{
            songViewModel.setCurrentSongIndex(songIndex -1);
        });
    }

}