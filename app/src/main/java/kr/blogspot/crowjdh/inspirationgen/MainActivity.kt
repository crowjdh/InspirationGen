package kr.blogspot.crowjdh.inspirationgen

//import kr.blogspot.crowjdh.midisupport.event.meta.TimeSignature
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kr.blogspot.crowjdh.inspirationgen.extensions.pauseIfPlaying
import kr.blogspot.crowjdh.inspirationgen.extensions.startIfNotPlaying
import kr.blogspot.crowjdh.inspirationgen.music.models.Bar
import kr.blogspot.crowjdh.inspirationgen.music.models.Sheet
import kr.blogspot.crowjdh.inspirationgen.music.models.toMidiFile
import java.io.File
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    var mPlayer: MediaPlayer by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cacheFile = createCacheFile()
        generateRandomSheet(cacheFile)
        prepareMediaPlayer(cacheFile)
    }

    override fun onResume() {
        super.onResume()

        playMediaPlayer()
    }

    override fun onPause() {
        super.onPause()

        pauseMediaPlayer()
    }

    private fun createCacheFile(): File = File(cacheDir, "temp.mid")

    private fun generateRandomSheet(cacheFile: File) {
        val sheet = Sheet()
        sheet.addBar(Bar.generate { })
        sheet.addBar(Bar.generate { })
        sheet.toMidiFile(120f).writeToFile(cacheFile)
    }

    private fun prepareMediaPlayer(file: File) {
        mPlayer = MediaPlayer()
        mPlayer.setDataSource(file.absolutePath)
        mPlayer.prepare()
    }

    private fun playMediaPlayer() {
        mPlayer.startIfNotPlaying()
    }

    private fun pauseMediaPlayer() {
        mPlayer.pauseIfPlaying()
    }
}
