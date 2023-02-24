package com.example.music;
import static android.content.ContentValues.TAG;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import phucdv.android.musichelper.Song;

public class MusicController implements Parcelable {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int mCurrentIndex;
    private boolean mIsPreparing;
    private MusicSource mMusicSource;

    public interface MusicSource {
        int getSize();
        Song getAtIndex(int index);
    }

    public MusicController(Context context, MusicSource musicSource) {
        mContext = context;
        mMusicSource = musicSource;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mIsPreparing = false;
            }
        });
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.
//            }
//        });

        mCurrentIndex = -1;
        mIsPreparing = false;
    }


    protected MusicController(Parcel in) {
        mCurrentIndex = in.readInt();
        mIsPreparing = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCurrentIndex);
        dest.writeByte((byte) (mIsPreparing ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicController> CREATOR = new Creator<MusicController>() {
        @Override
        public MusicController createFromParcel(Parcel in) {
            return new MusicController(in);
        }

        @Override
        public MusicController[] newArray(int size) {
            return new MusicController[size];
        }
    };

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    public boolean isPreparing(){
        return mIsPreparing;
    }

    public void playNext() {
        if(mMusicSource.getSize() != 0) {
            if (mCurrentIndex < mMusicSource.getSize() - 1) {
                mCurrentIndex++;
            } else {
                mCurrentIndex = 0;
            }
            playSongAt(mContext, mCurrentIndex);
        }
    }

    public void playPrev() {
        if(mMusicSource.getSize() != 0) {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
            } else {
                mCurrentIndex = mMusicSource.getSize() - 1;
            }
            playSongAt(mContext, mCurrentIndex);
        }
    }

    public void pause() {

        mMediaPlayer.pause();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void playSongAt(Context context, int index) {
        mMediaPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mMusicSource.getAtIndex(index).getId());
        try {
            mMediaPlayer.setDataSource(context, trackUri);
            mCurrentIndex = index;
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error starting data source", e);
        }
        mMediaPlayer.prepareAsync();
        mIsPreparing = true;
    }

}