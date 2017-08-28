package cn.bluerhino.driver.utils;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

public class AudioPlayer {

	// private final static String TAG = "AudioPlayer";

	public static final int PLAY_ERROR_MP3 = 0;

	private static AudioPlayer mAudioPlayer = new AudioPlayer();

	private MediaPlayer mediaPlayer;

	private boolean isStop;

	/**
	 * 实例播放器
	 * 
	 * @return {@link AudioPlayer}
	 */
	public static AudioPlayer getInstance() {
		return mAudioPlayer;
	}

	private AudioPlayer() {
	}

	/**
	 * 判断是否播放
	 * 
	 * @return
	 */
	public boolean isPlaying() {
		if (isStop) {
			return false;
		}
		return mediaPlayer != null && mediaPlayer.isPlaying();
	}

	/**
	 * 停止播放
	 * 
	 */
	public AudioPlayer stop() {
		isStop = true;
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		return this;
	}

	/**
	 * 释放播放器资源
	 */
	public AudioPlayer release() {
		if (mediaPlayer != null) {
			// mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
			mAudioPlayer = null;
		}
		return this;
	}

	/**
	 * 播放
	 * 
	 * @param rawResId
	 *        播放音频资源
	 * @param context
	 * @return {@link AudioPlayer}
	 */
	public synchronized AudioPlayer play(int rawResId, Context context) {
		isStop = false;
		if (rawResId != 0) {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				
			} 
			if (mediaPlayer != null)
			{
				try {
	                mediaPlayer.setDataSource(context,
	                        Uri.parse("android.resource://cn.bluerhino.driver/" + rawResId));
                } catch (IllegalArgumentException e) {
	                e.printStackTrace();
                } catch (SecurityException e) {
	                e.printStackTrace();
                } catch (IllegalStateException e) {
	                e.printStackTrace();
                } catch (IOException e) {
	                e.printStackTrace();
                }
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				try {
	                mediaPlayer.prepare();
	                mediaPlayer.start();
	                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					mediaPlayer.setVolume(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
	                		am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
					
                } catch (IllegalStateException e) {
	                e.printStackTrace();
                } catch (IOException e) {
	                e.printStackTrace();
                }
			}
		}
		return this;
	}

}
