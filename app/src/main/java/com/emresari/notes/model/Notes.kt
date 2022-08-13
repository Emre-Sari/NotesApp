package com.emresari.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Notes(
    @ColumnInfo
    var tittle:String,
    @ColumnInfo
    var note:String) : Serializable{

    @PrimaryKey(autoGenerate = true)
    var id=0


}
