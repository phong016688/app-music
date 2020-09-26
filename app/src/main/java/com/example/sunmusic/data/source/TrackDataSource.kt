package com.example.sunmusic.data.source

import com.example.sunmusic.data.source.remote.response.ImageResponse
import com.example.sunmusic.data.source.remote.response.TrackResponse

interface TrackDataSource {
    interface Remote {
        fun getTopTracks(limit: Int, offset: Int): List<TrackResponse>
        fun getImageTrack(artistsId: String): ImageResponse
        fun getTracksInAlbum(albumId: String): List<TrackResponse>
    }
}
