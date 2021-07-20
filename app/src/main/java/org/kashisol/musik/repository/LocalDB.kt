package org.kashisol.musik.repository

import android.content.Context
import androidx.room.Room
import org.kashisol.musik.model.ITuneResults

class LocalDB {

    companion object {

        private lateinit var db: Database
        private lateinit var dao: ITuneResultDao
        private lateinit var onLocalDBResult: OnLocalDBResult

        fun initializeDB(context: Context, onLocalDBResult: OnLocalDBResult) {
            Companion.onLocalDBResult = onLocalDBResult
            db = Room.databaseBuilder(
                context,
                Database::class.java,
                "tests_database"
            )
                .allowMainThreadQueries()
                .build()
            dao = db.iTuneResultDao()
        }

        fun insertAll(list: List<ITuneResults>, id: Int) {
            Thread {
                for (elem in list) {
                    insertSingle(elem, id)
                }
                onLocalDBResult.onInserted(id)
            }.start()
        }

        fun insertSingle(iTuneResults: ITuneResults, id: Int) {
            Thread {
                dao.insertAll(iTuneResults)
                onLocalDBResult.onInserted(id)
            }.start()
        }

        fun search(term: String, id: Int) {
            Thread {
                var results = dao.getOfflineResult(term)
                onLocalDBResult.onSearch(results, id)
            }.start()
        }

    }

    interface OnLocalDBResult {
        fun onSearch(result: List<ITuneResults>, id: Int)
        fun onInserted(id: Int)
    }

}