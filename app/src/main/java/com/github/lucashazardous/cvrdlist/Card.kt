package com.github.lucashazardous.cvrdlist

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class Card(
    var id: Int,
    val name: String,
    val imgUrl: String,
    val cardMarketUrl: String,
    var acquired: Boolean
    )

var cards = mutableStateListOf<Card>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(ctx: Context, card: Card) {

    var color by rememberSaveable { mutableStateOf("") }
    val colorSelect = {
        color = if(card.acquired) "Error" else "Secondary"
    }
    colorSelect()
    val colorMap = mapOf("Error" to MaterialTheme.colorScheme.error, "Secondary" to MaterialTheme.colorScheme.secondary)

    Card(
        modifier = Modifier
        .padding(5.dp)
        .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(7.dp))
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
                .fillMaxWidth()
                .height(155.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(model = card.imgUrl, contentDescription = card.name + " image", modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally))
            Text(card.name, maxLines = 1, textAlign = TextAlign.Center)
            IconButton(onClick = {
                card.acquired = !card.acquired
                colorSelect()
            }, colors = IconButtonDefaults.iconButtonColors(contentColor = colorMap[color]!!)) {
                Icon(Icons.Filled.Star, "Is Acquired", Modifier.size(24.dp))
            }
        }
    }
}