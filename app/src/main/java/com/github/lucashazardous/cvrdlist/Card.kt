package com.github.lucashazardous.cvrdlist

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.lucashazardous.cvrdlist.ui.theme.Red
import com.github.lucashazardous.cvrdlist.ui.theme.Teal
import java.io.File
import java.nio.charset.Charset

data class Card(
    val name: String,
    val imgUrl: String,
    val cardMarketUrl: String,
    var acquired: Boolean
    )

var cards = mutableStateListOf<Card>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(card: Card) {
    var color by rememberSaveable { mutableStateOf(if(card.acquired) "Teal" else "Red") }

    val colorMap = mapOf("Red" to Red, "Teal" to Teal)

    Card(modifier = Modifier.padding(5.dp)) {
        Column(
            modifier = Modifier
                .height(155.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(model = card.imgUrl, contentDescription = card.name + " image", modifier = Modifier.size(100.dp))
            Text(card.name, maxLines = 1)
            IconButton(onClick = {
                card.acquired = !card.acquired
                color = if(card.acquired) "Teal" else "Red"
            }, colors = IconButtonDefaults.iconButtonColors(contentColor = colorMap[color]!!)) {
                Icon(Icons.Filled.Star, "Is Acquired", Modifier.size(15.dp))
            }
        }
    }
}

fun saveCardsToFile(ctx: Context) {
    val json = gson.toJson(cards)
    val file = File(ctx.filesDir, "cards.json")
    file.delete()
    file.createNewFile()
    file.writeText(json)
}

fun readFromFile(ctx: Context) {
    val file = File(ctx.filesDir, "cards.json")
    if (!file.createNewFile()) {
        val text = file.readText(Charset.defaultCharset())
        val readCards = gson.fromJson(text, Array<Card>::class.java)
        cards.clear()
        cards.addAll(readCards)
    }
}