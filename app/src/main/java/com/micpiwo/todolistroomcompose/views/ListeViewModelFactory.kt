package com.micpiwo.todolistroomcompose.views

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

//Cette classe appel le listeViewModel et accéde au methode du crud
//Il herite de fournisseur FACTORY (usine a produire des taches)
class ListeViewModelFactory(private val application: Application):ViewModelProvider.Factory {
    //Les lambdas redefinissent les type de retour attendu par la fonction
    //ici On attend la classe ViewModel en paramètre qui est une classe
    //On appel donc dans une condition if la classe ListeViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListeViewModel::class.java)){
            return ListeViewModel(application) as T
        }
        throw IllegalArgumentException("Classe ViewModel inconnue ! ")
    }
}