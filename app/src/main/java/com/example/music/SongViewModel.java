package com.example.music;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import phucdv.android.musichelper.Song;

public class SongViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> listSong = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Integer> currentSongIndex = new MutableLiveData<>(-1);

    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);

    private Boolean isFirst = true;

// SET

    public void setIsFirst(boolean isFirst){
        this.isFirst = isFirst;
    }
    public void setListSong(List<Song> list){

        listSong.setValue(list);
    }
    public void togglePlaying(){
        isPlaying.setValue(Boolean.FALSE.equals(isPlaying.getValue()));
    }
    public void togglePlaying(boolean state){
        isPlaying.setValue(state); }
    public void setCurrentSongIndex(int index){
        int size = Objects.requireNonNull(listSong.getValue()).size();
        if(index < 0){
            currentSongIndex.setValue(size -1);
        }else if(index == size){
            currentSongIndex.setValue(0);
        }else{
            currentSongIndex.setValue(index);
        }

}

    // GET
    public boolean getIsFirst(){
        return this.isFirst;
    }
    public LiveData<List<Song>> getLiveListSong(){
        return listSong;
    }
    public LiveData<Integer> getLiveCurrentSongIndex(){

        return currentSongIndex;
    }
    public LiveData<Boolean> getLiveIsPlaying(){
        return isPlaying;
    }
    public int getSize(){
        return Objects.requireNonNull(listSong.getValue()).size();
    }

    public Song getAtIndex(int index){
        int size = Objects.requireNonNull(listSong.getValue()).size();
        if(index >= 0 && index < size){
            return Objects.requireNonNull(listSong.getValue()).get(index);
        }
        return null;
    }

}
