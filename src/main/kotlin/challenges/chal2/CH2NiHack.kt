package challenges.chal2

var heroName: String = ""

fun main() {
    // Embedded function:
//    com.bignerdranch.nihack.narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
//        "\u001b[33;1m$message\u001b[0m"
//    }
    heroName = promptHeroName()
//    com.bignerdranch.nihack.changeNarratorMood() -> FOR LATER
    narrate("$heroName, ${createTitle(heroName)}, heads to the town square")
    visitTavern()
}

fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?", ::makeYellow)
    // NEEDED LATER:
    /*val input = readLine()
    require(heroName != null && heroName.isNotEmpty()) {
        "The hero must have a name"
    }
    return input*/
    println("Satyricon")
    return "Satyricon"


}

private fun makeYellow(message: String) =
    "\u001b[33;1m$message\u001b[0m"

private fun createTitle(name: String): String {
    return when {
        name.all { it.isDigit() } ->
            "The Identifiable"
        name.none { it.isLetter() } ->
            "The Witness Protection Member"
        name.count { it.lowercase() in "aeiou" } > 4 ->
            "The Master of Vowels"
        else -> "The Renowned Hero"
    }
}