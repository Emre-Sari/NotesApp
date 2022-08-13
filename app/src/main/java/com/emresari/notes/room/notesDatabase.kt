package com.emresari.notes.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emresari.notes.model.Notes

@Database(entities = [Notes::class],version = 1)
abstract class notesDatabase : RoomDatabase(){
    abstract fun notesDao() : NotesDao
}