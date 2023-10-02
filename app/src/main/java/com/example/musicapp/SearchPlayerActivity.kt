package com.example.musicapp

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.SearchActivity.Companion.TRACK_ID
import com.example.musicapp.data.PlayerViewModel
import com.example.musicapp.data.Tracks
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlayerActivity : AppCompatActivity() {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val songName = findViewById<TextView>(R.id.player_activity_tv_song_name)
        val artistName = findViewById<TextView>(R.id.player_activity_tv_artist_name)
        val duration = findViewById<TextView>(R.id.player_activity_tv_duration)
        val currentTime = findViewById<TextView>(R.id.player_activity_tv_current_time)
        uiHandler = Handler(Looper.getMainLooper())

        val playIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_play)
        val favIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_fav)
        val mediaIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_media)
        val seekbar = findViewById<SeekBar>(R.id.player_activity_seekbar)

        // Получение и сохранение данных из сети + заполнение вьюшек:

        val currentId: Long = intent.getLongExtra(TRACK_ID, 0L)

        if (savedInstanceState == null) {  // К сети обращаемся 1 раз - при первом создании активити:
            RetrofitUtils.musicService.getTrackInfoById(currentId)
                .enqueue(object : Callback<Tracks> {

                    override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {

                        val results = response.body()!!.results[0]

                        mediaPlayer.apply {
                            setDataSource(results.previewUrl)
                            prepareAsync()
                        }

                        songName.text = results.trackName
                        artistName.text = results.artistName

                        Picasso.get()
                            .load(Uri.parse(results.artworkUrl100))
                            .placeholder(R.drawable.note_placeholder)
                            .into(findViewById<ImageView>(R.id.player_activity_iv_cover))

                        // Длительность трека
                        val durationInMinutes = (results.trackTimeMillis / 1000 / 60).toString()
                        var durationInSeconds = (results.trackTimeMillis / 1000 % 60).toString()
                        if (durationInSeconds.length < 2)
                            durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"
                        duration.text = "$durationInMinutes:$durationInSeconds"

                        // Инициализация значений ViewModel:
                        playerViewModel.coverImageLinkLiveData.value = results.artworkUrl100
                        playerViewModel.songNameLiveData.value = results.trackName
                        playerViewModel.artistNameLiveData.value = results.artistName
                        playerViewModel.durationLiveData.value = duration.text.toString()
                    }

                    override fun onFailure(call: Call<Tracks>, t: Throwable) {

                        playIcon.apply {
                            setImageResource(R.drawable.icon_play_disabled)
                            isClickable = false
                        }

                        favIcon.apply {
                            setImageResource(R.drawable.icon_fav_empty)
                            isClickable = false
                        }

                        mediaIcon.apply {
                            setImageResource(R.drawable.icon_media_empty)
                            isClickable = false
                        }

                        Toast.makeText(
                            this@SearchPlayerActivity,
                            "Ошибка: не удалось связаться с сервером",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {    // При перевороте экрана используем ViewModel:
            songName.text = playerViewModel.songNameLiveData.value
            artistName.text = playerViewModel.artistNameLiveData.value
            duration.text = playerViewModel.durationLiveData.value
            Picasso.get()
                .load(Uri.parse(playerViewModel.coverImageLinkLiveData.value))
                .placeholder(R.drawable.note_placeholder)
                .into(findViewById<ImageView>(R.id.player_activity_iv_cover))
        }

        // Для автопрокрутки текста:
        songName.isSelected = true
        artistName.isSelected = true

        // В плеере после результатов поиска кнопки previous/next не активны:
        findViewById<ImageView>(R.id.player_activity_iv_icon_next).apply {
            setImageResource(R.drawable.icon_next_disabled)
            isClickable = false
        }
        findViewById<ImageView>(R.id.player_activity_iv_icon_prev).apply {
            setImageResource(R.drawable.icon_prev_disabled)
            isClickable = false
        }

        // Кнопка play
        playIcon.apply {
            if (mediaPlayer.isPlaying)
                setImageResource(R.drawable.icon_pause)
            else
                setImageResource(R.drawable.icon_play_active)
        }

        // Иконка "Избранное/Нравится"
        if (playerViewModel.isLikedLiveData.value == true)
            favIcon.setImageResource(R.drawable.icon_fav_liked)
        else
            favIcon.setImageResource(R.drawable.icon_fav_empty)

        favIcon.setOnClickListener {
            if (playerViewModel.isLikedLiveData.value == true) {
                playerViewModel.isLikedLiveData.value = false
                favIcon.setImageResource(R.drawable.icon_fav_empty)
            } else {
                playerViewModel.isLikedLiveData.value = true
                favIcon.setImageResource(R.drawable.icon_fav_liked)
            }
        }

        // Иконка "Медиатека"
        if (playerViewModel.isAddedLiveData.value == true)
            mediaIcon.setImageResource(R.drawable.icon_media_added)
        else
            mediaIcon.setImageResource(R.drawable.icon_media_empty)

        mediaIcon.setOnClickListener {
            if (playerViewModel.isAddedLiveData.value == true) {
                playerViewModel.isAddedLiveData.value = false
                mediaIcon.setImageResource(R.drawable.icon_media_empty)
            } else {
                playerViewModel.isAddedLiveData.value = true
                mediaIcon.setImageResource(R.drawable.icon_media_added)
            }
        }

        // Runnable для установки текущего времени (запуск ниже):
        setCurrentTimeRunnable = Runnable {
            val currentInMinutes: String =
                (mediaPlayer.currentPosition / 1000 / 60).toString()
            var currentInSeconds: String =
                (mediaPlayer.currentPosition / 1000 % 60).toString()
            if (currentInSeconds.length < 2)
                currentInSeconds = "0$currentInSeconds"     // вместо "1:7" -> "1:07"

            currentTime.text = "$currentInMinutes:$currentInSeconds"
            uiHandler.postDelayed(setCurrentTimeRunnable, CURRENT_TIME_CHECK_TIMER)
        }

        // Первую секунду при открытии активити значение текущего времени указано "97:12",
        // а прогресс seekbar'а на максимуме, не понимаю откуда это...

        // Runnable для установки прогресса seekbar'a (запуск ниже):
        setCurrentSeekBarPosition = Runnable {     // Установка позиции seekbar'а
            seekbar.progress = mediaPlayer.currentPosition * 100 / mediaPlayer.duration
            uiHandler.postDelayed(setCurrentSeekBarPosition, CURRENT_SEEKBAR_CHECK_TIMER)
        }

        // Чтобы не перегружать поток при смене конфигураций - сначала удаляем, а потом запускаем
        // runnables для установки текущего времени и прогресса seekbar'а:
        uiHandler.apply {
            removeCallbacks(setCurrentTimeRunnable)
            removeCallbacks(setCurrentSeekBarPosition)
            post(setCurrentTimeRunnable)
            post(setCurrentSeekBarPosition)
        }

        fun play() {
            mediaPlayer.start()
            playIcon.setImageResource(R.drawable.icon_pause)
            // По завершении трека:
            mediaPlayer.setOnCompletionListener {
                playIcon.setImageResource(R.drawable.icon_play_active)
            }
        }

        fun pause() {
            mediaPlayer.pause()
            playIcon.setImageResource(R.drawable.icon_play_active)
        }

        playIcon.setOnClickListener {
            if (mediaPlayer.isPlaying)
                pause()
            else
                play()
        }

        findViewById<SeekBar>(R.id.player_activity_seekbar).setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {  // Оповещает об изменениях в seekbar'е
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(mediaPlayer.duration * progress / 100)
                        uiHandler.apply {
                            removeCallbacks(setCurrentTimeRunnable)
                            removeCallbacks(setCurrentSeekBarPosition)
                            post(setCurrentTimeRunnable)
                            post(setCurrentSeekBarPosition)
                            // Переменные:
                            // seekBar?.progress - прогресс seekbar'a от 0 до 100
                            // progress - указанный пользователем прогресс от 0 до 100
                            // mediaPlayer.duration - фактическая длительность всего трека в плеере
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        // Иконка "назад" в приложении
        findViewById<ImageView>(R.id.player_activity_btn_go_back).setOnClickListener {
            uiHandler.apply {
                removeCallbacks(setCurrentSeekBarPosition)
                removeCallbacks(setCurrentTimeRunnable)
            }
            mediaPlayer.apply {
                stop()
                release()
            }
            mediaPlayer = MediaPlayer()
            finish()
        }
    }

    // Системная кнопка "назад"
    override fun onBackPressed() {
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        mediaPlayer.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer()
        finish()
    }

    private companion object {
        var mediaPlayer = MediaPlayer()
        const val CURRENT_TIME_CHECK_TIMER = 1000L
        const val CURRENT_SEEKBAR_CHECK_TIMER = 600L
    }
}