package org.kashisol.musik.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import org.kashisol.musik.model.ITuneResults

@Database(entities = arrayOf(ITuneResults::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun iTuneResultDao(): ITuneResultDao

}