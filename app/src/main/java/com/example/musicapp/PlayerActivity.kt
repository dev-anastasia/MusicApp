package com.example.musicapp

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.SearchActivity.Companion.ARTIST_NAME_KEY
import com.example.musicapp.SearchActivity.Companion.DURATION_KEY
import com.example.musicapp.SearchActivity.Companion.SONG_NAME_KEY
import com.example.musicapp.SearchActivity.Companion.TRACK_COVER_KEY
import com.example.musicapp.network.WorkerThread
import com.squareup.picasso.Picasso

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying: Boolean = false
    private var isLiked: Boolean = false
    private var isAddedToMedia: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Обложка
        val coverUri = Uri.parse(intent.getStringExtra(TRACK_COVER_KEY))
        Picasso.get()
            .load(coverUri)
            .resize(275, 275)
            .into(findViewById<ImageView>(R.id.player_activity_iv_cover))

        // Название, исполнитель
        val artistName = findViewById<TextView>(R.id.player_activity_tv_song_name)
        artistName.text = intent.getStringExtra(SONG_NAME_KEY).toString()
        val songName = findViewById<TextView>(R.id.player_activity_tv_author_name)
        songName.text = intent.getStringExtra(ARTIST_NAME_KEY).toString()

//        fun showAnim(tv: TextView) {
//            val mAnimation = TranslateAnimation(
//                0F,
//                (artistName.width - 1000).toFloat(),
//                0F,
//                0F
//            )
//            mAnimation.duration = 5000L
//            tv.startAnimation(mAnimation)
//            uiHandler.postDelayed({ showAnim(tv) }, 6000L)
//        }
//        if (artistName.text.length > artistName.width)
//            uiHandler.post { showAnim(artistName) }
//        if (songName.text.length > songName.width)
//            uiHandler.post { showAnim(songName) }

        // Seekbar + Длительность
        mediaPlayer = MediaPlayer.create(this, R.raw.song)
        val durationMillis = intent.getLongExtra(DURATION_KEY, 0L)

        fun setDuration() {
            val durationInSeconds = durationMillis / 1000 % 60
            val durationInMinutes = durationMillis / 1000 / 60
            findViewById<TextView>(R.id.player_activity_tv_duration).text =
                "$durationInMinutes:$durationInSeconds"
        }
        setDuration()

        //var currentTimeSeconds = mediaPlayer!!.currentPosition / 1000 % 60
        //var currentTimeMinutes = mediaPlayer!!.currentPosition / 1000 / 60

//        fun setCurrentPosition() {
//            if (mediaPlayer != null) {
//                val currentTimeSeconds = mediaPlayer!!.currentPosition / 1000 % 60
//                val currentTimeMinutes = mediaPlayer!!.currentPosition / 1000 / 60
//            }
//        }

        findViewById<SeekBar>(R.id.player_activity_seekbar).setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val currentProgress = mediaPlayer.currentPosition
                    seekBar?.progress = currentProgress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            }
        )

        // Избранное
        val favIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_fav)
        favIcon.setOnClickListener {
            if (isLiked.not()) {
                favIcon.setBackgroundResource(R.drawable.icon_fav_liked)
                isLiked = true
            } else {
                favIcon.setBackgroundResource(R.drawable.icon_fav_empty)
                isLiked = false
            }
        }

        // Медиатека
        val mediaIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_media)
        mediaIcon.setOnClickListener {  // Почему-то не работает...
            if (isAddedToMedia.not()) {
                mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
                isAddedToMedia = true
            } else {
                mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
                isAddedToMedia = false
            }
        }

        // Previous, Next
        findViewById<ImageView>(R.id.player_activity_iv_icon_prev).setOnClickListener {

        }

        findViewById<ImageView>(R.id.player_activity_iv_icon_next).setOnClickListener {

        }

        // Проигрыватель
        val playButton = findViewById<ImageView>(R.id.player_activity_iv_icon_play)
        val workerThread = WorkerThread()
        workerThread.start()

        fun stopAndRelease() {
            workerThread.handler.post {
                mediaPlayer.release()
                mediaPlayer = MediaPlayer.create(this, R.raw.song)
            }
        }

        fun play() {
            playButton.setBackgroundResource(R.drawable.icon_pause)
            workerThread.handler.post {
                mediaPlayer.start()
                isPlaying = true
                mediaPlayer.setOnCompletionListener {
                    stopAndRelease()
                }
            }
        }

        fun pause() {
            playButton.setBackgroundResource(R.drawable.icon_play)

            workerThread.handler.post {
                mediaPlayer.pause()
                isPlaying = false
            }
        }

        fun setCurrentProgress() {

        }

        playButton.setOnClickListener {
            if (isPlaying)
                pause()
            else
                play()
        }

        findViewById<ImageView>(R.id.player_activity_btn_go_back).setOnClickListener {
            super.getOnBackPressedDispatcher().onBackPressed()
            stopAndRelease()
            workerThread.handler.looper.quitSafely()
        }
    }
}