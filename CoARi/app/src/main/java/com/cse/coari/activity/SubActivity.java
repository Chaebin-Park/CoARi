package com.cse.coari.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cse.coari.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class SubActivity extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    Button btn;
    YouTubePlayer.OnInitializedListener listener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        setRoomName();

        btn = (Button)findViewById(R.id.youtubeBtn);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeView);


        listener = new YouTubePlayer.OnInitializedListener() {

            String ID = setRoomName();
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.e("WHYNOT", " NOPE!");
                youTubePlayer.loadVideo(ID); //
                //https://www.youtube.com/watch?v=NmkYHmiNArc 유투브에서 v="" 이부분이 키에 해당

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        if(btn!=null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youTubePlayerView.initialize("AIzaSyCsu1kctw1UAxUAISZJQ7rVyhGMtmCSdFg", listener);

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
//        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }


    private String setRoomName(){
        Intent intent = getIntent();
        String myData = intent.getStringExtra("info");
        String vedioID = null;

        TextView textView = (TextView)findViewById(R.id.textview);

        if(myData.compareTo("0") == 0){
            textView.setText("901는 박유현 교수님 연구실입니다.");
            vedioID = "o8nNm466ghc";
        } else if(myData.compareTo("1") == 0){
            textView.setText("902은 비어있는 공간입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("2") == 0){
            textView.setText("903은 권오준 교수님 연구실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("3") == 0){
            textView.setText("904은 권순각 교수님 연구실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("4") == 0){
            textView.setText("905은 김성우 교수님 연구실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("5") == 0){
            textView.setText("906은 이중화 교수님 연구실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("6") == 0){
            textView.setText("907은 존경하고 사랑하고 아끼고 보고싶은 이종민 교수님 연구실입니다..");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("7") == 0){
            textView.setText("908은 공실 입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("8") == 0){
            textView.setText("910은 학생회실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("9") == 0){
            textView.setText("911은 학생들을 위한 실습실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("10") == 0){
            textView.setText("912은 학생들이 공부하는 공간입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("11") == 0){
            textView.setText("913은 학생들이 공부하는 공간입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("12") == 0){
            textView.setText("914은 학생들이 공부하는 공간입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("13") == 0){
            textView.setText("915은 학생들을 위한 실습실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("14") == 0){
            textView.setText("916은 학생들을 위한 실습실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("15") == 0){
            textView.setText("917은 학과사무실입니다..");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("16") == 0){
            textView.setText("918은 학생들을 위한 실습실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("17") == 0){
            textView.setText("919-1은 서버실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("18") == 0){
            textView.setText("919은 랩실입니다.");
            vedioID = "lM49lekPR6k";
        }
        else if(myData.compareTo("19") == 0){
            textView.setText("920은 랩실입니다.");
            vedioID = "lM49lekPR6k";
        }
        return vedioID;
    }
}