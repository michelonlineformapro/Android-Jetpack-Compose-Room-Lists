package com.micpiwo.todolistroomcompose.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.micpiwo.todolistroomcompose.entity.ListeTaches

@Dao
interface ListeDAO {
    //Afficher toutes les taches
    @Query("SELECT * FROM liste_taches ORDER BY id DESC")
    fun getTaches(): LiveData<MutableList<ListeTaches>>

    //Ajouter taches
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaches(listeTaches: ListeTaches)

    //Mise a jour
    @Update
    suspend fun miseJourTaches(listeTaches: ListeTaches)

    //Supprimer taches
    @Delete
    suspend fun supprimertaches(listeTaches: ListeTaches)

    //Supprimer tous
    @Query("DELETE FROM liste_taches")
    suspend fun supprimerTous()
}