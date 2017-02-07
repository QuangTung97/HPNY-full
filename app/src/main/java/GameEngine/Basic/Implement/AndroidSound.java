package GameEngine.Basic.Implement;

import android.media.SoundPool;

import GameEngine.Basic.Interfaces.Sound;

/**
 * Created by Quang Tung on 11/23/2015.
 */
public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 1, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}
