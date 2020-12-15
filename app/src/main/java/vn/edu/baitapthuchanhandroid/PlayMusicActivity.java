package vn.edu.baitapthuchanhandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import vn.edu.baitapthuchanhandroid.adapters.MusicItemAdapter;
import vn.edu.baitapthuchanhandroid.entities.Music;
import vn.edu.baitapthuchanhandroid.services.MyService;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnBack, btnRandom, btnSkipPrevious, btnSkipNext, btnPlayPause, btnRepeat;
    private TextView tvNameMusic, tvNameSinger, tvCurrentTime, tvTotalTime;
    private ImageView avatarMusic;
    private SeekBar progressMusic;

    private MyService myService;
    private boolean isBound = false;
    private boolean isStart = false;
    private ServiceConnection connection;

    private boolean isRandom = false, isPlaying = false;
    private int repeatMode = 0;
    private Intent intent;
    private Handler handlerUpdateCurrentTime;
    private Runnable runnableUpdateCurrentTime;
    private int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        position = getIntent().getIntExtra("music", 0);

        btnBack = findViewById(R.id.btn_back);
        btnRandom = findViewById(R.id.btn_random_music);
        btnSkipNext = findViewById(R.id.btn_skip_next_music);
        btnSkipPrevious = findViewById(R.id.btn_skip_previous_music);
        btnPlayPause = findViewById(R.id.btn_play_pause_music);
        btnRepeat = findViewById(R.id.btn_repeat_music);

        tvNameMusic = findViewById(R.id.music_name);
        tvNameSinger = findViewById(R.id.singer_name);
        tvCurrentTime = findViewById(R.id.current_time_music);
        tvTotalTime = findViewById(R.id.total_time_music);

        avatarMusic = findViewById(R.id.avatar_music);
        progressMusic = findViewById(R.id.progress_music);

        tvNameMusic.setText(MusicItemAdapter.musics.get(position).getName());
        tvNameSinger.setText(MusicItemAdapter.musics.get(position).getSingerName());
        avatarMusic.setImageResource(MusicItemAdapter.musics.get(position).getAvatar());

        avatarMusic.setClipToOutline(true);
        RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(10000);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);

        avatarMusic.startAnimation(anim);

        btnRandom.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSkipNext.setOnClickListener(this);
        btnSkipPrevious.setOnClickListener(this);

        // Khởi tạo ServiceConnection
        connection = new ServiceConnection() {

            // Phương thức này được hệ thống gọi khi kết nối tới service bị lỗi
            @Override
            public void onServiceDisconnected(ComponentName name) {

                isBound = false;
            }

            // Phương thức này được hệ thống gọi khi kết nối tới service thành công
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder binder = (MyService.MyBinder) service;
                myService = binder.getService(); // lấy đối tượng MyService
                isBound = true;
                if (myService.getLooping()) {
                    repeatMode = 2;
                    btnRepeat.setColorFilter(Color.rgb(0, 168, 142));
                    btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                }
                myService.setMusic(MusicItemAdapter.musics.get(position).getMusicResource());
                myService.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.d("repeate_mode", String.valueOf(repeatMode));
                        if (repeatMode == 2) return;
                        if (position == MusicItemAdapter.musics.size() - 1) {
                            if (repeatMode == 0) return;
                            position = 0;
                        } else {
                            position+=1;
                        }

                        if (isRandom) {
                            position = getRandomNumber(0, MusicItemAdapter.musics.size() - 1);
                        }
                        setNewMusic(position);
                    }
                });
            }
        };

        // Khởi tạo intent
        intent = new Intent(PlayMusicActivity.this,
                        MyService.class);

        bindService(intent, connection,
                Context.BIND_AUTO_CREATE);
        isStart = true;
        progressMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                long currentMinutes = TimeUnit.MILLISECONDS.toMinutes(i);
                long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(i)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(i));

                tvCurrentTime.setText((currentMinutes < 10 ? "0" + currentMinutes : currentMinutes + "") + ":" + (currentSeconds < 10 ? "0" + currentSeconds : currentSeconds + ""));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myService.fastForward();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myService.setCurrentPosition(seekBar.getProgress());
                if (isPlaying) {
                    myService.fastStart();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view.equals(btnRandom)) {
            isRandom = !isRandom;
            if (isRandom) {
                btnRandom.setColorFilter(Color.rgb(0, 168, 142));
            } else {
                btnRandom.setColorFilter(Color.rgb(210, 210, 210));
            }
        } else if (view.equals(btnRepeat)) {
            if (repeatMode == 0) {
                repeatMode = 1;
                btnRepeat.setColorFilter(Color.rgb(0, 168, 142));
                btnRepeat.setImageResource(R.drawable.ic_repeat);
                myService.setLooping(false);
            } else if (repeatMode == 1) {
                repeatMode = 2;
                btnRepeat.setColorFilter(Color.rgb(0, 168, 142));
                btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                myService.setLooping(true);
            } else if (repeatMode == 2) {
                myService.setLooping(false);
                repeatMode = 0;
                btnRepeat.setColorFilter(Color.rgb(210, 210, 210));
                btnRepeat.setImageResource(R.drawable.ic_repeat);
            }
        } else if (view.equals(btnPlayPause)) {
            long totalMillis = myService.getTotalTime();

            progressMusic.setMin(0);
            progressMusic.setMax((int) totalMillis);
            if (handlerUpdateCurrentTime == null) {

                handlerUpdateCurrentTime = new Handler();
                runnableUpdateCurrentTime = new Runnable() {
                    @Override
                    public void run() {
                        if (myService.getIsPlaying()) {
                            long currentMillis = myService.getCurrentTime();

                            progressMusic.setProgress((int) currentMillis);

                        }

                        handlerUpdateCurrentTime.postDelayed(runnableUpdateCurrentTime, 100);
                    }
                };
                runnableUpdateCurrentTime.run();
            }
            long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(totalMillis);
            long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalMillis)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis));

            tvTotalTime.setText((totalMinutes < 10 ? "0" + totalMinutes : totalMinutes + "") + ":" + (totalSeconds < 10 ? "0" + totalSeconds : totalSeconds + ""));

            isPlaying = !isPlaying;
            if (isPlaying) {
                btnPlayPause.setImageResource(R.drawable.ic_pause);
            } else {
                btnPlayPause.setImageResource(R.drawable.ic_play);
            }

            if (isPlaying) {
                if(isBound){
                    myService.fastStart();
                } else {
                    Toast.makeText(PlayMusicActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            } else if (!isPlaying) {
                if(isBound){
                    myService.fastForward();
                }else{
                    Toast.makeText(PlayMusicActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (view.equals(btnBack)) {
            finish();
        } else if (view.equals(btnSkipNext)) {
            if (position == MusicItemAdapter.musics.size() - 1) {
                position = 0;
            } else {
                position+=1;
            }

            if (isRandom) {
                position = getRandomNumber(0, MusicItemAdapter.musics.size() - 1);
            }
            setNewMusic(position);
        } else if (view.equals(btnSkipPrevious)) {
            if (position == 0) {
                position = MusicItemAdapter.musics.size() - 1;
            } else {
                position-=1;
            }

            if (isRandom) {
                position = getRandomNumber(0, MusicItemAdapter.musics.size() - 1);
            }
            setNewMusic(position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNewMusic(int index) {
        Music music = MusicItemAdapter.musics.get(index);
        myService.stopMusic();
        myService.setMusic(music.getMusicResource());
        if (isPlaying) {
            myService.fastStart();
        }
        avatarMusic.setImageResource(music.getAvatar());
        tvNameMusic.setText(music.getName());
        tvNameSinger.setText(music.getSingerName());
        long totalMillis = myService.getTotalTime();
        long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(totalMillis);
        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalMillis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis));
        progressMusic.setMin(0);
        progressMusic.setMax((int) totalMillis);
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText((totalMinutes < 10 ? "0" + totalMinutes : totalMinutes + "") + ":" + (totalSeconds < 10 ? "0" + totalSeconds : totalSeconds + ""));
        myService.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("repeate_mode", String.valueOf(repeatMode));
                if (repeatMode == 2) return;
                if (position == MusicItemAdapter.musics.size() - 1) {
                    if (repeatMode == 0) return;
                    position = 0;
                } else {
                    position+=1;
                }

                if (isRandom) {
                    position = getRandomNumber(0, MusicItemAdapter.musics.size() - 1);
                }
                setNewMusic(position);
            }
        });
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}