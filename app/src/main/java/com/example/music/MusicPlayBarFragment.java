package com.example.music;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

import phucdv.android.musichelper.Song;

public class MusicPlayBarFragment extends Fragment {

    private SongViewModel songViewModel;

    private ImageView ivThumbnailSong;

    private TextView tvSongName;

    private TextView tvSongArtist;

    private TextView tvCurrentTime;

    private SeekBar sbTimeLine;

    private TextView tvTime;

    private ImageView ivNext;

    private ImageView ivPlay;

    private ImageView ivPrev;

    private ImageView ivPlayMode;

    private ImageView ivPlayFlow;

    private Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_play_bar, container, false);

        view.setOnClickListener(v->{

        });
        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);

        initView(view);

        songViewModel.getLiveCurrentSongIndex().observe(getViewLifecycleOwner(),curSongIndex ->{
            if(Objects.equals(songViewModel.getLivePlayFlow().getValue(), MediaPlayerProp.FLOW_SHUFFLE)){
                curSongIndex = songViewModel.getIndexSongInShuffle(curSongIndex);
            }
            updatePlayBar(curSongIndex);
            updateAction(curSongIndex);
            if(curSongIndex != -1){
                handler.removeCallbacks(runnable);
                updateTimeline();
            }
        });
        songViewModel.getLiveIsPlaying().observe(getViewLifecycleOwner(),isPlaying ->{
            if(isPlaying){
                ivPlay.setImageResource(R.drawable.pause);
                updateTimeline();
            }else{
                ivPlay.setImageResource(R.drawable.play);
                handler.removeCallbacks(runnable);
            }
        });

        songViewModel.getLivePlayFlow().observe(getViewLifecycleOwner(),flow->{
            int curSongIndex = songViewModel.getLiveCurrentSongIndex().getValue();
            if(flow.equals(MediaPlayerProp.FLOW_SHUFFLE)){
                curSongIndex = songViewModel.getIndexSongInShuffle(curSongIndex);
            }
            updateAction(curSongIndex);
        });

        return view;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int curTime = songViewModel.getMusicController().getCurrentPositionOfMP();
            tvCurrentTime.setText(formatTime(curTime));
            updateTimeline();
        }
    };

    private void initView(View view){
        ivThumbnailSong = view.findViewById(R.id.iv_thumbnail_music);
        tvSongName = view.findViewById(R.id.tv_song_name);
        tvSongArtist = view.findViewById(R.id.tv_song_artist);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        sbTimeLine = view.findViewById(R.id.sb_timeLine);
        tvTime = view.findViewById(R.id.tv_time);
        ivNext = view.findViewById(R.id.iv_next);
        ivPlay = view.findViewById(R.id.iv_play);
        ivPrev = view.findViewById(R.id.iv_prev);
        ivPlayMode = view.findViewById(R.id.iv_play_mode);
        ivPlayFlow = view.findViewById(R.id.iv_play_flow);
        setEventSeekBar();
    }

    private void setEventSeekBar(){
        sbTimeLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MediaPlayer mediaPlayer = songViewModel.getMusicController().getMediaPlayer();
                int duration = mediaPlayer.getDuration();
                if(mediaPlayer!= null && b){
                    if(songViewModel.getLiveCurrentSongIndex().getValue() != -1){
                        mediaPlayer.seekTo((duration/100)*i);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateTimeline(){
        int curTime = songViewModel.getMusicController().getCurrentPositionOfMP();
        int duration = songViewModel.getMusicController().getDurationOfMP();
        sbTimeLine.setProgress((int)( (curTime /(float) duration) * 100));

        handler.postDelayed(runnable,1000);
    }

    private void updatePlayBar(Integer songIndex){
        Song song = songViewModel.getAtIndex(songIndex);
        if(song != null){
            ivThumbnailSong.setImageURI(song.getAlbumUri());
            tvSongName.setText(song.getTitle());
            tvSongArtist.setText(song.getArtist());
            tvCurrentTime.setText("00:00");
            sbTimeLine.setMax(100);
            tvTime.setText(song.getFormatTimes());
        }else{
            tvSongName.setText("<Song Name>");
            tvSongArtist.setText("<Song Artist>");
            tvCurrentTime.setText("00:00");
//        pbTimeLine;
            tvTime.setText("00:00");
        }
        if(songViewModel.getPlayMode().equals(MediaPlayerProp.MODE_REPEAT_ALL)){
            ivPlayMode.setImageResource(R.drawable.repeat_all);
        }else{
            ivPlayMode.setImageResource(R.drawable.repeat_one);
        }

        if(Objects.equals(songViewModel.getLivePlayFlow().getValue(), MediaPlayerProp.FLOW_LINEAR)){
            ivPlayFlow.setImageResource(R.drawable.parallel);
        }else{
            ivPlayFlow.setImageResource(R.drawable.shuffle);
        }

    }
    private void updateAction(int songIndex){
        boolean isShuffle = Objects.equals(songViewModel.getLivePlayFlow().getValue(), MediaPlayerProp.FLOW_SHUFFLE);
        ivNext.setOnClickListener(v ->{
            songViewModel.setIsBeforePlaying(false);
            songViewModel.getMusicController().setFalseIsNextSong();
            int nextSongIndex = isShuffle ? songViewModel.getNextSongFromIndexOfShuffle(songIndex) : songIndex+1;
            songViewModel.setCurrentSongIndex(nextSongIndex);
        });
        ivPlay.setOnClickListener(v ->{
            songViewModel.setIsBeforePlaying(false);
            songViewModel.getMusicController().setFalseIsNextSong();
            songViewModel.togglePlaying();
        });
        ivPrev.setOnClickListener(v->{
            songViewModel.setIsBeforePlaying(false);
            songViewModel.getMusicController().setFalseIsNextSong();
            int prevSongIndex = isShuffle ? songViewModel.getPrevSongFromIndexOfShuffle(songIndex) : songIndex-1;
            songViewModel.setCurrentSongIndex(prevSongIndex);
        });
        ivPlayMode.setOnClickListener(v->{
            if(songViewModel.getPlayMode().equals(MediaPlayerProp.MODE_REPEAT_ALL)){
                ivPlayMode.setImageResource(R.drawable.repeat_one);
            }else{
                ivPlayMode.setImageResource(R.drawable.repeat_all);
            }
            songViewModel.togglePlayMode();
        });
        ivPlayFlow.setOnClickListener(v->{
            if(songViewModel.getLivePlayFlow().getValue().equals(MediaPlayerProp.FLOW_LINEAR)){
                ivPlayFlow.setImageResource(R.drawable.shuffle);
            }else{
                ivPlayFlow.setImageResource(R.drawable.parallel);
            }
            songViewModel.togglePlayFlow();
        });
    }

    private String formatTime(long time) {
        long millis = time;
        millis /= 1000L;
        long minutes = millis / 60L;
        long sec = millis % 60L;
        return minutes + ":" + (sec < 10 ? "0"+sec:sec);
    }
}