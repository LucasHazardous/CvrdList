package com.github.lucashazardous.cvrdlist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.lucashazardous.cvrdlist.ui.theme.CvrdListTheme
import com.google.gson.Gson

val gson = Gson()

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
                        saveCardsToFile(ctx)
                    }
                    Lifecycle.Event.ON_CREATE -> {
                        readFromFile(ctx)
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
            CenterAlignedTopAppBar(
                navigationIcon = {
                    if (groupOpened.value != -1)
                        IconButton(
                            content = {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    "Back",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            onClick = {
                                groupOpened.value = -1
                            }
                        )
                },
                actions = {
                    if (groupOpened.value != -1)
                        IconButton(
                            content = {
                                Icon(
                                    Icons.Default.Delete,
                                    "Back",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            },
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
                            }
                        )
                },
                title = {
                        Text(
                            "CvrdList",
                            textAlign = TextAlign.Center,
                        )
                }
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
                        }
                    )
                    { Text(text = "OK") }
                },
                text = {
                    Text("Delete selected group?")
                }
            )
        }
        CardAdder(ctx)
        GroupAdder()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 75.dp, 10.dp, 0.dp)
        ) {
            if(cardGroups.size > 0) {
                if(groupOpened.value == -1) {
                    items(
                        cardGroups.size,
                        key = { item ->
                            cardGroups[item].name
                        },
                        itemContent = {item ->
                            CardGroupItem(cardGroups[item])
                        }
                    )
                } else {
                    items(
                        cards.size,
                        key = { item ->
                            cards[item].id
                        },
                        itemContent = {item ->
                            CardItem(ctx, cards[item])
                        }
                    )
                }
            }
        }
    }
}