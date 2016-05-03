package kr.blogspot.crowjdh.inspirationgen.extensions

import android.media.MediaPlayer

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * MediaPlayerExtensions
 */

fun MediaPlayer.startIfNotPlaying() {
    if (!isPlaying) start()
}

fun MediaPlayer.pauseIfPlaying() {
    if (isPlaying) pause()
}
