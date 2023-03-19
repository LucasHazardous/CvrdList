package com.github.lucashazardous.cvrdlist

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.github.lucashazardous.cvrdlist.ui.theme.Beige
import com.github.lucashazardous.cvrdlist.ui.theme.Purple80
import com.github.lucashazardous.cvrdlist.ui.theme.Teal
import java.io.File
import java.nio.charset.Charset

var cardGroups = mutableStateListOf<CardGroup>()
var groupOpened = mutableStateOf(-1)

data class CardGroup(val name: String, var cards: List<Card>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGroupItem(ctx: Context, cardGroup: CardGroup) {
    val isNightMode = ctx.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    Card(modifier = Modifier
        .padding(5.dp)
        .border(2.dp, if (isNightMode) Beige else Purple80, RoundedCornerShape(7.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    groupOpened.value = cardGroups.indexOf(cardGroup)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .height(155.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(cardGroup.name)
            Text(cardGroup.cards.size.toString(), color = Teal)
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
    file.delete()
    if (!file.createNewFile()) {
        val text = file.readText(Charset.defaultCharset())
        val readCards = gson.fromJson(text, Array<CardGroup>::class.java)
        cardGroups.clear()
        cardGroups.addAll(readCards)
    }
}