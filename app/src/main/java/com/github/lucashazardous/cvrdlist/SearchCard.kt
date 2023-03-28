package com.github.lucashazardous.cvrdlist

data class SearchCards(val data: List<SearchCard>)

fun SearchCard.toCard(newId: Int) = Card(
    id = newId,
    name = name + " " + set.name,
    imgUrl = images.small,
    cardMarketUrl = cardmarket.url,
    acquired = false
)

data class SearchCard (
    val name: String,
    val images: DataImages,
    val cardmarket: Cardmarket,
    val set: Set
)

data class Set (
    val name: String
)

data class DataImages (
    val small: String
)

data class Cardmarket (
    val url: String
)
