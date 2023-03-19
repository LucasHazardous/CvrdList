package com.github.lucashazardous.cvrdlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.github.lucashazardous.cvrdlist.ui.theme.Beige
import com.github.lucashazardous.cvrdlist.ui.theme.Black
import com.github.lucashazardous.cvrdlist.ui.theme.Red
import com.github.lucashazardous.cvrdlist.ui.theme.Teal

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
            containerColor = Black,
            onDismissRequest = {
                close()
            },
            confirmButton = {
                Button(
                    content = {
                        Text("Confirm")
                    },
                    onClick = {
                        cardGroups.add(CardGroup(name, ArrayList()))
                        close()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal,
                        contentColor = Beige
                    )
                )
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
                }
            }
        )
    }
}