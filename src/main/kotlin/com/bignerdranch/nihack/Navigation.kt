package com.bignerdranch.nihack

data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
}

enum class Direction(private val directionCoordinate: Coordinate) {
    North(directionCoordinate = Coordinate(0, -1)),
    East(directionCoordinate = Coordinate(1, 0)),
    South(directionCoordinate = Coordinate(0, 1)),
    West(directionCoordinate = Coordinate(-1, 0));

    fun updateCoordinate(coordinate: Coordinate) =
//        Coordinate(
//            x = coordinate.x + directionCoordinate.x,
//            y = coordinate.y + directionCoordinate.y
//        )
        coordinate + directionCoordinate
}

