package challenges.chall5_riddle_of_excalibur

import java.util.*

class Sword(name: String) {
    var name = name
        get() = "The Legendary $field"
        set(value) {
            field =
                value.lowercase().reversed()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

    init {
        this.name = name
    }
}

fun main() {
    val sword = Sword("menace")
    println(sword.name)
    sword.name = "rekkless"
    println(sword.name)
}