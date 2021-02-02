package com.developer.vrp.audioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView tv_start_time,tv_end_time;
    SeekBar seek_bar;
    ImageView iv_forword,iv_play,iv_pause,iv_backword;

    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Music Player");
        initViews();
    }

    private void initViews() {
        tv_start_time=findViewById(R.id.tv_start_time);
        tv_end_time=findViewById(R.id.tv_end_time);
        seek_bar=findViewById(R.id.seek_bar);
        iv_forword=findViewById(R.id.iv_forword);
        iv_play=findViewById(R.id.iv_play);
        iv_pause=findViewById(R.id.iv_pause);
        iv_backword=findViewById(R.id.iv_backword);

        //initialize media player
        mediaPlayer=MediaPlayer.create(this, R.raw.feelings);
        //initialize runnable
        runnable=new Runnable() {
            @Override
            public void run() {
                //set progress on seek bar
                seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };
        //get duration of media player
        int duration=mediaPlayer.getDuration();
        //convert millisecond to minute and second
        String sDuration=convertFormat(duration);
        //set duration on text view
        tv_end_time.setText(sDuration);

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_play.setVisibility(View.GONE);
                iv_pause.setVisibility(View.VISIBLE);

                mediaPlayer.start();
                seek_bar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });
        iv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_pause.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });
        iv_forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration!=currentPosition){
                    currentPosition=currentPosition+5000;
                    tv_start_time.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        iv_backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && currentPosition>5000){
                    currentPosition=currentPosition-5000;
                    tv_start_time.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                }
                tv_start_time.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                iv_pause.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
