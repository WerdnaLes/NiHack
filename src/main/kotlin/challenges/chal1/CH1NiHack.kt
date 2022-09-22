package challenges.chal1

fun main() {
    // Function reference:
    narrate("A hero enters the town of Kronstadt. What is their name?", ::makeYellow)

    // Embedded function:
//    com.bignerdranch.nihack.narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
//        "\u001b[33;1m$message\u001b[0m"
//    }
    val heroName = readLine()
    require(heroName != null && heroName.isNotEmpty()) {
        "The hero must have a name"
    }

    changeNarratorMood()
    narrate("$heroName, ${createTitle(heroName)}, heads to the town square")

//    var some = "Mississippi".count { it == 's' }
//    println(some)

}

private fun makeYellow(message: String) =
    "\u001b[33;1m$message\u001b[0m"

private fun createTitle(name: String): String {
    return when {
        name.length > 15 ->
            "The Verbose"
        name.all { it.isDigit() } ->
            "The Identifiable"
        name.none { it.isLetter() } ->
            "The Witness Protection Member"
        name.count { it.lowercase() in "aeiou" } > 4 ->
            "The Master of Vowels"
        name.all { it.isUpperCase() } ->
            "The Bold"
        name.lowercase().reversed() == name.lowercase() ->
            "Bringer of Palindromes"
        else -> "The Renowned Hero"
    }
}