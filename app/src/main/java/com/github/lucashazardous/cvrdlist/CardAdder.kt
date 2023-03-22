package com.github.lucashazardous.cvrdlist

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
                    BasicTextField(value = name,
                        onValueChange = { name = it },
                        decorationBox = {
                            BoxDecoration(name, "Name")
                        })
                    Text(text = "")
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
                    Text(text = "")
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

@Composable
fun BoxDecoration(value: String, placeholder: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 64.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, modifier = Modifier.align(Alignment.Center))
        } else {
            Text(text = value, modifier = Modifier.align(Alignment.Center))
        }
    }
}