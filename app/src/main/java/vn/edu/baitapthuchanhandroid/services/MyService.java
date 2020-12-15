package vn.edu.baitapthuchanhandroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import vn.edu.baitapthuchanhandroid.R;
import vn.edu.baitapthuchanhandroid.adapters.MusicItemAdapter;

public class MyService extends Service {

    private MyPlayer myPlayer;
    private IBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ServiceDemo", "Đã gọi onCreate()");

        myPlayer = new MyPlayer(this);
        binder = new MyBinder(); // do MyBinder được extends Binder

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ServiceDemo", "Đã gọi onBind()");
//        myPlayer.play();
        // trả về đối tượng binder cho ActivityMain
        return binder;

    }
    // Kết thúc một Service
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("ServiceDemo", "Đã gọi onBind()");
        myPlayer.stop();
        return super.onUnbind(intent);
    }

    // Xây dựng các phương thức thực hiện nhiệm vụ,
    // ở đây mình demo phương thức tua bài hát
    public void fastForward(){

        myPlayer.fastForward(60000); // tua đến giây thứ 120
    }
    public void fastStart(){

        myPlayer.fastStart();
    }

    public int getTotalTime() {
        return myPlayer.getTotalTime();
    }

    public int getCurrentTime() {
        return myPlayer.getCurrentTime();
    }


    public boolean getIsPlaying() {
        return myPlayer.getIsPlaying();
    }

    public boolean getLooping() {
        return myPlayer.getLooping();
    }

    public void setLooping(boolean isLoop) {
        myPlayer.setLooping(isLoop);
    }

    public void setMusic(int resourceID) {
        myPlayer.setMusic(resourceID);
    }
    public void setCurrentPosition(int miliseconds) {
        myPlayer.setCurrentPosition(miliseconds);
    }
    public MediaPlayer getMediaPlayer() { return myPlayer.getMediaPlayer(); }
    public void stopMusic() {
        myPlayer.stop();
    }

    public class MyBinder extends Binder {

        // phương thức này trả về đối tượng MyService
        public MyService getService() {

            return MyService.this;
        }
    }

}

// Xây dựng một đối tượng riêng để chơi nhạc
class MyPlayer {
    // đối tượng này giúp phát một bài nhạc
    private MediaPlayer mediaPlayer;
    private Context context;

    public MyPlayer(Context context) {
        this.context = context;
        // Nạp bài nhạc vào mediaPlayer
        mediaPlayer = MediaPlayer.create(
                context, R.raw.nangamxadan);
        // Đặt chế độ phát lặp lại liên tục
        mediaPlayer.setLooping(false);
//        mediaPlayer.pause();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void fastForward(int pos){
        //mediaPlayer.seekTo(pos);
        mediaPlayer.pause();

    }
    public void fastStart(){
        mediaPlayer.start();
    }

    // phát nhạc
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // dừng phát nhạc
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public int getTotalTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }

        return 0;
    }

    public int getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }

        return 0;
    }

    public boolean getIsPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }

        return false;
    }

    public void setCurrentPosition(int milisecond) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(milisecond);
        }
    }

    public boolean getLooping() {
        return mediaPlayer.isLooping();
    }

    public void setLooping(boolean isLoop) {
        mediaPlayer.setLooping(isLoop);
    }

    public void setMusic(int resourceId) {
        mediaPlayer = MediaPlayer.create(context, resourceId);
    }
}
