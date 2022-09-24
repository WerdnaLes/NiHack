package com.bignerdranch.nihack

import java.io.File
import java.util.*

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Taric", "Dylan")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider", "Silverhand")

private val menuData = File("data/tavern-menu-data")
    .readLines()
    .map { it.split(",") }

// Old:
//private val com.bignerdranch.nihack.menuItems = List(com.bignerdranch.nihack.menuData.size) { index ->
//    val (_, name, _) = com.bignerdranch.nihack.menuData[index].split(",")
//    name
//}
// Old 2:
//private val com.bignerdranch.nihack.menuItems = com.bignerdranch.nihack.menuData.map { menuEntry ->
//    val (_, name, _) = menuEntry
//    name
//}
private val menuItems = menuData.map { (_, name, _) -> name }

//Old:
//private val com.bignerdranch.nihack.menuItemPrices: Map<String, Double> =
//    List(com.bignerdranch.nihack.menuData.size) { index ->
//        val (_, name, price) = com.bignerdranch.nihack.menuData[index].split(",")
////        Pair(name, price.toDouble()) -> full signature
//        name to price.toDouble()
//    }.toMap()
//Old 2:
//private val com.bignerdranch.nihack.menuItemPrices = com.bignerdranch.nihack.menuData.associate { menuEntry ->
//    val (_, name, price) = menuEntry
//    name to price.toDouble()
//}
private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }

// Old:
//private val com.bignerdranch.nihack.menuItemTypes: Map<String, String> =
//    List(com.bignerdranch.nihack.menuData.size) { index ->
//        val (type, name, _) = com.bignerdranch.nihack.menuData[index].split(",")
//        name to type
//    }.toMap()
//Old 2:
//private val com.bignerdranch.nihack.menuItemTypes = com.bignerdranch.nihack.menuData.associate { menuEntry ->
//    val (type, name, _) = menuEntry
//    name to type
//}
private val menuItemTypes = menuData.associate { (type, name, _) -> name to type }

class Tavern : Room(TAVERN_NAME) {
    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName ->
            "$firstName $lastName"
        }.toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        player.name to 4.50,
        * patrons.map { it to 6.0 }.toTypedArray()
    )

    val itemOfDay = patrons.flatMap {
        getFavoriteMenuItems(it)
    }.random()

    override val status: String = "Busy"

    override val lootBox: LootBox<Key> =
        LootBox(Key("key to Nogartse's evil lair"))

    override fun enterRoom() {
        narrate("${player.name} enters $TAVERN_NAME")
        narrate("There are several items for sale:")
        println(menuItems.joinToString())
        println("The item of the day is the $itemOfDay\n")

//    val patrons: MutableSet<String> = mutableSetOf()
//    val test2 = listOf("some", "2.5").zipWithNext().associate { it.first to it.second.toDouble() }

//    val test = com.bignerdranch.nihack.firstNames.shuffled().zip(com.bignerdranch.nihack.lastNames.shuffled()).associate {(firstName, lastName)->
//        firstName to lastName
//    }
//    println("My test zip: $test")
//    while (patrons.size < 5) {
//        val patronName = "${com.bignerdranch.nihack.firstNames.random()} ${com.bignerdranch.nihack.lastNames.random()}"
//        patrons += patronName
//        patronGold += patronName to 6.0
//    }

//    patrons.forEach { patronName ->
//        patronGold += patronName to 6.0
//    }
        narrate("${player.name} sees several patrons in the tavern:")
        narrate(patrons.joinToString() + "\n")



        placeOrder(patrons.random(), menuItems.random())


//        patrons.filter { patron ->
//            patronGold.getOrDefault(patron, 0.0) < 4.0
//        }.also { departingPatrons ->
//            patrons -= departingPatrons.toSet()
//            patronGold -= departingPatrons.toSet()
//        }.forEach { patron ->
//            narrate("${player.name} sees $patron departing the tavern")
//        }
//        narrate("There are still some patrons in the tavern:")
//        narrate(patrons.joinToString())

        // TESTS:
//    val greeting = patrons.firstOrNull()?.let {
//        "$it walks over to Satyr and says, \"Hi! I'm $it. Welcome to Kronstadt!\""
//    } ?: "Nobody greets Satyr because the tavern is empty"
//    println(greeting)
//
//
//    val tavernPlaylist = mutableListOf("Lonely day", "Toxicity", "Demons")
//    val nowPlayingMessage = tavernPlaylist.run {
//        shuffle()
//        "${first()} is currently playing in the tavern"
//    }
//    println(nowPlayingMessage)
//
//    val healthPoints = 90
//    val healthStatus = run {
//        if (healthPoints == 100) {
//            "perfect health"
//        } else {
//            "has injuries"
//        }
//    }

//    val listOfPrimes = (1..5000).toList().filter { rangeNumber ->
//        com.bignerdranch.nihack.isPrime(rangeNumber)
//    }.take(1000)
//    listOfPrimes.forEach { number ->
//        println(number)
//    }

        // GENERATE SEQUENCE EXPAMPLE
//    val listInMillis = measureTimeMillis {
//        val oneThousandPrimes = generateSequence(3) { value ->
//            value + 1
//        }.filter { number ->
//            com.bignerdranch.nihack.isPrime(number)
//        }.take(1000).forEach {
//            println(it)
//        }
//    }
//    val seconds = (listInMillis / 1000) % 60
//    val minutes = ((listInMillis / 1000) % 3600) / 60
//    val hours = (listInMillis / 1000) / 3600
//    println("List completed in ${"%d:%02d:%02d".format(Locale.US, hours, minutes, seconds)}")

        // REDUCE EXAMPLE:
//    val someTest = listOf("Tu", "pac", " still ", "lives!").reduce { acc, s -> acc + s }
//    println(someTest)
        // FOLD EXAMPLE:
//    val someOtherTest = listOf(2, "pac", " still ", "lives ", 5, "Head").fold("Title: ") { acc, s ->
//        acc + s
//    }
//    println(someOtherTest)

//    val orderSubtotal = com.bignerdranch.nihack.menuItemPrices.getOrDefault("Dranon's Breath", 0.0)
//    val salesTaxPercent = 5
//    val gratuityPercent = 20
//    val feePercentages = listOf(salesTaxPercent, gratuityPercent)
//    val orderTotal1 = feePercentages.fold(orderSubtotal) { acc, i ->
//        acc * (1 + i / 100.0)
//    }

//    val orderTotal = com.bignerdranch.nihack.menuItems.sumOf { item ->
//        com.bignerdranch.nihack.menuItemPrices.getOrDefault(item, 0.0)
//    }
//    println(orderTotal)
    }

    private fun placeOrder(
        patronName: String,
        menuItemName: String
    ) {
        val itemPrice = menuItemPrices.getValue(menuItemName)
        narrate("$patronName speaks with $TAVERN_MASTER to place an order")
        if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
            println("\t~[SUCCESS]~")
            val action = when (menuItemTypes[menuItemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "services"
                else -> "hands"
            }
            narrate("\t-$TAVERN_MASTER $action $patronName a \"$menuItemName\"")
            narrate("\t\$$patronName pays $TAVERN_MASTER $itemPrice gold\n")
            patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
            patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
        } else {
            println("\t~[DENIED]~")
            narrate("\t$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"\n")
        }
    }
}

private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }

        else -> menuItems.shuffled().take(kotlin.random.Random.nextInt(1, 3))
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("${player.name} intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(Locale.US, balance)} gold")
    }
}

fun isPrime(number: Int): Boolean {
    (2 until number)
        .map { divisor ->
            if (number % divisor == 0) {
                return false // Not a prime
            }
        }
    return true
}

fun printConsonants() {
    prefixLoop@ for (prefix in listOf("alpha", "beta")) {
        var number = 0
        numbersLoop@ while (number < 10) {
            val identifier = "$prefix $number"
            if (identifier == "beta 3") {
                break@prefixLoop
            }
            number++
        }
    }
}