package com.example.music_list;

import static java.lang.Integer.parseInt;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity2 extends AppCompatActivity implements View.OnClickListener {
    private static SeekBar sb2;
    private static TextView tv_progress2,tv_total2,name_song2;
    private ObjectAnimator animator2;
    private MusicService2.MusicControl2 musicControl2;
    private String name2;
    private Intent intent21,intent22;
    private MyServiceConn2 conn2;
    private boolean isUnbind2 = false;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_music2);
        intent21 = getIntent();
        init();
    }
    private void init(){
        tv_progress2 = (TextView) findViewById(R.id.tv_progress2);
        tv_total2 = (TextView) findViewById(R.id.tv_total2);
        sb2 = (SeekBar) findViewById(R.id.sb2);
        name_song2 = (TextView) findViewById(R.id.song_name2);
        findViewById(R.id.btn_play2).setOnClickListener(this);
        findViewById(R.id.btn_pause2).setOnClickListener(this);
        findViewById(R.id.btn_continue_play2).setOnClickListener(this);
        findViewById(R.id.btn_exit2).setOnClickListener(this);
        name2 = intent21.getStringExtra("name");
        name_song2.setText(name2);
        intent22 = new Intent(this,MusicService2.class);
        conn2 = new MyServiceConn2();
        bindService(intent22,conn2,BIND_AUTO_CREATE);

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == seekBar.getMax()){
                    animator2.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                musicControl2.seekTo(progress);
            }
        });

        ImageView iv_music2 = (ImageView) findViewById(R.id.iv_music2);
        String position = intent21.getStringExtra("position");
        int i = parseInt(position);
        iv_music2.setImageResource(frag2.icons2[i]);

        animator2 = ObjectAnimator.ofFloat(iv_music2,"rotation",0f,360.0f);
        animator2.setDuration(10000);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setRepeatCount(-1);
    }
    public static Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");

            sb2.setMax(duration);
            sb2.setProgress(currentPosition);

            int minute = duration/1000/60;
            int second = duration/1000%60;
            String strMinute = null;
            String strSecond = null;
            if(minute<10){
                strMinute = "0"+minute;
            }
            else {
                strMinute = minute+"";
            }
            if(second<10){
                strSecond = "0"+second;
            }
            else{
                strSecond = second+"";
            }
            tv_total2.setText(strMinute+":"+strSecond);

            minute = currentPosition/1000/60;
            second = currentPosition/1000%60;
            if(minute<10){
                strMinute = "0"+minute;
            }
            else{
                strMinute = minute+" ";
            }
            if(second<10){
                strSecond="0"+second;
            }
            else{
                strSecond = second+" ";
            }
            tv_progress2.setText(strMinute+":"+strSecond);
        }
    };
    class MyServiceConn2 implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            musicControl2 = (MusicService2.MusicControl2) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name){

        }
    }
    private void unbind(boolean isUnbind2){
        if(!isUnbind2){
            musicControl2.pausePlay();
            unbindService(conn2);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_play2:
                String position = intent21.getStringExtra("position");
                int i =parseInt(position);
                musicControl2.play(i);
                animator2.start();
                break;
            case R.id.btn_pause2:
                musicControl2.pausePlay();
                animator2.pause();
                break;
            case R.id.btn_continue_play2:
                musicControl2.continuePlay();
                animator2.start();
                break;
            case R.id.btn_exit2:
                unbind(isUnbind2);
                isUnbind2 = true;
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbind(isUnbind2);
    }
}
