package com.emresari.notes.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emresari.notes.model.Notes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll() : Flowable<List<Notes>>
    @Insert
    fun insert(notes:Notes) : Completable
    @Delete
    fun delete(notes: Notes) : Completable
}