package challenges.chal2

import java.io.File

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Taric")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data")
    .readLines()

private val menuItems = List(menuData.size) { index ->
    val (_, name, price) = menuData[index].split(",")
    name
}


fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME and reads the table:")
    val welcome = "*** Welcome to $TAVERN_NAME ***"
    val maxLength: Int = welcome.length
    println()
    narrate(welcome)

    showCategorizedMenu(maxLength)

//    showMenu(maxLength, myMenuList)
}

private fun showCategorizedMenu(maxLength: Int) {
    val categorizedMenu = List(menuData.size) { index ->
        val (type, _, _) = menuData[index].split(",")
        type
    }.distinct()

    categorizedMenu.forEach { myType ->
        val typePadding = (maxLength + myType.length) / 2
        println("~[$myType]~".padStart(typePadding))

        menuData.forEach { menuDataBase ->
            val (type, name, price) = menuDataBase.split(",")
            if (type == myType) {
                val priceLen = price.length
                println("PriceLen: $priceLen")
                val dif = (maxLength - priceLen) - name.length
                val point = "."
                println("$name${point.repeat(dif)}$price")
            }
        }
    }
}

private fun showMenu(maxLength: Int) {
    menuData.forEach {
        val (_, name, price) = it.split(",")
        val priceLen = price.toDouble().toString().length
        val dif = (maxLength - priceLen) - name.length
        val point = "."
        println("$name${point.repeat(dif)}$price")
    }
}


private fun placeOrder(patronName: String, menuItemName: String) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("\t$TAVERN_MASTER hands $patronName a \"$menuItemName\"")
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