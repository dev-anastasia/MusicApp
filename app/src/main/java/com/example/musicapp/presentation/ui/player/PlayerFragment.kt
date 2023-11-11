package com.example.musicapp.presentation.ui.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.ui.media.viewpager.FavsFragment
import com.squareup.picasso.Picasso

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler
    private val vm: PlayerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackName = view.findViewById<TextView>(R.id.player_fragment_tv_song_name)
        val artistName = view.findViewById<TextView>(R.id.player_fragment_tv_artist_name)
        val duration = view.findViewById<TextView>(R.id.player_fragment_tv_duration)
        val currentTime = view.findViewById<TextView>(R.id.player_fragment_tv_current_time)
        val playIcon = view.findViewById<ImageView>(R.id.player_fragment_iv_icon_play)
        val favIcon = view.findViewById<ImageView>(R.id.player_fragment_iv_icon_fav)
        val mediaIcon = view.findViewById<ImageView>(R.id.player_fragment_iv_icon_media)
        val seekbar = view.findViewById<SeekBar>(R.id.player_fragment_seekbar)
        val goBackBtn = view.findViewById<ImageView>(R.id.player_fragment_btn_go_back)
        val coverImage = view.findViewById<ImageView>(R.id.player_fragment_iv_cover)

        var isLiked: Boolean? = null
        var isAdded: Boolean? = null

        uiHandler = Handler(Looper.getMainLooper())
        Creator.setPlayerUseCaseVM(vm)

        // Для автопрокрутки текста:
        trackName.isSelected = true
        artistName.isSelected = true

        // Получение и сохранение данных из сети + заполнение вьюшек:
        val currentId: Long = this.requireArguments().getLong(FavsFragment.TRACK_ID)

        // К сети обращаемся 1 раз - при первом создании фрагмента:
        if (savedInstanceState == null) {
            vm.apply {
                getTrackInfoFromServer(currentId, requireContext())
            }

            mediaPlayer.apply {
                setDataSource(vm.previewLiveData.value)
                prepareAsync()
            }
        }

        // Если успешно загрузили данные из сети:
        if (vm.serverReplied.value == true) {
            isLiked = vm.isLikedLiveData.value
            isAdded = vm.isAddedLiveData.value

            favIcon.isClickable = true
            mediaIcon.isClickable = true
            playIcon.isClickable = true

            // Остальные вьюшки:
            vm.coverImageLinkLiveData.observe(viewLifecycleOwner) { cover ->
                Picasso.get()
                    .load(Uri.parse(cover))
                    .placeholder(R.drawable.note_placeholder)
                    .into(coverImage)
            }
        } else {
            Toast.makeText(
                activity,
                "Ошибка: не удалось связаться с сервером",
                Toast.LENGTH_SHORT
            ).show()
        }

        vm.apply {
            trackNameLiveData.observe(viewLifecycleOwner) { name ->
                trackName.text = name
            }
            artistNameLiveData.observe(viewLifecycleOwner) { name ->
                artistName.text = name
            }
            durationLiveData.observe(viewLifecycleOwner) { dur ->
                duration.text = dur
            }
        }

        // Кнопка play
        playIcon.apply {
            if (mediaPlayer.isPlaying)
                setBackgroundResource(R.drawable.icon_pause)
            else
                setBackgroundResource(R.drawable.icon_play_active)
        }

        // Иконка "Избранное/Нравится"
        if (isLiked == true)
            favIcon.setBackgroundResource(R.drawable.icon_fav_liked)
        else
            favIcon.setBackgroundResource(R.drawable.icon_fav_empty)

        favIcon.setOnClickListener {
            if (isLiked == true) {
                vm.updateIsLikedLD(requireContext(), false)
                favIcon.setBackgroundResource(R.drawable.icon_fav_empty)
            } else {
                vm.updateIsLikedLD(requireContext(), true)
                favIcon.setBackgroundResource(R.drawable.icon_fav_liked)
            }
        }

        // Иконка "Медиатека"
        if (isAdded == true)
            mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
        else
            mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)

        mediaIcon.setOnClickListener {
            if (isAdded == true) {
                vm.updateIsAddedLD(false)
                mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
            } else {
                vm.updateIsAddedLD(true)
                mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
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
            playIcon.setBackgroundResource(R.drawable.icon_pause)
            // По завершении трека:
            mediaPlayer.setOnCompletionListener {
                playIcon.setBackgroundResource(R.drawable.icon_play_active)
            }
        }

        fun pause() {
            mediaPlayer.pause()
            playIcon.setBackgroundResource(R.drawable.icon_play_active)
        }

        playIcon.setOnClickListener {
            if (mediaPlayer.isPlaying)
                pause()
            else
                play()
        }

        seekbar.setOnSeekBarChangeListener(
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

        // Кнопки "назад" - в приложении и системная
        goBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        onBackPressed()
        super.onDestroy()
    }

    private fun onBackPressed() {   // Вызывается из SearchActivity!!
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        mediaPlayer.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private companion object {
        const val CURRENT_TIME_CHECK_TIMER = 1000L
        const val CURRENT_SEEKBAR_CHECK_TIMER = 600L
        var mediaPlayer = MediaPlayer()
    }
}