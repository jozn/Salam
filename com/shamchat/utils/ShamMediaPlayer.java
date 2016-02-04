package com.shamchat.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class ShamMediaPlayer {
    private static ShamMediaPlayer player;
    private int count;
    private final Handler handler;
    public OnProgressUpdateListener listener;
    private MediaPlayer mediaPlayer;
    OnCompletionListener onCompletionLstener;
    public OnPlayingCompletetionListner playCompletionlistner;

    public interface OnPlayingCompletetionListner {
        void onCompletion$4ce696ce();
    }

    public interface OnProgressUpdateListener {
        void onProgessUpdate(int i);
    }

    /* renamed from: com.shamchat.utils.ShamMediaPlayer.1 */
    class C11741 extends AsyncTask<String, Integer, String> {
        C11741() {
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            if (ShamMediaPlayer.this.mediaPlayer.isPlaying()) {
                ShamMediaPlayer.this.mediaPlayer.stop();
                ShamMediaPlayer.this.mediaPlayer.reset();
            }
            try {
                ShamMediaPlayer.this.mediaPlayer = new MediaPlayer();
                ShamMediaPlayer.this.mediaPlayer.setOnCompletionListener(ShamMediaPlayer.this.onCompletionLstener);
                ShamMediaPlayer.this.mediaPlayer.setDataSource(str);
                ShamMediaPlayer.this.mediaPlayer.prepare();
                ShamMediaPlayer.this.mediaPlayer.start();
                ShamMediaPlayer.this.startPlayProgressUpdater();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e2) {
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }

        protected final /* bridge */ /* synthetic */ void onProgressUpdate(Object[] objArr) {
            Integer[] numArr = (Integer[]) objArr;
            if (ShamMediaPlayer.this.listener != null) {
                ShamMediaPlayer.this.listener.onProgessUpdate(numArr[0].intValue());
            }
        }

        private String doInBackground(String... params) {
            Exception e;
            String result = BuildConfig.VERSION_NAME;
            File downloadedFile = null;
            try {
                String strUrl = params[0];
                URL url = new URL(strUrl);
                File folder = new File(Environment.getExternalStorageDirectory() + "/salam/audio");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File downloadedFile2 = new File(folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + strUrl.substring(strUrl.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1));
                try {
                    if (downloadedFile2.exists()) {
                        System.out.println("This file has been already downloaded " + downloadedFile2.getName());
                        publishProgress(new Integer[]{Integer.valueOf(100)});
                        downloadedFile = downloadedFile2;
                    } else {
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        int lenghtOfFile = conection.getContentLength();
                        InputStream input = new BufferedInputStream(url.openStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                        OutputStream output = new FileOutputStream(downloadedFile2);
                        byte[] data = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                        long total = 0;
                        while (true) {
                            if (ShamMediaPlayer.this.count = input.read(data) == -1) {
                                break;
                            }
                            total += (long) ShamMediaPlayer.this.count;
                            int precentage = (int) ((100 * total) / ((long) lenghtOfFile));
                            publishProgress(new Integer[]{Integer.valueOf(precentage)});
                            output.write(data, 0, ShamMediaPlayer.this.count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                        downloadedFile = downloadedFile2;
                    }
                } catch (Exception e2) {
                    e = e2;
                    downloadedFile = downloadedFile2;
                    Log.e("Error: ", e.getMessage());
                    if (downloadedFile != null) {
                        return result;
                    }
                    return downloadedFile.getAbsolutePath();
                }
            } catch (Exception e3) {
                e = e3;
                Log.e("Error: ", e.getMessage());
                if (downloadedFile != null) {
                    return downloadedFile.getAbsolutePath();
                }
                return result;
            }
            if (downloadedFile != null) {
                return downloadedFile.getAbsolutePath();
            }
            return result;
        }
    }

    /* renamed from: com.shamchat.utils.ShamMediaPlayer.2 */
    class C11752 implements Runnable {
        C11752() {
        }

        public final void run() {
            ShamMediaPlayer.this.startPlayProgressUpdater();
        }
    }

    /* renamed from: com.shamchat.utils.ShamMediaPlayer.3 */
    class C11763 implements OnCompletionListener {
        C11763() {
        }

        public final void onCompletion(MediaPlayer mp) {
            if (ShamMediaPlayer.this.playCompletionlistner != null) {
                ShamMediaPlayer.this.playCompletionlistner.onCompletion$4ce696ce();
            }
            if (ShamMediaPlayer.this.mediaPlayer.isPlaying()) {
                ShamMediaPlayer.this.mediaPlayer.stop();
                ShamMediaPlayer.this.mediaPlayer.reset();
            }
        }
    }

    static {
        player = new ShamMediaPlayer();
    }

    public static ShamMediaPlayer getInstance() {
        return player;
    }

    public ShamMediaPlayer() {
        this.handler = new Handler();
        this.onCompletionLstener = new C11763();
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnCompletionListener(this.onCompletionLstener);
    }

    public final void play(String url) {
        new C11741().execute(new String[]{url});
    }

    public final void startPlayProgressUpdater() {
        if (this.mediaPlayer.isPlaying()) {
            this.handler.postDelayed(new C11752(), 1000);
            return;
        }
        this.mediaPlayer.pause();
    }

    public final void stop() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
        }
    }
}
