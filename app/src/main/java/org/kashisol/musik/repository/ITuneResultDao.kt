package org.kashisol.musik.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.kashisol.musik.model.ITuneResults

@Dao
interface ITuneResultDao {

    @Query("SELECT * FROM ituneresults")
    fun getAll(): List<ITuneResults>

    @Insert
    fun insertAll(vararg iTuneResults: ITuneResults)

    @Query("SELECT * FROM ituneresults WHERE artistName = :search")
    fun findByName(search: String): List<ITuneResults>

    @Query("SELECT * FROM ituneresults WHERE LOWER(artistName) LIKE '%' || :search || '%' OR collectionName LIKE '%' || :search|| '%' OR trackName LIKE '%' || :search || '%' GROUP BY hash")
    fun getOfflineResult(search: String): List<ITuneResults>

}