package GameEngine.Basic.Implement;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

import GameEngine.Basic.Interfaces.Audio;
import GameEngine.Basic.Interfaces.Music;
import GameEngine.Basic.Interfaces.Sound;

/**
 * Created by Quang Tung on 11/23/2015.
 */
public class AndroidAudio implements Audio {
    AssetManager asset;
    SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.asset = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music newMusic(String fileName) {
        try {
            AssetFileDescriptor desc = asset.openFd(fileName);
            return new AndroidMusic(desc);

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + fileName + "'");
        }
    }

    @Override
    public Sound newSound(String fileName) {
        try {
            AssetFileDescriptor desc;
            desc = asset.openFd(fileName);
            int soundId = soundPool.load(desc, 1);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Couldnt load sound '" + fileName + "'");
        }
    }
}
