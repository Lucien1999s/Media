package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Button video_page;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10;
    Button btn11,btn12,btn13,btn14,btn15,btn16,btn17,btn18,btn19,btn20,btn21,btn22,btn23,btn24,btn25;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.one);
        btn2 = (Button) findViewById(R.id.two);
        btn3 = (Button) findViewById(R.id.three);
        btn4 = (Button) findViewById(R.id.four);
        btn5 = (Button) findViewById(R.id.five);
        btn6 = (Button) findViewById(R.id.six);
        btn7 = (Button) findViewById(R.id.seven);
        btn8 = (Button) findViewById(R.id.eight);
        btn9 = (Button) findViewById(R.id.nine);
        btn10 = (Button) findViewById(R.id.ten);
        btn11 = (Button) findViewById(R.id.eleven);
        btn12 = (Button) findViewById(R.id.twelve);
        btn13 = (Button) findViewById(R.id.thirteen);
        btn14 = (Button) findViewById(R.id.fourteen);
        btn15 = (Button) findViewById(R.id.fifteen);
        btn16 = (Button) findViewById(R.id.sixteen);
        btn17 = (Button) findViewById(R.id.seventeen);
        btn18 = (Button) findViewById(R.id.eighteen);
        btn19 = (Button) findViewById(R.id.nineteen);
        btn20 = (Button) findViewById(R.id.twenty);
        btn21 = (Button) findViewById(R.id.twentyone);
        btn22 = (Button) findViewById(R.id.twentytwo);
        btn23 = (Button) findViewById(R.id.twentythree);
        btn24 = (Button) findViewById(R.id.twentyfour);
        btn25 = (Button)findViewById(R.id.twentyfive);
        video_page=(Button)findViewById(R.id.video_page);

        video_page.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
        btn13.setOnClickListener(this);
        btn14.setOnClickListener(this);
        btn15.setOnClickListener(this);
        btn16.setOnClickListener(this);
        btn17.setOnClickListener(this);
        btn18.setOnClickListener(this);
        btn19.setOnClickListener(this);
        btn20.setOnClickListener(this);
        btn21.setOnClickListener(this);
        btn22.setOnClickListener(this);
        btn23.setOnClickListener(this);
        btn24.setOnClickListener(this);
        btn25.setOnClickListener(this);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentLayout,new Onefragment());
        ft.commit();
        mediaPlayer = MediaPlayer.create(this,R.raw.m1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        video_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,video_page.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        switch (v.getId()){
            case R.id.one:
                ft.replace(R.id.fragmentLayout,new Onefragment());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m1);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.two:
                ft.replace(R.id.fragmentLayout,new Twofragment());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m2);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.three:
                ft.replace(R.id.fragmentLayout,new Threefragment());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m3);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.four:
                ft.replace(R.id.fragmentLayout,new fragment4());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m4);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.five:
                ft.replace(R.id.fragmentLayout,new fragment5());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m5);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.six:
                ft.replace(R.id.fragmentLayout,new fragment6());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m6);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.seven:
                ft.replace(R.id.fragmentLayout,new fragment7());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m7);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.eight:
                ft.replace(R.id.fragmentLayout,new fragment8());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m8);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.nine:
                ft.replace(R.id.fragmentLayout,new fragment9());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m9);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.ten:
                ft.replace(R.id.fragmentLayout,new fragment10());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m10);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.eleven:
                ft.replace(R.id.fragmentLayout,new fragment11());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m11);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twelve:
                ft.replace(R.id.fragmentLayout,new fragment12());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m12);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.thirteen:
                ft.replace(R.id.fragmentLayout,new fragment13());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m13);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.fourteen:
                ft.replace(R.id.fragmentLayout,new fragment14());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m14);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.fifteen:
                ft.replace(R.id.fragmentLayout,new fragment15());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m15);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.sixteen:
                ft.replace(R.id.fragmentLayout,new fragment16());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m16);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.seventeen:
                ft.replace(R.id.fragmentLayout,new fragment17());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m17);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.eighteen:
                ft.replace(R.id.fragmentLayout,new fragment18());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m18);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.nineteen:
                ft.replace(R.id.fragmentLayout,new fragment19());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m19);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twenty:
                ft.replace(R.id.fragmentLayout,new fragment20());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m20);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twentyone:
                ft.replace(R.id.fragmentLayout,new fragment21());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m21);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twentytwo:
                ft.replace(R.id.fragmentLayout,new fragment22());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m22);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twentythree:
                ft.replace(R.id.fragmentLayout,new fragment23());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m23);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twentyfour:
                ft.replace(R.id.fragmentLayout,new fragment24());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m24);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            case R.id.twentyfive:
                ft.replace(R.id.fragmentLayout,new fragment25());
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(this,R.raw.m25);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;
            default:
                break;
        }
        ft.commit();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.three){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        mediaPlayer.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.pause();
    }
    public void jump3(View view){
        Uri uri = Uri.parse("https://8book.com/novelbooks/121051/");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}