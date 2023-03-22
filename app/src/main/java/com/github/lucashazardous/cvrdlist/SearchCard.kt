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
    val id: String,
    val name: String,
    val supertype: String,
    val subtypes: List<String>,
    val hp: String,
    val types: List<String>,
    val evolvesTo: List<String>,
    val rules: List<String>,
    val attacks: List<Attack>,
    val weaknesses: List<Weakness>,
    val retreatCost: List<String>,
    val convertedRetreatCost: Long,
    val set: Set,
    val number: String,
    val artist: String,
    val rarity: String,
    val nationalPokedexNumbers: List<Long>,
    val legalities: Legalities,
    val images: DataImages,
    val tcgplayer: Tcgplayer,
    val cardmarket: Cardmarket
)

data class Attack (
    val name: String,
    val cost: List<String>,
    val convertedEnergyCost: Long,
    val damage: String,
    val text: String
)

data class DataImages (
    val small: String,
    val large: String
)

data class Legalities (
    val unlimited: String,
    val expanded: String
)

data class Set (
    val id: String,
    val name: String,
    val series: String,
    val printedTotal: Long,
    val total: Long,
    val legalities: Legalities,
    val ptcgoCode: String,
    val releaseDate: String,
    val updatedAt: String,
    val images: SetImages
)

data class SetImages (
    val symbol: String,
    val logo: String
)

data class Tcgplayer (
    val url: String,
    val updatedAt: String,
    val prices: Prices
)

data class Prices (
    val holofoil: Holofoil
)

data class Holofoil (
    val low: Double,
    val mid: Double,
    val high: Double,
    val market: Double,
    val directLow: Double
)

data class Weakness (
    val type: String,
    val value: String
)

data class Cardmarket (
    val url: String,
    val updatedAt: String,
    val prices: Map<String, Double>
)
