package com.bignerdranch.nihack

open class TownSquare : Room("The Town Square") {
    override val status: String
        get() = "Bustling"
    private var bellSound = "GWONG"


     override fun enterRoom() {
        narrate("The villagers rally and cheer as the hero enters")
        ringBell()
    }

    fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }
}