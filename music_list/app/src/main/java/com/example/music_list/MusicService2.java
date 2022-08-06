package com.example.music_list;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService2 extends Service {
    private MediaPlayer player2;
    private Timer timer2;

    public MusicService2() {}
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicService2.MusicControl2();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        player2=new MediaPlayer();
    }
    public void addTimer(){
        if(timer2==null){
            timer2 = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(player2 == null) return;
                    int duration = player2.getDuration();
                    int currentPosition = player2.getCurrentPosition();
                    Message msg = MusicActivity2.handler2.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);

                    msg.setData(bundle);
                    MusicActivity2.handler2.sendMessage(msg);
                }
            };
            timer2.schedule(task,5,500);
        }
    }
    class MusicControl2 extends Binder {
        public void play(int i){
            Uri uri2 = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"musics"+(i+1));
            try{
                player2.reset();
                player2 = MediaPlayer.create(getApplicationContext(),uri2);
                player2.start();
                addTimer();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void pausePlay(){
            player2.pause();
        }
        public void continuePlay(){
            player2.start();
        }
        public void seekTo(int progress){
            player2.seekTo(progress);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(player2 == null)return;;
        if(player2.isPlaying()) player2.stop();
        player2.release();
        player2 = null;
    }
}
