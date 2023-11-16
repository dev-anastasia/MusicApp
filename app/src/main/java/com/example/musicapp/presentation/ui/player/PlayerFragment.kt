package com.example.musicapp.presentation.ui.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicapp.R
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.ui.media.viewpager.FavsFragment
import com.squareup.picasso.Picasso

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable

    private lateinit var uiHandler: Handler
    private val vm: PlayerViewModel by viewModels()

    private val trackName: TextView
        get() {
            return requireView().findViewById(R.id.player_fragment_tv_song_name)
        }
    private val artistName: TextView
        get() {
            return requireView().findViewById(R.id.player_fragment_tv_artist_name)
        }
    private val duration: TextView
        get() {
            return requireView().findViewById(R.id.player_fragment_tv_duration)
        }
    private val currentTime: TextView
        get() {
            return requireView().findViewById(R.id.player_fragment_tv_current_time)
        }
    private val playBtn: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_icon_play)
        }
    private val likeIcon: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_icon_fav)
        }
    private val mediaIcon: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_icon_media)
        }
    private val seekbar: SeekBar
        get() {
            return requireView().findViewById(R.id.player_fragment_seekbar)
        }
    private val goBackBtn: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_btn_go_back)
        }
    private val coverImage: ImageView
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_cover)
        }

    private val currentId: Long
        get() {
            if (_currentId == null)
                _currentId = this.requireArguments().getLong(FavsFragment.TRACK_ID)
            return _currentId!!
        }
    private var _currentId: Long? = null    // packing property

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Подписываемся на ViewModel
//        vm.apply {
//            trackNameLiveData.observe(viewLifecycleOwner) {
//                trackName.text = it
//            }
//            artistNameLiveData.observe(viewLifecycleOwner) {
//                artistName.text = it
//            }
//            durationLiveData.observe(viewLifecycleOwner) {
//                duration.text = it
//            }
//            isLikedLiveData.observe(viewLifecycleOwner) {
//                if (it == true)
//                    likeIcon.setBackgroundResource(R.drawable.icon_fav_liked)
//                else
//                    likeIcon.setBackgroundResource(R.drawable.icon_fav_empty)
//            }
//            isAddedLiveData.observe(viewLifecycleOwner) {
//                if (it == true)
//                    mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
//                else
//                    mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
//            }
//            uiState.observe(viewLifecycleOwner) {
//                when (vm.uiState.value) {
//                    (UIState.Success) -> {
//                        updateUI()
//                    }
//
//                    (UIState.Loading) -> {}
//
//                    (UIState.Error) -> {
//                        Toast.makeText(
//                            activity,
//                            "Ошибка: не удалось связаться с сервером",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    else -> throw IllegalStateException("Illegal UI State")
//                }
//            }
//        }

        // Инициализируем lateinit vars
        uiHandler = Handler(Looper.getMainLooper())

        // Runnable для установки текущего времени:
        setCurrentTimeRunnable = Runnable {
            if (vm.durationLiveData.value == "0:00")
                currentTime.text = vm.durationLiveData.value
            else {
                val currentTimeInMinutes: String =
                    (mediaPlayer.currentPosition / 1000 / 60).toString()
                var currentTimeInSeconds: String =
                    (mediaPlayer.currentPosition / 1000 % 60).toString()
                if (currentTimeInSeconds.length < 2)
                    currentTimeInSeconds = "0$currentTimeInSeconds"     // вместо "1:7" -> "1:07"

                currentTime.text = "$currentTimeInMinutes:$currentTimeInSeconds"
                uiHandler.postDelayed(setCurrentTimeRunnable, CURRENT_TIME_CHECK_TIMER)
            }
        }

        // Runnable для установки прогресса seekbar'a:
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
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )
    }

    override fun onResume() {

        // Запрос в сеть
        vm.getTrackInfoFromServer(currentId, requireActivity().applicationContext)

        // Подписываемся на ViewModel
        vm.apply {
            trackNameLiveData.observe(viewLifecycleOwner) {
                trackName.text = it
            }
            artistNameLiveData.observe(viewLifecycleOwner) {
                artistName.text = it
            }
            durationLiveData.observe(viewLifecycleOwner) {
                duration.text = it
            }
            isLikedLiveData.observe(viewLifecycleOwner) {
                if (it == true)
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_liked)
                else
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_empty)
            }
            isAddedLiveData.observe(viewLifecycleOwner) {
                if (it == true)
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
                else
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
            }
            uiState.observe(viewLifecycleOwner) {
                when (vm.uiState.value) {
                    (UIState.Success) -> {
                        updateUI()
                    }

                    (UIState.Loading) -> {}

                    (UIState.Error) -> {
                        Toast.makeText(
                            activity,
                            "Ошибка: не удалось связаться с сервером",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> throw IllegalStateException("Illegal UI State")
                }
            }
        }

        // Иконка "Вернуться назад"
        goBackBtn.setOnClickListener {
            onBackPressed()
        }

        super.onResume()
    }

    private fun play() {
        mediaPlayer.start()
        playBtn.setBackgroundResource(R.drawable.icon_pause)
        // По завершении трека:
        mediaPlayer.setOnCompletionListener {
            playBtn.setBackgroundResource(R.drawable.icon_play_active)
        }
    }

    private fun updateUI() {
        setPlayer()
        setUI()
    }

    private fun setPlayer() {
        mediaPlayer.apply {
            setDataSource(vm.audioPreviewLiveData.value)
            prepareAsync()
        }

        // Кнопка play
        playBtn.apply {
            isClickable = true

            if (mediaPlayer.isPlaying)
                setBackgroundResource(R.drawable.icon_pause)
            else
                setBackgroundResource(R.drawable.icon_play_active)

            setOnClickListener {
                if (mediaPlayer.isPlaying)
                    pause()
                else
                    play()
            }
        }
    }

    private fun pause() {
        mediaPlayer.pause()
        playBtn.setBackgroundResource(R.drawable.icon_play_active)
    }

    private fun setUI() {
        likeIcon.apply {
            isClickable = true
            setOnClickListener {
                if (vm.isLikedLiveData.value == true) {
                    vm.updateIsLikedLD(requireActivity().applicationContext, false)
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_empty)
                } else {
                    vm.updateIsLikedLD(requireActivity().applicationContext, true)
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_liked)
                }
            }
        }

        mediaIcon.apply {
            isClickable = true
            setOnClickListener {
                if (isAdded) {
                    vm.updateIsAddedLD(false)
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
                } else {
                    vm.updateIsAddedLD(true)
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
                }
            }
        }

        // Для автопрокрутки текста:
        trackName.isSelected = true
        artistName.isSelected = true

        vm.coverImageLinkLiveData.observe(viewLifecycleOwner) { cover ->
            Picasso.get()
                .load(Uri.parse(cover))
                .placeholder(R.drawable.note_placeholder)
                .into(coverImage)
        }
    }

    override fun onDestroy() {  // Проблема при перевороте экрана!!!
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
        parentFragmentManager.popBackStack()
    }

    private companion object {
        const val CURRENT_TIME_CHECK_TIMER = 1000L
        const val CURRENT_SEEKBAR_CHECK_TIMER = 600L
        var mediaPlayer = MediaPlayer()
    }
}