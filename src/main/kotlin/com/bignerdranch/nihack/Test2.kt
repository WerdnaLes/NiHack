package com.bignerdranch.nihack

@JvmInline
value class Kilometers(private val kilometers: Double) {
    val km: String
        get() = "The value is $kilometers"

    operator fun plus(other: Kilometers) =
        Kilometers(kilometers + other.kilometers)

    fun toMiles() = kilometers / 1.609
}

@JvmInline
value class Miles(private val miles: Double) {
    operator fun plus(other: Miles) =
        Miles(miles + other.miles)


    fun toKilometers() = miles * 1.609
}

fun main() {
    val km = (Kilometers(2.0) + Kilometers(3.0)).toMiles()
    println(km)
    var some = Miles(2.0).toKilometers()
    var other = Miles(3.0).toKilometers()
    val newKm = Kilometers(some) + Kilometers(other)
    println(newKm.km)
    println(newKm)
}