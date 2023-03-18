package com.github.lucashazardous.cvrdlist

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.lucashazardous.cvrdlist.ui.theme.*
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
fun CardItem(ctx: Context, card: Card) {
    var color by rememberSaveable { mutableStateOf(if(card.acquired) "Teal" else "Red") }

    val colorMap = mapOf("Red" to Red, "Teal" to Teal)
    val isNightMode = ctx.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    Card(modifier = Modifier
        .padding(5.dp)
        .border(2.dp, if (isNightMode) Beige else Purple80, RoundedCornerShape(7.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    if(card.cardMarketUrl.isNotEmpty())
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(card.cardMarketUrl)))
                }
            )
        }) {
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