package com.micpiwo.todolistroomcompose.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.micpiwo.todolistroomcompose.dao.ListeDAO
import com.micpiwo.todolistroomcompose.entity.ListeTaches

@Database(entities = [ListeTaches::class], version = 1, exportSchema = false)
abstract class RoomSingletonDB: RoomDatabase() {
    //Appel de la DAO
    abstract fun listeDAO(): ListeDAO

    //companion object = utilisé pour evité l'implémentation d'une interface comme en java
    companion object{
        private var INSTANCE: RoomSingletonDB? = null
        fun getInstance(context: Context): RoomSingletonDB{
            //Si l'instance de la db est null = on la creer
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context, RoomSingletonDB::class.java, "roomDB"
                )
                    .build()
            }
            return INSTANCE as RoomSingletonDB
        }
    }

}