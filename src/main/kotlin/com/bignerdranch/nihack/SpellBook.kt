package com.bignerdranch.nihack

class SpellBook(val spells: List<String>) {
    companion object {
        fun createDefaultSpellBook(): SpellBook =
            SpellBook(
                listOf(
                    "Fireball",
                    "Thundersurge",
                    "Arcane Ammunition",
                    "Reverse Damage"
                )
            )
    }
}