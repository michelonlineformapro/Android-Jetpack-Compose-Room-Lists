package com.micpiwo.todolistroomcompose

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.micpiwo.todolistroomcompose.entity.ListeTaches
import com.micpiwo.todolistroomcompose.ui.theme.TodoListRoomComposeTheme
import com.micpiwo.todolistroomcompose.views.ListeViewModel
import com.micpiwo.todolistroomcompose.views.ListeViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListRoomComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GetScaffold()
                }
            }
        }
    }
}

/*
Un échafaudage est une mise en page qui implémente la structure de mise en page de conception matérielle de base.
 Vous pouvez ajouter des choses comme une TopBar, une BottomBar, une FAB ou un Drawer.
 */
@Composable
fun GetScaffold() {
    //Etat general de App + mise en cache = remember
    val scaffoldState: ScaffoldState = rememberScaffoldState(snackbarHostState = SnackbarHostState())
    //Echafaudage
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(text = "Liste des taches", color = Color.White)},
                backgroundColor = Color(0xFFFDA433),
            )
        },
        //Ici le composable MainContent = function ContenuPrincipale(+ echafaud)
        content = {ContenuPrincipale(scaffoldState)},
        backgroundColor = Color(0xFFBEEFF5)
        )
}

@Composable
fun ContenuPrincipale(scaffoldState: ScaffoldState){
    //evite mes efet secondaire + contrôler manuellement le cycle de vie de app
    val portee = rememberCoroutineScope()
    val contexte = LocalContext.current
    //on lie mainActivity a l'usine de generation de tache = ListeViewModelFactory
    val modele: ListeViewModel = viewModel(factory = ListeViewModelFactory(contexte.applicationContext as Application))

    //On stocke dans une variable la valeur de input (de la tache entrée par l'utilisateur)
    val liste:List<ListeTaches> = modele.listeTaches.observeAsState(listOf()).value
    //On vide l'input et on met en cache
    val textState = remember{ mutableStateOf("")}
    //La boite parente
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
        contentAlignment = Alignment.Center
    ){
        //Une colonne
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                    ),
                value = textState.value,
                onValueChange = {textState.value = it},
                placeholder = {
                    Text(text = "Votre tache : ")
                }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    //on appel la methode de la ListeViewModel + Unit
                    modele.ajouterUneTache(ListeTaches(null, UUID.randomUUID().toString(), textState.value))
                    portee.launch {
                        //on vide le champ
                        textState.value = ""
                        scaffoldState.snackbarHostState.showSnackbar(message = "Erreur lors de l'ajout de la tache")
                    }
                }) {
                    Text(text = "Ajouter une tache")
                }
                //Bouton tous supprimer
                Button(onClick = { modele.supprimerToutesTaches()
                    portee.launch { scaffoldState.snackbarHostState.showSnackbar(
                        message = "Toutes les taches on été supprimée !"
                    ) }
                }) {
                    //Texte du bouton
                    Text(text = "Tous supprimer")
                }
            }

            //Chargement lazy = charge que ce qui est visible
            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)){
                items(liste.size){
                    index ->
                    Card(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically)
                    ){
                        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            //id dynamique (comme es6 js)
                           Text(text = "${liste[index].id}",
                                fontWeight = FontWeight.Bold,
                               modifier = Modifier.padding(start = 12.dp)
                               )
                            //le nom de l'utilisateur
                            Text(text = ":" + liste[index].nomUtilisateurs.take(10))
                            //la tache
                            Text(text = ":" + liste[index].notes, style = TextStyle(color = if(liste[index].id!! > 33)
                                Color(0xFF3B7A57)
                            else
                                Color(0xFFAB274F)
                                ), modifier = Modifier.weight(2f))
                            IconButton(onClick = {
                                //au clic sur une tache
                                liste[index].notes = textState.value
                                //Mise a jour de ma tache methode de la classe ListeViewModel
                                modele.updateTache(liste[index])
                                //la notif
                                portee.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("La tache a été mise à jour ! " + ": ${liste[index].id}")
                                    //on vide le champ
                                    textState.value = ""
                                }
                            }) {
                                Icon(Icons.Filled.Edit, "", tint = Color.Magenta)
                            }
                            IconButton(onClick = {
                                modele.supprimerUneTache(liste[index])
                                //la notif
                                portee.launch { scaffoldState.snackbarHostState.showSnackbar("La tache a bien été supprimée " + ":${liste[index].id}")
                                //on vide le champ
                                    textState.value = ""
                                }
                            }) {
                                Icon(Icons.Filled.Delete,"", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListRoomComposeTheme {
        GetScaffold()
    }
}