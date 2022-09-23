package com.bignerdranch.nihack

import kotlin.random.Random
import kotlin.system.exitProcess

lateinit var player: Player
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
//    changeNarratorMood() -> FOR LATER


    Game.play()
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
//    val input = readLine()
//    require(!input.isNullOrEmpty()) {
//        "The hero must have a name"
//    }
//    return input
    println("Satyricon")
    return "Satyricon"
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
        narrate("Welcome, adventurer")
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

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)

        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            updateOnMapPosition(oldPosition = currentPosition, newPosition = newPosition)
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You cannot move ${direction.name}")
        }
    }

    fun fight() {
        val monsterRoom = currentRoom as? MonsterRoom
        val currentMonster = monsterRoom?.monster
        if (currentMonster == null) {
            narrate("There's nothing to fight here")
            return
        }

        while (player.healthPoints > 0 &&
            currentMonster.healthPoints > 0
        ) {
            player.attack(currentMonster)
            if (currentMonster.healthPoints > 0) {
                currentMonster.attack(player)
            }
            Thread.sleep(1000)
        }

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
        onMapPosition[oldPosition.y][oldPosition.x] = "O"
        onMapPosition[newPosition.y][newPosition.x] = "X"
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
