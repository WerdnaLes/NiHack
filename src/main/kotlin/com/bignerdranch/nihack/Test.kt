package com.bignerdranch.nihack

sealed class StudentStatus {
    object NotEnrolled : StudentStatus()
    data class Active(val courseId: String) : StudentStatus()
    object Graduated : StudentStatus();
}

open class Student(private val status: StudentStatus) {

    fun studentMessage(): String {
        return when (status) {
            is StudentStatus.NotEnrolled -> "Please choose a course."
            is StudentStatus.Active -> "You are enrolled in ${status.courseId}!"
            is StudentStatus.Graduated -> "Congratulations!"
        }
    }
}

fun main() {
    val student = Student(StudentStatus.Active("Kotlin101"))
    println(student.studentMessage())
}
