package challenges.chal3

import java.io.File
import java.util.*
import kotlin.random.Random

const val heroName: String = "YoMama"
private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Taric")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data")
    .readText()
    .split("\n")

private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}

private val menuItemPrices: Map<String, Double> =
    List(menuData.size) { index ->
        val (_, name, price) = menuData[index].split(",")
//        Pair(name, price.toDouble()) -> full signature
        name to price.toDouble()
    }.toMap()

private val menuItemTypes: Map<String, String> =
    List(menuData.size) { index ->
        val (type, name, _) = menuData[index].split(",")
        name to type
    }.toMap()

val narrate = { message: String ->
    println(message)
}

private fun main() {
    visitTavern()
}

fun visitTavern() {
//    com.bignerdranch.nihack.narrate("$heroName enters $TAVERN_NAME")
//    com.bignerdranch.nihack.narrate("There are several items for sale:")
//    println(menuItems.joinToString())
//
////    VERY INTERESTING BTW:
//    val (patrons: MutableSet<String>, patronGold) = inviteVisitors()
//
//    com.bignerdranch.nihack.narrate("$heroName sees several patrons in the tavern:")
//    com.bignerdranch.nihack.narrate(patrons.joinToString() + "\n")
//
//    repeat(3) {
//        createPatronOrders(patrons, patronGold)
//    }
//    displayPatronBalances(patronGold)

    val gradesByStudent = mapOf("Josh" to 4.0, "Alex" to 2.0, "Jane" to 3.0)
    println("Original values: $gradesByStudent")
    println("Flipped values: ${flipValues(gradesByStudent)}")

}

private fun flipValues(grades: Map<String, Double>): Map<Double, String> {
    val newGrade = grades.entries.associate {
        it.value to it.key
    }
    return newGrade
}

private fun inviteVisitors(): Pair<MutableSet<String>, MutableMap<String, Double>> {
    val patrons: MutableSet<String> = mutableSetOf()
    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50
    )
    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
        patronGold += patronName to Random.nextDouble(5.00, 26.00)
    }
    return Pair(patrons, patronGold)
}

private fun createPatronOrders(
    patrons: MutableSet<String>,
    patronGold: MutableMap<String, Double>
) {
    var totalPrice = 0.0

    val patronOrder = menuData.shuffled().take(Random.nextInt(1, 4)).associate {
        val (_, name, price) = it.split(",")
        totalPrice += price.toDouble()
        name to price.toDouble()
    }
//    patronOrder += List(Random.nextInt(1,4)) {
//        val (_, name, price) = menuData.random().split(",")
//        totalPrice += price.toDouble()
//        name to price.toDouble()
//    }.toMap()
    placeOrder(patrons.random(), patronOrder, patronGold, totalPrice)
}


private fun placeOrder(
    patronName: String,
    menuItemName: Map<String, Double>,
    patronGold: MutableMap<String, Double>,
    totalPrice: Double
) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")

    val orderNames = menuItemName.keys
    narrate("*$patronName orders ${orderNames.joinToString()}")
    if (totalPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        menuItemName.forEach { (str, _) ->
            val action = when (menuItemTypes[str]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "services"
                else -> "hands"
            }
            narrate("\t-$TAVERN_MASTER $action $patronName a \"$str\"")
        }
        narrate("\t\$$patronName pays $TAVERN_MASTER ${"%.2f".format(totalPrice)} gold\n")
        patronGold[patronName] = patronGold.getValue(patronName) - totalPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + totalPrice
    } else {
        narrate("[DENIED]: $TAVERN_MASTER says, \"You need more coin for a your order\"\n")
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("~$heroName intuitively knows how much money each patron has~")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(Locale.US, balance)} gold")
    }
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