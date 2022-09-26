package com.bignerdranch.nihack

fun String.addEnthusiasm(enthusiasmLevel: Int = 1) =
    this + "!".repeat(enthusiasmLevel)

val String.numVowels
    get() = count { it.lowercase() in "aeiou" }

fun <T> T.print(): T {
    println(this)
    return this
}

infix fun Coordinate.move(direction: Direction) =
    direction.updateCoordinate(this)

fun Room?.orEmptyRoom(name: String = "the middle of nowhere") =
    this ?: Room(name)

operator fun MutableList<MutableList<String>>.set(coordinate: Coordinate, setter: String) {
    if (coordinate.x >= 0 && coordinate.y >= 0 &&
        coordinate.y < this.size && coordinate.x < this[coordinate.y].size
    )
        get(coordinate.y)[coordinate.x] = setter
}

operator fun <T, R> T.plus(another: R) where T : Loot, R : Sellable =
    this.name + another.value

infix fun Int.tic(that: Int) = this - that

inline fun <T> T.doTest(block: T.() -> Unit): T {
    block()
    return this
}

inline fun MonsterRoom.configurePitGoblin(
    block: MonsterRoom.(Goblin) -> Goblin
): MonsterRoom {
    val goblin = block(Goblin("Pit Goblin", description = "An Evil Pit Goblin"))
    monster = goblin
    return this
}

fun String.frame(padding: Int, formatChar: String = "*"): String {
    val greeting = "$this!"
    val middle = formatChar
        .padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (0 until middle.length).joinToString("") {
        formatChar
    }
    return "$end\n$middle\n$end"
}