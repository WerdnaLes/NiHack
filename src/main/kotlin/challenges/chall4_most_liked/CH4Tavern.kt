package challenges.chall4_most_liked

import com.bignerdranch.nihack.player
import java.io.File
import java.util.*

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Taric", "Dylan")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider", "Silverhand")

private val menuData = File("data/tavern-menu-data")
    .readText()
    .split('\n')
    .map { it.split(",") }

// Old:
//private val menuItems = List(menuData.size) { index ->
//    val (_, name, _) = menuData[index].split(",")
//    name
//}
// Old 2:
//private val menuItems = menuData.map { menuEntry ->
//    val (_, name, _) = menuEntry
//    name
//}
private val menuItems = menuData.map { (_, name, _) -> name }

//Old:
//private val menuItemPrices: Map<String, Double> =
//    List(menuData.size) { index ->
//        val (_, name, price) = menuData[index].split(",")
////        Pair(name, price.toDouble()) -> full signature
//        name to price.toDouble()
//    }.toMap()
//Old 2:
//private val menuItemPrices = menuData.associate { menuEntry ->
//    val (_, name, price) = menuEntry
//    name to price.toDouble()
//}
private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }

// Old:
//private val menuItemTypes: Map<String, String> =
//    List(menuData.size) { index ->
//        val (type, name, _) = menuData[index].split(",")
//        name to type
//    }.toMap()
//Old 2:
//private val menuItemTypes = menuData.associate { menuEntry ->
//    val (type, name, _) = menuEntry
//    name to type
//}
private val menuItemTypes = menuData.associate { (type, name, _) -> name to type }
const val heroName = "Satyricon"
val narrate = { message: String ->
    println(message)
}

fun main() {
    visitTavern()
}

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    println(menuItems.joinToString())

//    val patrons: MutableSet<String> = mutableSetOf()
    val patrons = firstNames.shuffled().zip(lastNames.shuffled()) { firstName, lastName ->
        "$firstName $lastName"
    }.toMutableSet()

//    val test2 = listOf("some", "2.5").zipWithNext().associate { it.first to it.second.toDouble() }

//    val test = firstNames.shuffled().zip(lastNames.shuffled()).associate {(firstName, lastName)->
//        firstName to lastName
//    }
//    println("My test zip: $test")

    println("FirstNames after shuffle: $firstNames\nLast names after shuffle: $lastNames")
    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50,
        * patrons.map { it to 6.0 }.toTypedArray()
    )
//    while (patrons.size < 5) {
//        val patronName = "${firstNames.random()} ${lastNames.random()}"
//        patrons += patronName
//        patronGold += patronName to 6.0
//    }

//    patrons.forEach { patronName ->
//        patronGold += patronName to 6.0
//    }

    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString() + "\n")

    // THE CHALLENGE (FINALLY FCN REWORKED IN ONE FCN LINE XD!:
    val itemOfDay = patrons.flatMap {
        getFavoriteMenuItems(it)
    }.groupingBy { it }.eachCount().let {
        it.filterValues { value ->
            value == it.maxOf { max ->
                max.value
            }
        }.keys.random()
    }

//    val filtered = itemOfDay.filterValues {
//        it == itemOfDay.maxOf { some ->
//            some.value
//        }
//    }.keys.random()

//    val maxString = favoriteItemOfTheDay(itemOfDay)

    println("The item of the day is the $itemOfDay\n")

    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)

    val departingPatrons = patrons.filter { patron ->
        patronGold.getOrDefault(patron, 0.0) < 4.0
    }
    patrons -= departingPatrons.toSet()
    patronGold -= departingPatrons.toSet()
    departingPatrons.forEach { patron ->
        narrate("$heroName sees $patron departing the tavern")
    }
    narrate("There are still some patrons in the tavern:")
    narrate(patrons.joinToString())

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

//    val orderSubtotal = menuItemPrices.getOrDefault("Dranon's Breath", 0.0)
//    val salesTaxPercent = 5
//    val gratuityPercent = 20
//    val feePercentages = listOf(salesTaxPercent, gratuityPercent)
//    val orderTotal1 = feePercentages.fold(orderSubtotal) { acc, i ->
//        acc * (1 + i / 100.0)
//    }

//    val orderTotal = menuItems.sumOf { item ->
//        menuItemPrices.getOrDefault(item, 0.0)
//    }
//    println(orderTotal)
}

// BY USING FREQUENCY METHOD:
private fun favoriteItemOfTheDay(itemOfDay: List<String>): String {
    var maxVal = 0;
    var maxString = ""
    for (item in itemOfDay.distinct()) {
        val frequency = Collections.frequency(itemOfDay, item)
        println("$item: $frequency")
        if (frequency > maxVal) {
            maxVal = frequency
            maxString = item
        } else if (frequency == maxVal) {
            maxString = listOf(item, maxString).random()
        }
    }
    return maxString
}

private fun getFavoriteMenuItems(patron: String): kotlin.collections.List<String> {
    val likedItem = menuItemTypes[menuItems.random()]!!
    return menuItems.filter { menuItem ->
        menuItemTypes[menuItem]?.contains(likedItem) == true
    }

//    val newList = listOf(patron, menuItemTypes[menuItems.random()])
//    return when (patron) {
//        "Alex Ironfoot" -> menuItems.filter { menuItem ->
//            menuItemTypes[menuItem]?.contains("dessert") == true
//        }
//        else -> menuItems.shuffled().take(kotlin.random.Random.nextInt(1, 3))
//    }
}

private fun placeOrder(
    patronName: String,
    menuItemName: String,
    patronGold: MutableMap<String, Double>
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

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitively knows how much money each patron has")
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