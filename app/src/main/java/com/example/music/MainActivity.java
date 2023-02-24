package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import phucdv.android.musichelper.MediaHelper;
import phucdv.android.musichelper.Song;

public class MainActivity extends AppCompatActivity {

    private SongViewModel songViewModel;

    private MusicController musicController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fcv_list_song,ListSongFragment.class,null)
                    .add(R.id.fcv_play_bar,MusicPlayBarFragment.class,null)
                    .commit();
        }
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
        } else {
            doRetrieveAllSong();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 999) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                doRetrieveAllSong();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("initialized",true);
        outState.putParcelable("musicController",musicController);
        super.onSaveInstanceState(outState);
    }

    private void doRetrieveAllSong(){
        MediaHelper.retrieveAllSong(this, new MediaHelper.OnFinishRetrieve() {
            @Override
            public void onFinish(List<Song> list) {
                songViewModel.setListSong(list);
                Log.d("Ktl", "onFinish: Call retrieve all song");
                initMusicController();
            }

        });
    }

    private void initMusicController(){

        // Music Controller

        musicController = new MusicController(getApplicationContext(), new MusicController.MusicSource() {
            @Override
            public int getSize() {
                return songViewModel.getSize();
            }

            @Override
            public Song getAtIndex(int index) {
                return songViewModel.getAtIndex(index);
            }
        });

        songViewModel.getLiveCurrentSongIndex().observe(this,curSongIndex ->{
            if(!songViewModel.getIsFirst()){
                musicController.playSongAt(this,curSongIndex);
                songViewModel.togglePlaying(true);
            }

        });
        songViewModel.getLiveIsPlaying().observe(this,isPlaying->{
            if(!isPlaying){
                musicController.pause();
            }else{
                if(songViewModel.getIsFirst()){
                    songViewModel.setIsFirst(false);
                    songViewModel.setCurrentSongIndex(0);
                }else{
                    musicController.start();
                }
            }
        });

    }

}