package org.kashisol.musik.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ITuneResults(
    @ColumnInfo var wrapperType: String = "",
    @ColumnInfo var kind: String = "",
    @ColumnInfo var artistId: Int = 0,
    @ColumnInfo var collectionId: Int = 0,
    @ColumnInfo var trackId: Int = 0,
    @ColumnInfo var artistName: String = "",
    @ColumnInfo var collectionName: String = "",
    @ColumnInfo var trackName: String = "",
    @ColumnInfo var collectionCensoredName: String = "",
    @ColumnInfo var trackCensoredName: String = "",
    @ColumnInfo var artistViewUrl: String = "",
    @ColumnInfo var collectionViewUrl: String = "",
    @ColumnInfo var trackViewUrl: String = "",
    @ColumnInfo var previewUrl: String = "",
    @ColumnInfo var artworkUrl30: String = "",
    @ColumnInfo var artworkUrl60: String = "",
    @ColumnInfo var artworkUrl100: String = "",
    @ColumnInfo var collectionPrice: Double = 0.0,
    @ColumnInfo var trackPrice: Double = 0.0,
    @ColumnInfo var releaseDate: String = "",
    @ColumnInfo var collectionExplicitness: String = "",
    @ColumnInfo var trackExplicitness: String = "",
    @ColumnInfo var discCount: Int = 0,
    @ColumnInfo var discNumber: Int = 0,
    @ColumnInfo var trackCount: Int = 0,
    @ColumnInfo var trackNumber: Int = 0,
    @ColumnInfo var trackTimeMillis: Int = 0,
    @ColumnInfo var country: String = "",
    @ColumnInfo var currency: String = "",
    @ColumnInfo var primaryGenreName: String = "",
    @ColumnInfo var contentAdvisoryRating: String = "",
    @ColumnInfo var isStreamable: Boolean = false,
    @ColumnInfo var shortDescription: String = "",
    @ColumnInfo var longDescription: String = "",
    @PrimaryKey var uniqueId: String = UUID.randomUUID().toString(),
    @ColumnInfo var hash: Int = 0,
) {

    fun setHash() {
        hash = super.hashCode()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return "ITuneResults(wrapperType='$wrapperType', kind='$kind', artistId=$artistId, collectionId=$collectionId, trackId=$trackId, artistName='$artistName', collectionName='$collectionName', trackName='$trackName', collectionCensoredName='$collectionCensoredName', trackCensoredName='$trackCensoredName', artistViewUrl='$artistViewUrl', collectionViewUrl='$collectionViewUrl', trackViewUrl='$trackViewUrl', previewUrl='$previewUrl', artworkUrl30='$artworkUrl30', artworkUrl60='$artworkUrl60', artworkUrl100='$artworkUrl100', collectionPrice=$collectionPrice, trackPrice=$trackPrice, releaseDate='$releaseDate', collectionExplicitness='$collectionExplicitness', trackExplicitness='$trackExplicitness', discCount=$discCount, discNumber=$discNumber, trackCount=$trackCount, trackNumber=$trackNumber, trackTimeMillis=$trackTimeMillis, country='$country', currency='$currency', primaryGenreName='$primaryGenreName', contentAdvisoryRating='$contentAdvisoryRating', isStreamable=$isStreamable)"
    }
}