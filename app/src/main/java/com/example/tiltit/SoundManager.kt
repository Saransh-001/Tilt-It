package com.example.tiltit

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool

class SoundManager(
    private val context: Context
) {

    private var backgroundPlayer: MediaPlayer? = null
    private var gameOverPrayer: MediaPlayer? = null

    private var soundPool: SoundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val collisionSoundId: Int

    init {
        collisionSoundId = soundPool.load(context, R.raw.collision_sound, 1)
    }

    fun playBackgroundMusic() {
        if (backgroundPlayer == null) {
            backgroundPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
                isLooping = true
            }
        }
        backgroundPlayer?.start()
    }

    fun pauseBackgroundMusic() {
        backgroundPlayer?.pause()
    }

    fun stopBackgroundMusic() {
        backgroundPlayer?.stop()
        backgroundPlayer?.release()
        backgroundPlayer = null
    }

    fun playGameOverMusic() {
        if (gameOverPrayer == null) {
            gameOverPrayer = MediaPlayer.create(context, R.raw.gameover_music).apply {
                isLooping = true
            }
        }
        gameOverPrayer?.start()
    }

    fun stopGameOverMusic() {
        gameOverPrayer?.stop()
        gameOverPrayer?.release()
        gameOverPrayer = null
    }

    fun playCollisionSound() {
        soundPool.play(collisionSoundId, 1f, 1f, 0, 0, 1f)
    }

    fun releaseAll() {
        stopBackgroundMusic()
        stopGameOverMusic()
        soundPool.release()
    }

}
