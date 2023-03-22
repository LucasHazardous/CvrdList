package com.github.lucashazardous.cvrdlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

var addGroupOpened = mutableStateOf(false)

@Composable
fun GroupAdder() {
    var name by rememberSaveable { mutableStateOf("") }

    val close = {
        name = ""
        addGroupOpened.value = false
    }

    if (addGroupOpened.value) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            confirmButton = {
                Button(
                    content = {
                        Text("Confirm")
                    },
                    onClick = {
                        if(name.trim().isNotEmpty())
                            cardGroups.add(CardGroup(name, ArrayList(), 0))
                        close()
                    }
                )
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
                }
            }
        )
    }
}