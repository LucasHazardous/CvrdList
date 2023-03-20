package com.github.lucashazardous.cvrdlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.lucashazardous.cvrdlist.ui.theme.Beige
import com.github.lucashazardous.cvrdlist.ui.theme.Black
import com.github.lucashazardous.cvrdlist.ui.theme.CvrdListTheme
import com.github.lucashazardous.cvrdlist.ui.theme.Red
import com.google.gson.Gson

val gson = Gson()
var refresh = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            CycleObserver(applicationContext, lifecycleOwner)

            CvrdListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CvrdListView(this)
                }
            }
        }
    }
}

@Composable
fun CycleObserver(ctx: Context, lifecycle: LifecycleOwner) {
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver {
                _, event ->
            run {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        if(!refresh)
                            saveCardsToFile(ctx)
                        refresh = false
                    }
                    Lifecycle.Event.ON_CREATE -> {
                        if(!refresh)
                            readFromFile(ctx)
                        refresh = false
                    }
                    else -> {}
                }
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose { lifecycle.lifecycle.removeObserver(observer) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvrdListView(ctx: ComponentActivity) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Row {
                        if (groupOpened.value != -1) {
                            Button(
                                content = { Text(text = "Back") },
                                onClick = {
                                    groupOpened.value = -1
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Beige,
                                    contentColor = Black
                                )
                            )
                            Button(
                                content = { Text(text = "Clear acquired") },
                                onClick = {
                                    var removed = 0
                                    for (i in 0 until cards.size) {
                                        if (cards[i - removed].acquired) {
                                            cards.removeAt(i - removed)
                                            removed++
                                        }
                                    }
                                    cardGroups[groupOpened.value].cards.clear()
                                    cardGroups[groupOpened.value].cards.addAll(cards)
                                    refresh = true
                                    val intent = Intent(ctx, MainActivity::class.java)
                                    ctx.startActivity(intent)
                                    ctx.finish()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Beige,
                                    contentColor = Black
                                )
                            )
                        }
                    }
                }, colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Black,
                    titleContentColor = Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (groupOpened.value == -1) {
                        addGroupOpened.value = !addGroupOpened.value
                    } else {
                        addCardOpened.value = !addCardOpened.value
                    }

                },
                containerColor = Beige, contentColor = Red
            ) {
                Icon(
                    Icons.Filled.Add, "Add card",
                    modifier = Modifier.size(20.dp)
                )
            }
        }) { _ ->
        if (deleteGroupQuestion.value) {
            AlertDialog(
                onDismissRequest = {
                    deleteGroupQuestion.value = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            cardGroups.remove(cardGroupToDelete)
                            deleteGroupQuestion.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Beige,
                            contentColor = Black
                        )
                    )
                    { Text(text = "OK") }
                },
                text = {
                    Text("Delete selected group?", color = Beige)
                },
                containerColor = Black
            )
        }
        CardAdder(ctx)
        GroupAdder()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 75.dp, 10.dp, 0.dp),
        ) {
            if(cardGroups.size > 0) {
                if(groupOpened.value == -1) {
                    items(cardGroups.size) { item ->
                        CardGroupItem(ctx, cardGroups[item])
                    }
                } else {
                    items(cards.size) { item ->
                        CardItem(ctx, cards[item])
                    }
                }
            }
        }
    }
}