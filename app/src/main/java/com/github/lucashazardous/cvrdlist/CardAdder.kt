package com.github.lucashazardous.cvrdlist

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lucashazardous.cvrdlist.api.ApiRequests
import com.github.lucashazardous.cvrdlist.ui.theme.Beige
import com.github.lucashazardous.cvrdlist.ui.theme.Black
import com.github.lucashazardous.cvrdlist.ui.theme.Red
import com.github.lucashazardous.cvrdlist.ui.theme.Teal

var addCardOpened = mutableStateOf(false)
var loadedSearchCards = mutableStateListOf(Card("", "", "", false))

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
            containerColor = Black,
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
                            cards.add(copy)
                            cardGroups[groupOpened.value].cards.add(copy)
                        }
                    }
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = Beige))
            },
            dismissButton = {
                Button(content = {
                    Text("Cancel")
                }, onClick = {
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = Red, contentColor = Beige))
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
                            if(name.isNotEmpty()) {
                                if(previousName != name) {
                                    page = 1
                                    previousName = name
                                } else {
                                    page++
                                }
                                ApiRequests.searchCards("name:\"$name\"", 4, page)
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor = Beige, contentColor = Black)) {
                            Text(text = "Search")
                        }
                        Button(onClick = {
                            loadedSearchCards.removeRange(1, loadedSearchCards.size)
                        }, colors = ButtonDefaults.buttonColors(containerColor = Beige, contentColor = Black)) {
                            Text(text = "Clear")
                        }
                    }
                    Text(text = "")
                    LazyRow {
                        items(loadedSearchCards.size) { item ->
                            CardItem(ctx, loadedSearchCards[item])
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
                color = Beige,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, modifier = Modifier.align(Alignment.Center), color = Beige)
        } else {
            Text(text = value, modifier = Modifier.align(Alignment.Center), color = Beige)
        }
    }
}