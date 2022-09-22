package challenges.chal2

import kotlin.random.Random

// Random mood for narator:
var narrationModifier: (String) -> String = { it }

// With an argument:
/*val com.bignerdranch.nihack.getNarrationModifier = { message: String ->
    val numExclamationPoints = 3
    message.uppercase() + "!".repeat(numExclamationPoints)
}*/

// Classic use:
/*val com.bignerdranch.nihack.getNarrationModifier: (String) -> String = {
    val numExclamationPoints = 3
    it.uppercase() + "!".repeat(numExclamationPoints)
}*/

//  When no return type declared and there are no arguments:
/*val narrationTest = {
    val points = 4
    "text".uppercase() + "!".repeat(points)
}*/


inline fun narrate(
    message: String,
    modifier: (String) -> String = { narrationModifier(it) }
) {
//    println({
//        val numExclamationPoints = 5
//        message.uppercase() + "!".repeat(numExclamationPoints)
//    }())
    println(modifier(message))
}

fun changeNarratorMood() {
    val mood: String
    val modifier: (String) -> String

    when (Random.nextInt(5)) {
        1 -> {
            mood = "loud"
            modifier = { message ->
                val numExclamationPoints = 3
                message.uppercase() + "!".repeat(numExclamationPoints)
            }
        }
        2 -> {
            mood = "tired"
            modifier = { message ->
                message.lowercase().replace(" ", "... ")
            }
        }
        3 -> {
            mood = "unsure"
            modifier = { message ->
                "$message"
            }
        }
        4 -> {
            var narrationsGiven = 0
            mood = "like sending an itemized bill"
            modifier = { message ->
                narrationsGiven++
                "$message.\n(I have narrated $narrationsGiven things)"
            }
        }
        else -> {
            mood = "professional"
            modifier = { message ->
                "$message"
            }
        }
    }
    narrationModifier = modifier
    narrate("The narrator begins to feel $mood")
}