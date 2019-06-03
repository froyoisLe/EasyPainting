package site.isleti.painting.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

import site.isleti.painting.R;

public class MusicService extends Service {

    //为日志工具设置标签
    private static String TAG = "MusicService";
    //定义音乐播放器变量
    private MediaPlayer mPlayer;

    //该服务不存在需要被创建时被调用，不管startService()还是bindService()都会启动时调用该方法
    @Override
    public void onCreate() {

        Log.e(TAG, "MusicService onCreate()");
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zazie);
        //设置可以重复播放
        mPlayer.setLooping(true);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "MusicService onStart()");
        mPlayer.start();
        super.onStart(intent, startId);
    }
    @Override
    public void onDestroy() {

        Log.e(TAG, "MusicService onDestroy()");

        mPlayer.stop();

        super.onDestroy();
    }
    //其他对象通过bindService 方法通知该Service时该方法被调用
    @Override
    public IBinder onBind(Intent intent) {

        Log.e(TAG, "MusicService onBind()");

        mPlayer.start();

        return null;
    }
    //其它对象通过unbindService方法通知该Service时该方法被调用
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "MusicService onUnbind()");
        mPlayer.stop();
        return super.onUnbind(intent);
    }
}
