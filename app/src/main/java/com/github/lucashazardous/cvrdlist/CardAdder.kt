package com.github.lucashazardous.cvrdlist

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.github.lucashazardous.cvrdlist.ui.theme.Red
import com.github.lucashazardous.cvrdlist.ui.theme.Teal

var addCardOpened = mutableStateOf(false)
var loadedSearchCards = mutableStateListOf<Card>()

@Composable
fun CardAdder() {
    var name by rememberSaveable { mutableStateOf("") }

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
                    Text("Cancel")
                }, onClick = {
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = Red))
            },
            dismissButton = {
                Button(content = {
                    Text("Add")
                }, onClick = {
                    close()
                }, colors = ButtonDefaults.buttonColors(containerColor = Teal))
            },
            text = {
                Column {
                    BasicTextField(value = name,
                        onValueChange = { name = it },
                        decorationBox = {
                            BoxDecoration(name, "Name")
                        })
                    Text(text = "")
                    Button(onClick = {
                        ApiRequests.searchCards("name:\"$name\"", 1, 1)
                    }) {
                        Text(text = "search")
                    }
                    Text(text = "")
                    LazyRow {
                        items(loadedSearchCards.size) { item ->
                            CardItem(loadedSearchCards[item])
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
            Text(text = placeholder, modifier = Modifier.align(Alignment.Center))
        } else {
            Text(text = value, modifier = Modifier.align(Alignment.Center))
        }
    }
}