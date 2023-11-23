package com.example.musicapp.presentation.ui.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu.NONE
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.squareup.picasso.Picasso

class PlayerFragment : Fragment(R.layout.fragment_player),
    PopupMenu.OnMenuItemClickListener {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler

    private val vm: PlayerViewModel by viewModels()
    private val playlistsList = mutableListOf<PlaylistTable>()

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
                _currentId = this.requireArguments().getLong(TRACK_ID)
            return _currentId!!
        }
    private var _currentId: Long? = null    // packing property


    // ПЕРЕОПРЕДЕЛЁННЫЕ МЕТОДЫ + МЕТОДЫ ЖЦ:

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем lateinit vars
        uiHandler = Handler(Looper.getMainLooper())

        setCurrentTimeRunnable = Runnable {     // Runnable для установки текущего времени:
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

        setCurrentSeekBarPosition = Runnable {     // Runnable для установки прогресса seekbar'a:
            if (vm.durationLiveData.value != "0:00")
                seekbar.progress = mediaPlayer.currentPosition * 100 / mediaPlayer.duration
            else
                seekbar.progress = 0
            uiHandler.postDelayed(setCurrentSeekBarPosition, CURRENT_SEEKBAR_CHECK_TIMER)
        }
    }

    override fun onResume() {

        vm.apply {

            getTrackInfoFromServer(currentId, requireActivity().applicationContext)

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

            isAddedToMediaLiveData.observe(viewLifecycleOwner) {
                if (it == true)
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_added)
                else
                    mediaIcon.setBackgroundResource(R.drawable.icon_media_empty)
            }

            playerUiState.observe(viewLifecycleOwner) {
                when (it) {

                    (PlayerUIState.Success) -> {
                        updateUI()
                    }

                    (PlayerUIState.Error) -> {
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

        // Получение списка доступных плейлистов (для дальнейшего добавления в медиатеку)
        getListOfUsersPlaylists()

        // Иконка "Вернуться назад"
        goBackBtn.setOnClickListener {
            onBackPressed()
        }

        super.onResume()
    }

    override fun onPause() {
        pausePlayer()

        uiHandler.apply {
            removeCallbacks(setCurrentTimeRunnable)
            removeCallbacks(setCurrentSeekBarPosition)
        }

        super.onPause()
    }

    override fun onDestroy() {  // Проблема при перевороте экрана!!!
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        super.onDestroy()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            val playlistId = playlistsList[item.itemId].playlistId
            vm.mediaIconClicked(requireActivity().applicationContext, playlistId)
        }
        return true
    }

    // ЧАСТНЫЕ МЕТОДЫ ФРАГМЕНТА:

    private fun updateUI() {
        setPlayer()
        setSeekbar()
        setIcons()
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
                    pausePlayer()
                else
                    playPlayer()
            }
        }
    }

    private fun playPlayer() {
        mediaPlayer.apply {
            start()
            setOnCompletionListener {   // При завершении трека:
                onStop()
                playBtn.setBackgroundResource(R.drawable.icon_play_active)
            }
        }
        playBtn.setBackgroundResource(R.drawable.icon_pause)

        uiHandler.apply {
            post(setCurrentTimeRunnable)
            post(setCurrentSeekBarPosition)
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playBtn.setBackgroundResource(R.drawable.icon_play_active)
    }

    private fun stopPlayer() {
        mediaPlayer.stop()

        uiHandler.apply {
            removeCallbacks(setCurrentTimeRunnable)
            removeCallbacks(setCurrentSeekBarPosition)
        }
    }

    private fun setIcons() {    // Установка иконок "Избранное" и "Медиа"
        vm.apply {
            checkIfFavourite(requireActivity().applicationContext)
            checkIfAddedToMedia(requireActivity().applicationContext)
        }

        likeIcon.apply {
            isClickable = true

            setOnClickListener {
                vm.likeClicked(requireActivity().applicationContext)
            }
        }

        mediaIcon.apply {
            isClickable = true

            setOnClickListener {
                if (playlistsList.isEmpty())
                    Toast.makeText(
                        context,
                        "Медиатека пуста. Создайте первый плейлист!",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    showMenu(it)
            }
        }

        // Для автопрокрутки текста:
        trackName.isSelected = true
        artistName.isSelected = true

        vm.cover100LiveData.observe(viewLifecycleOwner) { cover ->
            Picasso.get()
                .load(Uri.parse(cover))
                .placeholder(R.drawable.note_placeholder)
                .into(coverImage)
        }
    }


    private fun setSeekbar() {
        seekbar.apply {
            isClickable = true      // Теперь можно перематывать время трека

            setOnSeekBarChangeListener(
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
    }

    private fun getListOfUsersPlaylists() {
        Thread {
            playlistsList.clear()
            playlistsList.addAll(
                Creator.getPlaylistsUseCase
                    .getAllPlaylists(requireActivity().applicationContext)
            )
        }.start()
    }

    private fun showMenu(view: View) {
        uiHandler.post {
            val pMenu = PopupMenu(view.context, view)
            for (i in 0 until playlistsList.size) {
                pMenu.menu.add(NONE, i, NONE, playlistsList[i].playlistName)
            }
            pMenu.apply {
                inflate(R.menu.menu)
                setOnMenuItemClickListener(this@PlayerFragment)
                show()
            }
        }
    }

    private fun onBackPressed() {
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        mediaPlayer.apply {
            stopPlayer()
            release()
        }
        mediaPlayer = MediaPlayer()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private companion object {
        const val CURRENT_TIME_CHECK_TIMER = 1000L
        const val TRACK_ID = "track id key"
        const val CURRENT_SEEKBAR_CHECK_TIMER = 600L
        var mediaPlayer = MediaPlayer()
    }
}