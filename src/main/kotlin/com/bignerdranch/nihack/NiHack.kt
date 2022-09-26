package com.bignerdranch.nihack

import kotlin.random.Random
import kotlin.system.exitProcess

lateinit var player: Player
private operator fun List<List<Room>>.get(coordinate: Coordinate) =
    getOrNull(coordinate.y)?.getOrNull(coordinate.x)

//var some by Delegates.notNull<Boolean>()


fun main() {
    // Embedded function:
//    com.bignerdranch.nihack.narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
//        "\u001b[33;1m$message\u001b[0m"
//    }

    /*//LATEINIT NULL CHECK:
if (::player.isInitialized) {
    narrate("Welcome, ${player.name}")
}*/

    player = Player(promptHeroName())
    changeNarratorMood()

    println("Welcome, ${player.name}".frame(5))
//    val lootBox = LootBox.random()
//    val loot = lootBox.takeLootOfType<Hat>()
//    player.name.print().addEnthusiasm(4).print().run { }
//    val anotherLoot = Fedora("some ", 4)
//    val someSome = Gemstones(20)
//
//    val test = anotherLoot + someSome
//    println(test)
    Game.play()
}

fun frame(
    name: String, padding: Int,
    formatChar: String = "*"
): String {
    val greeting = "$name!"
    val middle = formatChar
        .padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (0 until middle.length).joinToString("") {
        formatChar
    }
    return "$end\n$middle\n$end"
}

fun printIsSourceOfBlessings(any: Any) {
    val isSourceOfBlessings: Boolean = if (any is Player) {
        any.title == "Blessed"
    } else if (any is Room) {
        any.name == "The Fount of Blessings"
    } else false
    println("$any is a source of blessings: $isSourceOfBlessings")
}

fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?", ::makeYellow)
    // NEEDED LATER:
    val input = readLine()
    require(!input.isNullOrEmpty()) {
        "The hero must have a name"
    }
    return input
}

object Game {
    private val worldMap = listOf(
        listOf(TownSquare(), Tavern()),
        listOf(Room("Generic Room"), MonsterRoom("Dark Room"), Room("Empty Room")),
        listOf(MonsterRoom("Back Room", Draugr()), Room("Empty Room"), MonsterRoom("Sussy room", Werewolf())),
        listOf(MonsterRoom("Dungeon", Dragon()))
    )

    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)
    var endGame = false

    private val onMapPosition: MutableList<MutableList<String>> = mutableListOf()

    init {
//        narrate("Welcome, adventurer")
        val mortality = if (player.isImmortal) "an immortal" else "a mortal"
        narrate("${player.name}, $mortality, has ${player.healthPoints} health points")
        initOnMapPosition()
//        narrate("${player.name}'s inventory: ${player.inventory}")

    }

    private fun initOnMapPosition() {
        for (element in worldMap) {
            val tempList = mutableListOf<String>()
            for (y in element.indices) {
                tempList += "O"
            }
            onMapPosition += tempList
        }
        onMapPosition[0][0] = "X"
    }

    fun play() {
        while (!endGame) {
            narrate(
                "${player.name} of ${player.hometown}, ${player.title}, " +
                        "is in ${currentRoom.description()}"
            )
            currentRoom.enterRoom()

            print("> Enter your command: ")
            GameInput(readLine()).processCommand()
        }
    }

    fun configureCurrentRoom() {
        val monsterRoom = currentRoom as? MonsterRoom ?: return

        monsterRoom.configurePitGoblin {
            it.healthPoints = when {
                "Haunted" in name -> 60
                "Dungeon" in name -> 45
                "Town Square" in name -> 15
                else -> 30
            }
            it
        }
    }

    fun takeLoot() {
        val loot = currentRoom.lootBox.takeLoot()
        if (loot == null) {
            narrate("${player.name} approaches the loot box, but it is empty")
        } else {
            narrate("${player.name} now has a ${loot.name}")
            player.inventory += loot
        }
    }

    fun sellLoot() {
        when (val currentRoom = currentRoom) {
            is TownSquare -> {
                if (player.inventory.size <= 0) {
                    narrate("You have nothing to sell")
                    return
                }
                player.inventory.forEach { item ->
                    if (item is Sellable) {
                        val sellPrice = currentRoom.sellLoot(item)
                        narrate("Sold ${item.name} for $sellPrice gold")
                        player.gold += sellPrice
                    } else {
                        narrate("Your ${item.name} can't be sold")
                    }
                }
                player.inventory.removeAll { it is Sellable }
            }

            else -> narrate("You cannot sell anything here")
        }
    }

    fun move(direction: Direction) {
        val newPosition = currentPosition move direction
        val newRoom = worldMap[newPosition].orEmptyRoom()

        narrate("The hero moves ${direction.name}")
        updateOnMapPosition(oldPosition = currentPosition, newPosition = newPosition)
        currentPosition = newPosition
        currentRoom = newRoom

    }

    fun fight() {
        val monsterRoom = currentRoom as? MonsterRoom
        val currentMonster = monsterRoom?.monster
        if (currentMonster == null) {
            narrate("There's nothing to fight here")
            return
        }

        var combatRound = 0
        val previousNarrationModifier = narrationModifier
        narrationModifier = {
            it.addEnthusiasm(enthusiasmLevel = combatRound)
        }

        while (player.healthPoints > 0 &&
            currentMonster.healthPoints > 0
        ) {
            combatRound++
            player.attack(currentMonster)
            if (currentMonster.healthPoints > 0) {
                currentMonster.attack(player)
            }
            Thread.sleep(1000)
        }
        narrationModifier = previousNarrationModifier

        if (player.healthPoints <= 0) {
            narrate("You have been defeated! Thanks for playing")
            exitProcess(0)
        } else {
            narrate("${currentMonster.name} has been defeated")
            monsterRoom.monster = null
        }
    }

    fun cast(spell: String) {
        narrate("${player.name} casts a $spell")
        narrate("A glass of $spell springs into existence \"(x2)\"")
    }

    private fun updateOnMapPosition(oldPosition: Coordinate, newPosition: Coordinate) {
        onMapPosition[oldPosition] = "O"
        onMapPosition[newPosition] = "X"
    }

    fun printMap() {
        onMapPosition.forEach { println(it) }
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() = when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values().firstOrNull { it.name.equals(argument, true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }

            "cast" -> {
                val spell = SpellBook.createDefaultSpellBook().spells.firstOrNull { it.equals(argument, true) }
                if (spell != null) {
                    cast(spell)
                } else narrate("You haven't learned that spell yet")
            }

            "prophesize" -> player.prophesize()
            "map" -> printMap()
            "take" -> {
                if (argument.equals("loot", true)) {
                    takeLoot()
                } else {
                    narrate("I don't know what you're trying to take")
                }
            }

            "config" -> configureCurrentRoom()

            "sell" -> {
                if (argument.equals("loot", true)) {
                    sellLoot()
                } else {
                    narrate("I don't know what you're trying to sell")
                }
            }

            "inventory" -> player.inventory.forEach { println(it.name) }
            "ring" -> TownSquare().ringBell()
            "fight" -> fight()
            "hp" -> println(player.healthPoints)
            "exit" -> {
                narrate("${player.name} leaves the world. For now...")
                endGame = true
            }

            else -> narrate("I'm not sure what you're trying to do")
        }
    }
}

val abandonedTownSquare = object : TownSquare() {
    override val status: String
        get() = "Empty"

    override fun enterRoom() {
        narrate("The hero anticipated applause, but no one is here...")
    }
}

private fun makeYellow(message: String) =
    "\u001b[33;1m$message\u001b[0m"
