package com.micpiwo.todolistroomcompose.views

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micpiwo.todolistroomcompose.database.RoomSingletonDB
import com.micpiwo.todolistroomcompose.entity.ListeTaches
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListeViewModel(application: Application):ViewModel() {

    //Appel de la connexion a la DB room via la methode getInstance
    private val db = RoomSingletonDB.getInstance(application)

    //Appel de la liste de l'entité via : la connexion + la dao + la methode gettaches
    internal val listeTaches: LiveData<MutableList<ListeTaches>> = db.listeDAO().getTaches()

    //pour ajouter une taches
    fun ajouterUneTache(listeTaches: ListeTaches){
        //Appel de la connexion db + la methode de la DAO
        db.listeDAO().addTaches(listeTaches)
    }

    //Mettre a jour une tache
    fun updateTache(listeTaches: ListeTaches){
        //regroupe chaque viewModel taches et lance une coroutines
        viewModelScope.launch(Dispatchers.IO){
            //La db + la methode de la DAO
            db.listeDAO().miseJourTaches(listeTaches)
        }
    }

    //Supprimer une taches
    fun supprimerUneTache(listeTaches: ListeTaches){
        viewModelScope.launch(Dispatchers.IO){
            db.listeDAO().supprimertaches(listeTaches)
        }
    }

    //Supprimer toutes les taches
    fun supprimerToutesTaches(){
        viewModelScope.launch(Dispatchers.IO){
            db.listeDAO().supprimerTous()
        }
    }

}