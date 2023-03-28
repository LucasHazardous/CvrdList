package com.github.lucashazardous.cvrdlist

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lucashazardous.cvrdlist.api.ApiRequests

var addCardOpened = mutableStateOf(false)
var loadedSearchCards = mutableStateListOf<Card>()
var cardSearchIdCounter = 0

@Composable
fun CardAdder(ctx: Context) {
    var name by rememberSaveable { mutableStateOf("") }
    var previousName by rememberSaveable { mutableStateOf("") }
    var page by rememberSaveable { mutableStateOf(0) }

    val close = {
        name = ""
        addCardOpened.value = false
    }

    if (addCardOpened.value) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            confirmButton = {
                Button(content = {
                    Text("Confirm")
                }, onClick = {
                    for(card in loadedSearchCards) {
                        if(card.acquired) {
                            card.acquired = false
                            val copy = card.copy()
                            copy.id = ++cardGroups[groupOpened.value].lastId
                            cards.add(copy)
                            cardGroups[groupOpened.value].cards.add(copy)
                        }
                    }
                    close()
                })
            },
            dismissButton = {
                Button(content = {
                    Text("Cancel")
                }, onClick = {
                    close()
                })
            },
            text = {
                Column {
                    OutlinedTextField(value = name,
                        onValueChange = { name = it },
                        label = {
                            Text("Name")
                        },
                        singleLine = true,
                        maxLines = 1
                    )
                    Text("")
                    Row {
                        Button(onClick = {
                            cardSearchIdCounter = 0
                            loadedSearchCards.clear()
                        }) {
                            Text(text = "Clear")
                        }
                        Spacer(Modifier.weight(1f))
                        Button(onClick = {
                            if(name.isNotEmpty()) {
                                if(previousName != name) {
                                    page = 1
                                    previousName = name
                                } else {
                                    page++
                                }
                                ApiRequests.searchCards("name:\"$name\"", 4, page, "-set.releaseDate")
                            }
                        }) {
                            Text(text = "Search")
                        }
                    }
                    Text("")
                    LazyRow {
                        if (loadedSearchCards.size == 0) {
                            item { Box(modifier = Modifier.height(160.dp)) {} }
                        } else {
                            items(
                                loadedSearchCards.size,
                                key = { item ->
                                    loadedSearchCards[item].id
                                },
                                itemContent = {item ->
                                    CardItem(ctx, loadedSearchCards[item])
                                }
                            )
                        }

                    }
                }
            }
        )
    }
}