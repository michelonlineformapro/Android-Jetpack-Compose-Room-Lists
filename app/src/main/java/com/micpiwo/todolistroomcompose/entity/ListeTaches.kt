package com.micpiwo.todolistroomcompose.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liste_taches")
class ListeTaches (
    @PrimaryKey
    var id: Long?,
    @ColumnInfo(name = "uuid")
    var nomUtilisateurs: String,
    @ColumnInfo(name = "notes")
    var notes: String
    )