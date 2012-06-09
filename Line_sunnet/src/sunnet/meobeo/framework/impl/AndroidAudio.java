package sunnet.meobeo.framework.impl;

import java.io.IOException;

import sunnet.meobeo.framework.Audio;
import sunnet.meobeo.framework.Music;
import sunnet.meobeo.framework.Sound;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {
	AssetManager assets;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
	}
	
	public Sound newSound(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool,soundId);
		}catch(IOException e) {
			throw new RuntimeException("Could not load sound"+fileName);
		}
	}

	@Override
	public Music newMusic(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
}
