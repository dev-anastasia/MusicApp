package com.example.musicapp.presentation.ui.player

import android.content.Context
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
import com.example.musicapp.R
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.presenters.PlayerViewModel
import com.squareup.picasso.Picasso

class PlayerFragment : Fragment(R.layout.fragment_player),
    PopupMenu.OnMenuItemClickListener {

    private lateinit var setCurrentTimeRunnable: Runnable
    private lateinit var setCurrentSeekBarPosition: Runnable
    private lateinit var uiHandler: Handler
    private var playlistId: Int? = null
    private val apContext: Context
        get() {
            return requireActivity().applicationContext
        }
    private val vm: PlayerViewModel by viewModels()
    private val playlistsList = mutableListOf<Playlist>()

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
    private val prevBtn: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_icon_prev)
        }
    private val nextBtn: ImageButton
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_icon_next)
        }
    private val coverImage: ImageView
        get() {
            return requireView().findViewById(R.id.player_fragment_iv_cover)
        }

    private val currentTrackId: Long
        get() {
            if (_currentTrackId == null)
                _currentTrackId = this.requireArguments().getLong(TRACK_ID)
            return _currentTrackId!!
        }
    private var _currentTrackId: Long? = null    // packing property


    // ПЕРЕОПРЕДЕЛЁННЫЕ МЕТОДЫ + МЕТОДЫ ЖЦ:

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем lateinit vars
        uiHandler = Handler(Looper.getMainLooper())
        playlistId = this@PlayerFragment.arguments?.getInt(PLAYLIST_ID)

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

        vm.apply {  // устанавливаем observers:
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
        }
    }

    override fun onResume() {

        vm.getTrackInfoFromServer(currentTrackId, apContext)    // Получаем трек с сервера

        // Получение списка доступных плейлистов (для работы с Медиатекой)
        getListOfUsersPlaylists()

        // Иконка "Вернуться назад"
        goBackBtn.setOnClickListener {
            releasePlayer()
            requireActivity().supportFragmentManager.popBackStack()
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

    override fun onDestroy() {
        uiHandler.apply {
            removeCallbacks(setCurrentSeekBarPosition)
            removeCallbacks(setCurrentTimeRunnable)
        }
        super.onDestroy()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            val playlistId = playlistsList[item.itemId].playlistId
            vm.mediaIconClicked(playlistId, apContext)
        }
        return true
    }

    // ЧАСТНЫЕ МЕТОДЫ ФРАГМЕНТА:

    private fun updateUI() {
        setPlayer()
        setSeekbar()
        setIcons()
        if (playlistId != null) {
            setPrevAndNextBtns()
        }
    }

    private fun setPlayer() {

        mediaPlayer.apply {
            if (this.isPlaying.not()) {
                setDataSource(vm.audioPreviewLiveData.value)
                prepareAsync()
            }
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
            checkIfFavourite(apContext)
            checkIfAddedToMedia(apContext)
        }

        likeIcon.apply {

            isClickable = true

            setOnClickListener {
                vm.likeClicked(apContext)
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

    private fun setPrevAndNextBtns() {
        vm.getTracksList(playlistId!!, apContext)

        vm.tracksInThisPlaylistList.observe(viewLifecycleOwner) { list ->
            var currentTrackPosition = 0

            for (element in list) {
                if (element.trackId == currentTrackId)
                    currentTrackPosition = list.indexOf(element)
            }

            if (currentTrackPosition > 0) {    // Доступна кнопка "пред"
                prevBtn.apply {

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
                nextBtn.apply {

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
        playlistsList.clear()
        vm.getListOfUsersPlaylists(apContext) {
            playlistsList.addAll(it)
        }
    }

    private fun openNewTrackPlayerFragment(trackId: Long) {
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, trackId)
        bundle.putInt(PLAYLIST_ID, playlistId!!)
        playerFragment.arguments = bundle

        releasePlayer()

        requireActivity().supportFragmentManager.apply {
            popBackStack()
            beginTransaction()
                .replace(R.id.media_container_main, playerFragment)
                .addToBackStack("added PlayerFragment")
                .setReorderingAllowed(true)
                .commit()
        }
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
        var mediaPlayer = MediaPlayer()
    }
}