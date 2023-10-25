package com.example.musicapp.presentation.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.ui.SearchActivity.Companion.TRACK_ID
import com.squareup.picasso.Picasso

class PlayerActivity : AppCompatActivity() {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler
    private val vm: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val trackName = findViewById<TextView>(R.id.player_activity_tv_song_name)
        val artistName = findViewById<TextView>(R.id.player_activity_tv_artist_name)
        val duration = findViewById<TextView>(R.id.player_activity_tv_duration)
        val currentTime = findViewById<TextView>(R.id.player_activity_tv_current_time)
        val playIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_play)
        val favIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_fav)
        val mediaIcon = findViewById<ImageView>(R.id.player_activity_iv_icon_media)
        val seekbar = findViewById<SeekBar>(R.id.player_activity_seekbar)
        val coverImage = findViewById<ImageView>(R.id.player_activity_iv_cover)


        uiHandler = Handler(Looper.getMainLooper())
        Creator.updatePlayerUseCase(vm)


        // Для автопрокрутки текста:
        trackName.isSelected = true
        artistName.isSelected = true

        // В плеере после результатов поиска кнопки previous/next не активны:
        findViewById<ImageView>(R.id.player_activity_iv_icon_next)
            .setImageResource(R.drawable.icon_next_disabled)
        findViewById<ImageView>(R.id.player_activity_iv_icon_prev)
            .setImageResource(R.drawable.icon_prev_disabled)

        // Получение и сохранение данных из сети + заполнение вьюшек:
        val currentId: Long = intent.getLongExtra(TRACK_ID, 0L)

        // К сети обращаемся 1 раз - при первом создании активити:
        if (savedInstanceState == null) {
            vm.apply {
                initUserPrefsLD() // Инициализация статусов isLiked и isAddedToMedia
                onGetTrackInfoClicked(currentId)
            }
        }

        var isLiked = vm.isLikedLiveData.value
        var isAdded = vm.isAddedLiveData.value

        // Остальные вьюшки:
        if (vm.coverImageLinkLiveData.value != null) {
            vm.coverImageLinkLiveData.observe(this) { cover ->
                Picasso.get()
                    .load(Uri.parse(cover))
                    .placeholder(R.drawable.note_placeholder)
                    .into(coverImage)
            }
        }

        vm.apply {
            trackNameLiveData.observe(this@PlayerActivity) { name ->
                trackName.text = name
            }
            artistNameLiveData.observe(this@PlayerActivity) { name ->
                artistName.text = name
            }
            durationLiveData.observe(this@PlayerActivity) { dur ->
                duration.text = dur
            }
        }

        if (savedInstanceState == null) {
            mediaPlayer.apply {
                if (vm.previewLiveData.value != null) {
                    setDataSource(vm.previewLiveData.value)
                    prepareAsync()
                }
            }
        }

        // Кнопка play
        playIcon.apply {
            if (mediaPlayer.isPlaying)
                setImageResource(R.drawable.icon_pause)
            else
                setImageResource(R.drawable.icon_play_active)
        }

        // Иконка "Избранное/Нравится"
        if (isLiked == true)
            favIcon.setImageResource(R.drawable.icon_fav_liked)
        else
            favIcon.setImageResource(R.drawable.icon_fav_empty)

        favIcon.setOnClickListener {
            if (isLiked == true) {
                vm.updateIsLikedLD(false)
                favIcon.setImageResource(R.drawable.icon_fav_empty)
            } else {
                vm.updateIsLikedLD(true)
                favIcon.setImageResource(R.drawable.icon_fav_liked)
            }
        }

        // Иконка "Медиатека"
        if (isAdded == true)
            mediaIcon.setImageResource(R.drawable.icon_media_added)
        else
            mediaIcon.setImageResource(R.drawable.icon_media_empty)

        mediaIcon.setOnClickListener {
            if (isAdded == true) {
                vm.updateIsAddedLD(false)
                mediaIcon.setImageResource(R.drawable.icon_media_empty)
            } else {
                vm.updateIsAddedLD(true)
                mediaIcon.setImageResource(R.drawable.icon_media_added)
            }
        }


        // Runnable для установки текущего времени:
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
        super.onBackPressed()
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