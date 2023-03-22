package com.github.lucashazardous.cvrdlist

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.io.File
import java.nio.charset.Charset

var cardGroups = mutableStateListOf<CardGroup>()
var groupOpened = mutableStateOf(-1)
var deleteGroupQuestion = mutableStateOf(false)
lateinit var cardGroupToDelete: CardGroup

data class CardGroup(val name: String, var cards: ArrayList<Card>, var lastId: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGroupItem(cardGroup: CardGroup) {
    Card(modifier = Modifier
        .padding(5.dp)
        .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(7.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    cards.clear()
                    cards.addAll(cardGroup.cards)
                    groupOpened.value = cardGroups.indexOf(cardGroup)
                },
                onLongPress = {
                    cardGroupToDelete = cardGroup
                    deleteGroupQuestion.value = true
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .height(155.dp)
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .wrapContentHeight()
        ){
            Text(cardGroup.name, textAlign = TextAlign.Center)
            Text(cardGroup.cards.size.toString(),
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

fun saveCardsToFile(ctx: Context) {
    val json = gson.toJson(cardGroups)
    val file = File(ctx.filesDir, "cards.json")
    file.delete()
    file.createNewFile()
    file.writeText(json)
}

fun readFromFile(ctx: Context) {
    val file = File(ctx.filesDir, "cards.json")
    if (!file.createNewFile()) {
        val text = file.readText(Charset.defaultCharset())
        val readCards = gson.fromJson(text, Array<CardGroup>::class.java)
        if(readCards != null) {
            cardGroups.clear()
            cardGroups.addAll(readCards)
        }
    }
}