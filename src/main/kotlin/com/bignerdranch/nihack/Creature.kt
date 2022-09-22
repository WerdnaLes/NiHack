package com.bignerdranch.nihack

import kotlin.random.Random

interface Fightable {
    val name: String
    var healthPoints: Int
    val diceCount: Int
    val diceSides: Int

    fun takeDamage(damage: Int)

    fun attack(opponent: Fightable) {
        val damageRoll = (0 until diceCount).sumOf {
            Random.nextInt(diceSides + 1)
        }
        narrate("$name deals $damageRoll damage to ${opponent.name}")
        opponent.takeDamage(damageRoll)
    }
}

abstract class Monster(
    override val name: String,
    val description: String,
    override var healthPoints: Int
) : Fightable {
    override fun takeDamage(damage: Int) {
        healthPoints -= damage
    }
}

class Goblin(
    name: String = "Goblin",
    description: String = "A nasty-looking goblin",
    healthPoints: Int = 30
) : Monster(name, description, healthPoints) {
    override val diceCount = 2
    override val diceSides = 8
}

class Draugr(
    name: String = "Draugr",
    description: String = "An ancient menacing creature",
    healthPoints: Int = 50
) : Monster(name, description, healthPoints) {
    override val diceCount = 2
    override val diceSides = 9
}

class Werewolf(
    name: String = "Werewolf",
    description: String = "An abomination hybrid of a human and a wolf",
    healthPoints: Int = 62
) : Monster(name, description, healthPoints) {
    override val diceCount = 3
    override val diceSides = 7
}

class Dragon(
    name: String = "Dragon",
    description: String = "An elder fire-breathing dragon",
    healthPoints: Int = 90
) : Monster(name, description, healthPoints) {
    override val diceCount = 2
    override val diceSides = 12
}