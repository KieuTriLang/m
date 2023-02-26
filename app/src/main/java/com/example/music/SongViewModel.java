package com.example.music;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import phucdv.android.musichelper.Song;

public class SongViewModel extends ViewModel {

    private MusicController musicController;
    private final MutableLiveData<List<Song>> listSong = new MutableLiveData<>(new ArrayList<>());

    private final List<Integer> shuffleSongIndex = new ArrayList<>();

    private final MutableLiveData<Integer> currentSongIndex = new MutableLiveData<>(-1);

    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);

    private MediaPlayerProp playMode = MediaPlayerProp.MODE_REPEAT_ALL;

    private MutableLiveData<MediaPlayerProp> playFlow = new MutableLiveData<>(MediaPlayerProp.FLOW_LINEAR);

    private Boolean isBeforePlaying = false;

// SET
    public void togglePlayMode(){
        if(playMode.equals(MediaPlayerProp.MODE_REPEAT_ALL)){
            playMode = MediaPlayerProp.MODE_REPEAT_ONE;
        }else{
            playMode = MediaPlayerProp.MODE_REPEAT_ALL;
        }
    }
    public void togglePlayFlow(){
        if(Objects.equals(playFlow.getValue(), MediaPlayerProp.FLOW_LINEAR)){
            playFlow.setValue(MediaPlayerProp.FLOW_SHUFFLE);
        }else{
            playFlow.setValue(MediaPlayerProp.FLOW_LINEAR);
        }
    }
    public void setMusicController(MusicController musicController){
        this.musicController = musicController;
    }
    public void setIsBeforePlaying(boolean isBeforePlaying){
        this.isBeforePlaying = isBeforePlaying;
    }
    public void setListSong(List<Song> list){

        listSong.setValue(list);
        for (int i = 0; i < list.size(); i++) {
            shuffleSongIndex.add(i);
        }
        Collections.shuffle(shuffleSongIndex);
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
    public int getIndexSongInShuffle(int index){
        return shuffleSongIndex.indexOf(index);
    }
    public int getNextSongFromIndexOfShuffle(int indexInShuffle){
        int nextIndexInShuffle = indexInShuffle + 1 >= shuffleSongIndex.size() ? 0 : indexInShuffle + 1;
        return shuffleSongIndex.get(nextIndexInShuffle);
    }
    public int getPrevSongFromIndexOfShuffle(int indexInShuffle){
        int prevIndexInShuffle = indexInShuffle - 1 < 0 ? shuffleSongIndex.size()-1 : indexInShuffle - 1;
        return shuffleSongIndex.get(prevIndexInShuffle);
    }
    public MediaPlayerProp getPlayMode(){
        return this.playMode;
    }
    public LiveData<MediaPlayerProp> getLivePlayFlow(){
        return this.playFlow;
    }
    public MusicController getMusicController(){
        return this.musicController;
    }
    public boolean getIsBeforePlaying(){
        return this.isBeforePlaying;
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
        if(index == -1){
            return null;
        }
        int size = Objects.requireNonNull(listSong.getValue()).size();
        if(playFlow.equals(MediaPlayerProp.FLOW_SHUFFLE)){
            index = shuffleSongIndex.get(index);
        }
        if(index >= 0 && index < size){
            return Objects.requireNonNull(listSong.getValue()).get(index);
        }
        return null;
    }

}
