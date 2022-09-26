package com.bignerdranch.nihack

import java.io.File

class Player(
    initialName: String,
    val hometown: String = "Neversummer",
    override var healthPoints: Int,
    val isImmortal: Boolean
) : Fightable {
    override var name = initialName
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }
    override val diceCount = 3
    override val diceSides = 4


    val title: String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable"
            name.none { it.isLetter() } -> "The Witness Protection Member"
            name.numVowels > 4 -> "The Master of Vowels"
            else -> "The Renowned Hero"
        }

    private val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophesy upon $name")

        "And intrepid hero from $hometown shall some day " +
                listOf(
                    "form an unlikely bond between two warring factions",
                    "take possession of an otherworldly blade",
                    "bring the gift of creation back to the world",
                    "best the world-eater"
                ).random()
    }


    val inventory = mutableListOf<Loot>()

    var gold = 0

    //    private var isArenaOpen = false
//    private lateinit var opponentName: String
//    fun prepareArena() {
//        isArenaOpen = true
//        opponentName = getWillingCombatants().random()
//    }
//    private fun getWillingCombatants() =
//        listOf("Cornelius", "Cheryl")
//    init {
//        val baseInventory = listOf("Waterskin", "torches")
//        val classInventory = when (name) {
//            "archer" -> listOf("arrows")
//            "Wizard" -> listOf("arcane staff", "spellbook")
//            else -> emptyList()
//        }
//        inventory = baseInventory + classInventory
//    }

    constructor(name: String) : this(
        initialName = name,
        healthPoints = 100,
        isImmortal = false
    ) {
        if (name.equals("Jason", ignoreCase = true)) {
            healthPoints = 500
        }
    }

//    companion object {
//        private const val SAVE_FILE_NAME = "player.dat"
//
//        fun fromSaveFile() =
//            Player(File(SAVE_FILE_NAME).readBytes())
//    }

    override fun takeDamage(damage: Int) {
        if (!isImmortal) {
            healthPoints -= damage
        }
    }

    override fun toString(): String {
        val mortality = when (isImmortal) {
            true -> "Immortal"
            else -> "Mortal"
        }
        return """
            ~~
            Name: $name
            Hometown: $hometown
            HP: $healthPoints
            $mortality
            ~~
        """.trimIndent()
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x$numFireballs)")
    }

    fun changeName(newName: String) {
        narrate("$name legally changes their name to $newName")
        name = newName
    }

    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told $name, \"$prophecy\"")
    }
}
