package com.example.musicapp.presentation.ui.player

import android.content.Context
import android.media.MediaPlayer
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
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MyObject.mediaPlayer
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.example.musicapp.presentation.presenters.factories.PlayerVMFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.fragment_player), PopupMenu.OnMenuItemClickListener {

    @Inject
    lateinit var playerClass: PlayerClass

    @Inject
    lateinit var vmFactory: PlayerVMFactory
    private lateinit var vm: PlayerViewModel
    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler
    private val playlistsList = mutableListOf<Playlist>()

    // ПЕРЕОПРЕДЕЛЁННЫЕ МЕТОДЫ + МЕТОДЫ ЖЦ:

    override fun onAttach(context: Context) {
        val playerSubcomponent =
            requireActivity().applicationContext.component.playerSubcomponent().create()
        playerSubcomponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[PlayerViewModel::class.java]
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime: TextView = view.findViewById(R.id.player_fragment_tv_current_time)
        val likeIcon: ImageButton = view.findViewById(R.id.player_fragment_iv_icon_fav)
        val seekbar: SeekBar = view.findViewById(R.id.player_fragment_seekbar)
        val songName: TextView = view.findViewById(R.id.player_fragment_tv_song_name)
        val artistName: TextView = view.findViewById(R.id.player_fragment_tv_artist_name)
        val durationString: TextView = view.findViewById(R.id.player_fragment_tv_duration)

        // Устанавливаем observers
        vm.apply {  // устанавливаем observers:

            if (playerUiState.value != PlayerUIState.IsPlaying
                &&
                playerUiState.value != PlayerUIState.Success
            ) {
                getTrackInfoFromServer(requireArguments().getLong(TRACK_ID)) // Получаем трек с сервера
            }

            // Устанавливаем observers:
            playerUiState.observe(viewLifecycleOwner) {
                when (it) {
                    PlayerUIState.Success -> {
                        updateUI()
                    }

                    PlayerUIState.IsPlaying -> {
                        updateUI()      // В этом состоянии трек не будет загружен повторно в плеер
                    }

                    PlayerUIState.Error -> {
                        Toast.makeText(
                            activity, "Ошибка: не удалось связаться с сервером", Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            viewState.observe(viewLifecycleOwner) {

                songName.text = it.trackName
                artistName.text = it.artistName
                durationString.text = it.durationString

                Picasso.get().load(it.artworkUrl100.ifEmpty { null })
                    .placeholder(R.drawable.note_placeholder)
                    .into(view.findViewById<ImageView>(R.id.player_fragment_iv_cover))
            }

            isLikedLiveData.observe(viewLifecycleOwner) {
                if (it == true) {
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_liked)
                } else {
                    likeIcon.setBackgroundResource(R.drawable.icon_fav_empty)
                }
            }
        }

        // Инициализируем lateinit vars

        uiHandler = Handler(Looper.getMainLooper())

        setCurrentTimeRunnable = Runnable {     // Runnable для установки текущего времени:
            if (vm.viewState.value?.durationString == DURATION_DEFAULT) {
                currentTime.text = vm.viewState.value?.durationString
            } else {
                currentTime.text = vm.countCurrentTime()
                uiHandler.postDelayed(setCurrentTimeRunnable, CURRENT_TIME_CHECK_TIMER)
            }
        }

        setCurrentSeekBarPosition = Runnable {     // Runnable для установки прогресса seekbar'a:
            if (vm.viewState.value!!.durationString != DURATION_DEFAULT) {
                seekbar.progress = mediaPlayer.currentPosition * 100 / mediaPlayer.duration
            } else {
                seekbar.progress = 0
            }
            uiHandler.postDelayed(setCurrentSeekBarPosition, CURRENT_SEEKBAR_CHECK_TIMER)
        }
    }

    override fun onResume() {

        requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_play).isClickable =
            false       // До загрузки трека кнопка play не кликабельна
        requireView().findViewById<TextView>(R.id.player_fragment_tv_top_info).setText(
            R.string.player_is_loading_text
        )
        // Получение списка доступных плейлистов (для работы с Медиатекой)
        getListOfUsersPlaylists()

        // Иконка "Вернуться назад"
        requireView().findViewById<ImageButton>(R.id.player_fragment_btn_go_back)
            .setOnClickListener {
                releasePlayer()
                requireActivity().supportFragmentManager.popBackStack()
            }
        super.onResume()
    }

    override fun onPause() {
        pausePlayer()
        super.onPause()
    }

    override fun onStop() {
        uiHandler.apply {
            removeCallbacks(setCurrentTimeRunnable)
            removeCallbacks(setCurrentSeekBarPosition)
        }
        super.onStop()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            val playlistId = playlistsList[item.itemId].playlistId
            vm.mediaIconClicked(playlistId)
        }
        return true
    }

    // ЧАСТНЫЕ МЕТОДЫ ФРАГМЕНТА:

    private fun updateUI() {
        setPlayer()
        setSeekbar()
        setIcons()
        val playlistId = this.arguments?.getInt(PLAYLIST_ID)
        if (playlistId != null) {
            setPrevAndNextBtns(playlistId)
        }
    }

    private fun setPlayer() {

        val playBtn = requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_play)

        if (vm.playerUiState.value != PlayerUIState.IsPlaying) {
            val link = vm.viewState.value!!.audioPreview
            playerClass.setPlayer(link) {

                vm.setPlayerUiState(IS_PLAYING_STATE)

                requireView().findViewById<TextView>(R.id.player_fragment_tv_top_info).setText(
                    R.string.player_is_ready_text
                )

                requireView().findViewById<TextView>(R.id.player_fragment_tv_current_time).text =
                    DURATION_DEFAULT

                playBtn.setBackgroundResource(R.drawable.icon_play_active)
                playBtn.isClickable = true

                requireView().findViewById<TextView>(R.id.player_fragment_tv_duration).text =
                    countDuration()

                covertTrackDurationMillisToString()
            }
        }

        playBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pausePlayer()
                it.setBackgroundResource(R.drawable.icon_play_active)
            } else {
                playPlayer()
                it.setBackgroundResource(R.drawable.icon_pause)
            }
        }
    }

    private fun playPlayer() {
        playerClass.playPlayer {
            requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_play)
                .setBackgroundResource(R.drawable.icon_play_active)
        }
        setCurrentTimeRunnable.run()
        setCurrentSeekBarPosition.run()
    }

    private fun pausePlayer() {
        playerClass.pausePlayer()
        requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_play)
            .setBackgroundResource(R.drawable.icon_play_active)
    }

    private fun stopPlayer() {
        playerClass.stopPlayer()
        uiHandler.apply {
            removeCallbacks(setCurrentTimeRunnable)
            removeCallbacks(setCurrentSeekBarPosition)
        }
    }

    private fun setIcons() {    // Установка иконок "Избранное" и "Медиа"
        vm.checkIfFavourite()

        requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_fav).apply {
            isClickable = true
            setOnClickListener {
                vm.likeClicked()
            }
        }

        requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_media).apply {
            isClickable = true
            setOnClickListener {
                if (playlistsList.isEmpty()) {
                    Toast.makeText(
                        context, "Медиатека пуста. Создайте первый плейлист!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showMenu(it)
                }
            }
        }

        // Для автопрокрутки текста:
        requireView().findViewById<TextView>(R.id.player_fragment_tv_song_name).isSelected = true
        requireView().findViewById<TextView>(R.id.player_fragment_tv_artist_name).isSelected = true
    }

    private fun setPrevAndNextBtns(id: Int) {
        vm.getTracksList(id)

        vm.tracksInThisPlaylistList.observe(viewLifecycleOwner) { list ->
            var currentTrackPosition = 0

            for (element in list) {
                if (element.trackId == this.requireArguments().getLong(TRACK_ID)) {
                    currentTrackPosition = list.indexOf(element)
                }
            }

            if (currentTrackPosition > 0) {    // Доступна кнопка "пред"
                requireView().findViewById<ImageButton>(R.id.player_fragment_iv_icon_prev).apply {

                    isClickable = true

                    setBackgroundResource(R.drawable.icon_prev_active)

                    setOnClickListener {
                        val prevTrackId =
                            vm.tracksInThisPlaylistList.value!![currentTrackPosition - 1].trackId
                        openNewTrackPlayerFragment(prevTrackId)
                    }
                }
            }

            if (currentTrackPosition < vm.tracksInThisPlaylistList.value!!.size - 1) {  // Доступна кнопка "след"
                requireView().findViewById<ImageView>(R.id.player_fragment_iv_icon_next).apply {

                    isClickable = true

                    setBackgroundResource(R.drawable.icon_next_active)

                    setOnClickListener {
                        val nextTrackId =
                            vm.tracksInThisPlaylistList.value!![currentTrackPosition + 1].trackId
                        openNewTrackPlayerFragment(nextTrackId)
                    }
                }
            }
        }
    }

    private fun setSeekbar() {
        requireView().findViewById<SeekBar>(R.id.player_fragment_seekbar).apply {
            isClickable = true      // Теперь можно перематывать время трека

            setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {  // Оповещает об изменениях в seekbar'е
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
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
            })
        }
    }

    private fun countDuration(): String {
        val millis = mediaPlayer.duration
        if (millis == -1) {
            return "0:00"
        }

        val mins = millis / 1000 / 60
        val currSecs = millis / 1000 % 60
        var secs = currSecs.toString()
        if (currSecs.toString().length < 2) {
            secs = "0$secs"
        }
        return "$mins:$secs"
    }

    private fun getListOfUsersPlaylists() {
        playlistsList.clear()
        vm.getListOfUsersPlaylists {
            playlistsList.addAll(it)
        }
    }

    private fun covertTrackDurationMillisToString() {

        val duration = vm.viewState.value!!.durationString
        if (duration == DURATION_DEFAULT) {

            val dur = mediaPlayer.duration.toLong()

            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()

            if (durationInSeconds.length < 2) {
                durationInSeconds = "0$durationInSeconds"
            }

            "$durationInMinutes:$durationInSeconds"
        }
    }

    private fun openNewTrackPlayerFragment(trackId: Long) {
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, trackId)
        bundle.putInt(PLAYLIST_ID, this.requireArguments().getInt(PLAYLIST_ID))
        playerFragment.arguments = bundle

        releasePlayer()

        requireActivity().supportFragmentManager.apply {
            beginTransaction().replace(R.id.main_container, playerFragment)
                .setReorderingAllowed(true).commit()
        }
    }

    private fun showMenu(view: View) {
        val pMenu = PopupMenu(view.context, view)
        for (i in 0 until playlistsList.size) {
            pMenu.menu.add(NONE, i, NONE, playlistsList[i].playlistName)
        }
        pMenu.apply {
            setOnMenuItemClickListener(this@PlayerFragment)
            show()
        }
    }

    private fun releasePlayer() {
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        mediaPlayer.apply {
            stopPlayer()
            release()
        }
        mediaPlayer = MediaPlayer()
    }

    private companion object {
        const val CURRENT_TIME_CHECK_TIMER = 1000L
        const val CURRENT_SEEKBAR_CHECK_TIMER = 600L
        const val TRACK_ID = "track id key"
        const val PLAYLIST_ID = "playlist id key"
        const val DURATION_DEFAULT = "0:00"
        const val IS_PLAYING_STATE = "IsPlaying"
        const val SUCCESS_STATE = "SUCCESS"
    }
}