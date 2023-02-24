package com.example.music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import phucdv.android.musichelper.Song;

public class ListSongFragment extends Fragment {

    private SongViewModel songViewModel;

    private SongAdapter songAdapter;

    private RecyclerView rcvListSong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);
        // View model
        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);

        // RecyclerView
        rcvListSong = view.findViewById(R.id.rcv_song);
        rcvListSong.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter
        songAdapter = new SongAdapter();
        rcvListSong.setAdapter(songAdapter);

        songViewModel.getLiveListSong().observe(getViewLifecycleOwner(),listSong->{
            songAdapter.setListSong(listSong);
        });

//        songViewModel.getLiveCurrentSongIndex().observe(getViewLifecycleOwner(),curSongIndex->{
//            resetHolder();
//            SongAdapter.SongHolder holder = (SongAdapter.SongHolder) rcvListSong.getChildViewHolder(rcvListSong.getChildAt(curSongIndex));
//            holder.setPlayBtn();
//        });

        songAdapter.setOnClickSongItem((holder,pos)->{

            if(songViewModel.getLiveCurrentSongIndex().getValue() != pos){
                songViewModel.setIsFirst(false);
                songViewModel.setCurrentSongIndex(pos);
            }else{
                songViewModel.togglePlaying();
            }
//            resetHolder();
//            holder.startAnimation(pos);

        });

        return view;
    }
    private void resetHolder(){
        for (int i = 0; i < rcvListSong.getChildCount(); i++) {
            SongAdapter.SongHolder holder = (SongAdapter.SongHolder) rcvListSong.getChildViewHolder(rcvListSong.getChildAt(i));
            holder.clearIvPlay();
        }
    }

}