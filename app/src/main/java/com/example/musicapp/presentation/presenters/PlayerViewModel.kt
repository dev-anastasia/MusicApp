package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackTable
import com.example.musicapp.presentation.ui.player.PlayerUIState

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val mainHandler = Handler(Looper.getMainLooper())

    val playerUiState = MutableLiveData<PlayerUIState<Int>>()

    val trackNameLiveData = MutableLiveData("")
    val artistNameLiveData = MutableLiveData("")
    val cover100LiveData = MutableLiveData("")  // Больший размер (для плеера)
    private val cover60LiveData = MutableLiveData("")   // Меньший размер (для БД)
    val durationLiveData = MutableLiveData("")
    val audioPreviewLiveData = MutableLiveData("")
    val isLikedLiveData = MutableLiveData(false)
    val isAddedToMediaLiveData = MutableLiveData(false)
    var trackId: Long = 0

    fun getTrackInfoFromServer(currentId: Long, context: Context) {
        trackId = currentId

        Thread {
            Creator.getTrackInfoUseCase.getTrackInfo(trackId, context) {
                mainHandler.post {
                    updateLiveData(it)
                }
            }
        }.start()
    }

    private fun covertTrackDurationMillisToString() {
        if (durationLiveData.value!! != "0:00") {
            val dur = durationLiveData.value!!.toLong()
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()

            if (durationInSeconds.length < 2)
                durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"

            durationLiveData.value = "$durationInMinutes:$durationInSeconds"
        }
    }

    override fun updateLiveData(hashmap: HashMap<String, String>) {
        // Обновить данные лайвдаты согласно хэшмапе после ответа из сети

        // Перенести эти состояния(hashmap) в UIState,
        // здесь будет только лайвдата с uiState
        // liveData.value = UIState

        if (hashmap.isEmpty().not()) {          // СОКРАТИТЬ - DATA CLASS?
            if (trackNameLiveData.value != hashmap[TRACK_NAME])
                trackNameLiveData.value = hashmap[TRACK_NAME]
            if (artistNameLiveData.value != hashmap[ARTIST_NAME])
                artistNameLiveData.value = hashmap[ARTIST_NAME]
            if (cover100LiveData.value != hashmap[COVER_IMAGE_100])
                cover100LiveData.value = hashmap[COVER_IMAGE_100]
            if (audioPreviewLiveData.value != hashmap[PREVIEW])
                audioPreviewLiveData.value = hashmap[PREVIEW]
            if (durationLiveData.value != hashmap[DURATION])
                durationLiveData.value = hashmap[DURATION]

            if (playerUiState.value != PlayerUIState.Success)
                playerUiState.value = PlayerUIState.Success
        } else {
            trackNameLiveData.value = ""
            artistNameLiveData.value = ""
            cover100LiveData.value = ""
            cover60LiveData.value = ""
            audioPreviewLiveData.value = ""

            playerUiState.value = PlayerUIState.Error
        }
        covertTrackDurationMillisToString()
    }

    //Проверка статуса "Нравится"/"Не нравится" (иконка с сердечком)
    fun checkIfFavourite(context: Context) {
        Thread {                                            // "-1" - номер плейлиста избранных треков
            Creator.getTracksListUseCase.findTrackInSinglePlaylist(trackId, -1, context) {
                mainHandler.post {
                    isLikedLiveData.value = it.isNotEmpty() // список непустой = true, в избранном
                }
            }
        }.start()
    }

    fun checkIfAddedToMedia(context: Context) {
        Thread {
            Creator.getTracksListUseCase.lookForTrackInMedia(trackId, context) {
                mainHandler.post {
                    isAddedToMediaLiveData.value =
                        it.isNotEmpty() // список непустой = true, в избранном
                }
            }
        }.start()
    }

    // Логика нажатия на кнопку "Нравится"
    fun likeClicked(context: Context) {
        if (isLikedLiveData.value == false) {
            // Добавляем в избранное:
            Thread {
                val crossRef =
                    PlaylistTrackCrossRef(-1, trackId) // "-1" - номер плейлиста избранных треков
                val track = TrackTable(
                    trackId,
                    artistNameLiveData.value.toString(),
                    trackNameLiveData.value.toString(),
                    cover60LiveData.value.toString(),
                    System.currentTimeMillis()
                )
                Creator.insertTrackUseCase.addTrackToPlaylist(track, crossRef, context)
                mainHandler.post {
                    isLikedLiveData.value = true
                }
            }.start()
        } else {
            // Удаляем из избранного:
            Thread {
                Creator.deleteTrackUseCase.deleteTrackFromPlaylist(trackId, -1, context)
                mainHandler.post {
                    isLikedLiveData.value = false
                }
            }.start()
        }
    }

    // Логика нажатия на иконку "Медиа"
    fun mediaIconClicked(context: Context, playlistId: Int) {
        Thread {    // Ищем: есть ли уже трек в выбранном плейлисте? (по аналогии с Яндекс.Музыкой)
            Creator.getTracksListUseCase.findTrackInSinglePlaylist(
                trackId, playlistId, context
            ) {
                if (it.isEmpty()) {   // Если трека нет в этом плейлисте - добавляем
                    addToMedia(context, playlistId)
                } else {       // Если трек уже есть в этом плейлисте - удаляем
                    deleteFromMedia(context, playlistId)
                }
            }
        }.start()
    }

    private fun addToMedia(context: Context, playlistId: Int) {
        Thread {
            val crossRef = PlaylistTrackCrossRef(playlistId, trackId)
            val track = TrackTable(
                trackId,
                artistNameLiveData.value.toString(),
                trackNameLiveData.value.toString(),
                cover100LiveData.value.toString(),
                System.currentTimeMillis()
            )
            Creator.insertTrackUseCase.addTrackToPlaylist(track, crossRef, context)
            mainHandler.post {
                isAddedToMediaLiveData.value = true
            }
        }.start()
    }

    private fun deleteFromMedia(context: Context, playlistId: Int) {
        Thread {
            Creator.deleteTrackUseCase.deleteTrackFromPlaylist(trackId, playlistId, context)
            // Обновляем иконку "Медиа" (проверяем, есть ли все еще трек в медиатеке)
            Creator.getTracksListUseCase.lookForTrackInMedia(trackId, context) {
                mainHandler.post {
                    isAddedToMediaLiveData.value =
                        it.isNotEmpty()  // Если список пуст - трек есть в Медиатеке (true)
                }
            }
        }.start()
    }

    companion object {
        const val ARTIST_NAME = "artist name key"
        const val TRACK_NAME = "track name key"
        const val DURATION = "duration key"
        const val PREVIEW = "preview key"
        const val COVER_IMAGE_100 = "cover image key"
    }
}