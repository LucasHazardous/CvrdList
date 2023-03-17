package com.github.lucashazardous.cvrdlist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class Card(
    val name: String,
    val imgUrl: String,
    val cardMarketUrl: String,
    val acquired: Boolean
    )

var cards = mutableListOf(Card("Charizard", "https://images.pokemontcg.io/swsh4/25.png", "", false))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(card: Card) {
    Card {
        Column(
            modifier = Modifier
                .height(150.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(model = card.imgUrl, contentDescription = card.name + " image", modifier = Modifier.size(100.dp))
            Text(card.name)
        }
    }
}