package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.ui.player.PlayerUIState

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val uiHandler = Handler(Looper.getMainLooper())
    val playerUiState = MutableLiveData<PlayerUIState<Int>>()

    val trackNameLiveData = MutableLiveData("")
    val artistNameLiveData = MutableLiveData("")
    val durationLiveData = MutableLiveData("")
    val audioPreviewLiveData = MutableLiveData("")
    val isLikedLiveData = MutableLiveData(false)
    val isAddedToMediaLiveData = MutableLiveData(false)
    val tracksInThisPlaylistList = MutableLiveData<List<MusicTrack>>(emptyList())
    val cover100LiveData = MutableLiveData<String>(null)  // Больший размер (для плеера)
    private val cover60LiveData = MutableLiveData<String>(null)   // Меньший размер (для БД)
    private var trackId: Long = 0


    fun getTrackInfoFromServer(currentId: Long, context: Context) {

        trackId = currentId

        Thread {
            Creator.getTrackInfoUseCase.getTrackInfo(trackId, context) {
                uiHandler.post {
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
        if (hashmap.isEmpty().not()) {
            if (trackNameLiveData.value != hashmap[TRACK_NAME])
                trackNameLiveData.value = hashmap[TRACK_NAME]
            if (artistNameLiveData.value != hashmap[ARTIST_NAME])
                artistNameLiveData.value = hashmap[ARTIST_NAME]
            if (cover100LiveData.value != hashmap[COVER_IMAGE_100])
                cover100LiveData.value = hashmap[COVER_IMAGE_100]
            if (cover60LiveData.value != hashmap[COVER_IMAGE_60])
                cover60LiveData.value = hashmap[COVER_IMAGE_60]
            if (audioPreviewLiveData.value != hashmap[PREVIEW])
                audioPreviewLiveData.value = hashmap[PREVIEW]
            if (durationLiveData.value != hashmap[DURATION])
                durationLiveData.value = hashmap[DURATION]

            if (playerUiState.value != PlayerUIState.Success)
                playerUiState.value = PlayerUIState.Success
        } else {
            playerUiState.value = PlayerUIState.Error
        }
        covertTrackDurationMillisToString()
    }

    //Проверка статуса "Нравится"/"Не нравится" (иконка с сердечком)
    fun checkIfFavourite(context: Context) {
        Thread {
            Creator.getTracksListUseCase.lookForTrackInPlaylist(trackId, -1, context) {
                uiHandler.post {
                    isLikedLiveData.value = it.isNotEmpty() // список непустой = true, в избранном
                }
            }
        }.start()
    }

    fun checkIfAddedToMedia(context: Context) {
        Thread {
            Creator.getTracksListUseCase.lookForTrackInMedia(trackId, context) {
                uiHandler.post {
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
                val track = MusicTrack(
                    trackId,
                    artistNameLiveData.value.toString(),
                    trackNameLiveData.value.toString(),
                    audioPreviewLiveData.value.toString(),
                    cover60LiveData.value.toString(),
                    cover100LiveData.value.toString()
                )
                Creator.insertTrackUseCase.addTrackToPlaylist(
                    track, Creator.favsPlaylistId, context
                )
                uiHandler.post {
                    isLikedLiveData.value = true
                }
            }.start()
        } else {
            // Удаляем из избранного:
            Thread {
                Creator.deleteTrackUseCase.deleteTrackFromPlaylist(
                    trackId, Creator.favsPlaylistId, context
                )
                uiHandler.post {
                    isLikedLiveData.value = false
                }
            }.start()
        }
    }

    // Логика нажатия на иконку "Медиа"
    fun mediaIconClicked( playlistId: Int, context: Context) {
        Thread {    // Ищем: есть ли уже трек в выбранном плейлисте? (по аналогии с Яндекс.Музыкой)
            Creator.getTracksListUseCase.lookForTrackInPlaylist(
                trackId, playlistId, context
            ) {
                if (it.isEmpty()) {                 // Если трека нет в этом плейлисте - добавляем
                    addToMedia(playlistId, context)
                } else {                        // Если трек уже есть в этом плейлисте - удаляем
                    deleteFromMedia(playlistId, context)
                }
            }
        }.start()
    }

    fun getListOfUsersPlaylists(context: Context, callback: (List<Playlist>) -> Unit) {
        Thread {
            Creator.getPlaylistsUseCase.getAllPlaylists(context, callback)
        }.start()
    }

    fun getTracksList(playlistId: Int, context: Context) {
        Thread {
            Creator.getTracksListUseCase.getPlaylistTracksList(context, playlistId) {
                uiHandler.post {
                    updateList(it)
                }
            }
        }.start()
    }

    private fun addToMedia(playlistId: Int, context: Context) {
        Thread {
            val track = MusicTrack(
                trackId,
                artistNameLiveData.value.toString(),
                trackNameLiveData.value.toString(),
                audioPreviewLiveData.value.toString(),
                cover60LiveData.value.toString(),
                cover100LiveData.value.toString()
            )
            Creator.insertTrackUseCase.addTrackToPlaylist(track, playlistId, context)
            uiHandler.post {
                isAddedToMediaLiveData.value = true
            }
        }.start()
    }

    private fun deleteFromMedia(playlistId: Int, context: Context) {
        Thread {
            Creator.deleteTrackUseCase.deleteTrackFromPlaylist(trackId, playlistId, context)
            // Обновляем иконку "Медиа" (проверяем, есть ли все еще трек в медиатеке)
            Creator.getTracksListUseCase.lookForTrackInMedia(trackId, context) {
                uiHandler.post {
                    isAddedToMediaLiveData.value =
                        it.isNotEmpty()  // Если список пуст - трек есть в Медиатеке (true)
                }
            }
        }.start()
    }

    private fun updateList(list: List<MusicTrack>) {
        this.tracksInThisPlaylistList.value = list
    }

    companion object {
        const val ARTIST_NAME = "artist name key"
        const val TRACK_NAME = "track name key"
        const val DURATION = "duration key"
        const val PREVIEW = "preview key"
        const val COVER_IMAGE_100 = "cover image big key"
        const val COVER_IMAGE_60 = "cover image small key"
    }
}