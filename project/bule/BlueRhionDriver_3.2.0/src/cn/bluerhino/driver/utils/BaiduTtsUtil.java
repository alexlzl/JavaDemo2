package cn.bluerhino.driver.utils;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.DataInfoUtils;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechLogger;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.controller.receiver.PushStateContext;
import cn.bluerhino.driver.model.PushInfo;

/**
 * Describe:百度离线语音
 * 
 * Date:2015-8-4
 * 
 * Author:liuzhouliang
 */
public class BaiduTtsUtil implements SpeechSynthesizerListener {
	private SpeechSynthesizer speechSynthesizer;
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	private PushInfo mPushInfo;
	private static final String TAG = BaiduTtsUtil.class.getSimpleName();

	public BaiduTtsUtil(Context context) {
		mContext = context;
		init();
	}

	private void init() {
		System.loadLibrary("gnustl_shared");
		// 部分版本不需要BDSpeechDecoder_V1
		try {
			System.loadLibrary("BDSpeechDecoder_V1");
		} catch (UnsatisfiedLinkError e) {
			SpeechLogger.logD("load BDSpeechDecoder_V1 failed, ignore");
			new UploadErrorMessage().uploadErrorMessage(ConstantsManager.TTS_ERROR, e.toString());;
		}
		System.loadLibrary("bd_etts");
		System.loadLibrary("bds");

		speechSynthesizer = SpeechSynthesizer.newInstance(SpeechSynthesizer.SYNTHESIZER_AUTO,
				mContext.getApplicationContext(), "holder", this);
		// 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
		speechSynthesizer.setApiKey("uQRqc87yQ6GAcFoW9n0PA650", "Kr2LPX6uz7oGK1523wlfQxUUFK45wLKO");
		// 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
		speechSynthesizer.setAppId("6347647");
		// TTS所需的资源文件，可以放在任意可读目录，可以任意改名
		String ttsTextModelFilePath = mContext.getApplicationContext().getApplicationInfo().dataDir
				+ "/lib/libbd_etts_text.dat.so";
		String ttsSpeechModelFilePath = mContext.getApplicationContext().getApplicationInfo().dataDir
				+ "/lib/libbd_etts_speech_female.dat.so";
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ttsTextModelFilePath);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ttsSpeechModelFilePath);
		DataInfoUtils.verifyDataFile(ttsTextModelFilePath);
		DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_DATE);
		DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_SPEAKER);
		DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_GENDER);
		DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_CATEGORY);
		DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_LANGUAGE);
		this.setParams();
		speechSynthesizer.initEngine();
	}

	public void speak(String content) {
		speechSynthesizer.initEngine();
		if (!StringUtil.isEmpty(content)) {
			AudioManagerUtil.setStreamMusicMaxVolume(mContext);
			speechSynthesizer.speak(content);
		}
	}

	public void speak(String content, PushInfo pushInfor) {
		mPushInfo = pushInfor;
		speak(content);
	}

	private void setParams() {
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
		// 音量，取值0-9，默认为5中音量
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
		// 语速，取值0-9，默认为5中语速
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "6");
		// 音调，取值0-9，默认为5中语调
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "7");
		// mp3压缩（仅在线引擎）
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, "1");
		// mp3压缩的16k（仅在线引擎）
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, "4");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE, "ZH");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
	}

	@Override
	public void onCancel(SpeechSynthesizer arg0) {
	}

	@Override
	public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
		LogUtil.d(TAG, "语音错误code:" + arg1.code + "---语音错误description---" + arg1.description);
		new UploadErrorMessage().uploadErrorMessage(ConstantsManager.TTS_ERROR, "语音错误code:" + arg1.code + "---语音错误description---" + arg1.description);
		if (mPushInfo != null) {
			playVoice();
		}
	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1, boolean arg2) {
	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer arg0) {
	}

	@Override
	public void onSpeechPause(SpeechSynthesizer arg0) {
	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
	}

	@Override
	public void onSpeechResume(SpeechSynthesizer arg0) {
	}

	@Override
	public void onSpeechStart(SpeechSynthesizer arg0) {
	}

	@Override
	public void onStartWorking(SpeechSynthesizer arg0) {
	}

	@Override
	public void onSynthesizeFinish(SpeechSynthesizer arg0) {
	}

	/**
	 * 
	 * Describe:调用SDK失败，播放本地音频文件
	 * 
	 * Date:2015-8-27
	 * 
	 * Author:liuzhouliang
	 */
	private void playVoice() {
		int voiceId = getVoiceId();
		if (voiceId != 0) {
			mMediaPlayer = MediaPlayer.create(mContext, voiceId); //
			// 设置音频源
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置流类型
			mMediaPlayer.setLooping(false); // 设置是否循环播放
			mMediaPlayer.start(); // 开始播放
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					mPushInfo = null;
				}
			});
			mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mp.release();
					mPushInfo = null;
					return true;
				}
			});
		}

	}

	private int getVoiceId() {
		String stateAction = mPushInfo.getAction();
		if (TextUtils.equals(stateAction, PushStateContext.HASNEWORDER)) {
			/**
			 * 指派订单
			 */
			return R.raw.newnotification;
		} else if (TextUtils.equals(stateAction, PushStateContext.HASMODIFYEDORDER)) {
			/**
			 * 订单发生变化
			 */
			return R.raw.changenotification;

		} else if (TextUtils.equals(stateAction, PushStateContext.SNATCHORDERSUCCEED)) {
			/**
			 * 抢单成功
			 */
			return R.raw.snatchordersucceed;

		} else if (TextUtils.equals(stateAction, PushStateContext.HASREARRANGEDORDER)) {
			/**
			 * 订单被改派
			 */
			return R.raw.changenotification;

		} else if (TextUtils.equals(stateAction, PushStateContext.HASCANCLEDORDER)) {
			/**
			 * 订单被取消
			 */
			return R.raw.cancelnotification;
		} else {
			return 0;
		}
	}
}
