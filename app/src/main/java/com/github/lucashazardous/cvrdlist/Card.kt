package com.github.lucashazardous.cvrdlist

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

data class Card(
    val name: String,
    val imgUrl: String,
    val cardMarketUrl: String,
    var acquired: Boolean
    )

var cards = mutableStateListOf(Card("Charizard", "https://images.pokemontcg.io/swsh4/25.png", "", false))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(card: Card) {
    var color by rememberSaveable { mutableStateOf(if(card.acquired) "Teal" else "Red") }

    val colorMap = mapOf("Red" to Red, "Teal" to Teal)

    Card(modifier = Modifier.padding(2.dp)) {
        Column(
            modifier = Modifier
                .height(150.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box {
                AsyncImage(model = card.imgUrl, contentDescription = card.name + " image", modifier = Modifier.size(100.dp))
                IconButton(onClick = {
                    card.acquired = !card.acquired
                    color = if(card.acquired) "Teal" else "Red"
                }, colors = IconButtonDefaults.iconButtonColors(contentColor = colorMap[color]!!)) {
                    Icon(Icons.Filled.Star, "Is Acquired", Modifier.size(20.dp))
                }
            }
            Text(card.name)
        }
    }
}