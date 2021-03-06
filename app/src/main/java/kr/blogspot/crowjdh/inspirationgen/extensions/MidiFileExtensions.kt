package kr.blogspot.crowjdh.inspirationgen.extensions

import android.content.res.Resources
import android.support.annotation.RawRes
import kr.blogspot.crowjdh.midisupport.MidiFile

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 3.
 *
 * MidiFileExtensions
 */

fun MidiFile.removeTracks(range: IntRange) {
    tracks.removeRange(range)
    val trackCountField = javaClass.getDeclaredField("mTrackCount")
    trackCountField.isAccessible = true
    trackCountField.set(this, tracks.size)
    type = if (tracks.size > 1) 1 else 0
}

fun MidiFile(resources: Resources, @RawRes resId: Int): MidiFile =
        resources.openRawResource(resId).use { MidiFile(this) }
