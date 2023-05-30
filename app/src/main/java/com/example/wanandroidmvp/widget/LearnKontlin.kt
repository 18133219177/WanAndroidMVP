package com.example.wanandroidmvp.widget

fun main() {
    val s1 = Student()
    val s2 = Student("Jake", 19)
    val s3 = Student("001", 100, "Jake", 19)
    println("s1.name=${s1.name}")
    println("s2.name=${s2.name}")
    println("s3.name=${s3.name}")
}

open class Person(val name: String, val age: Int)

class Student(val sno: String, val gradle: Int, name: String, age: Int) : Person(name, age) {
    constructor() : this("", 0) {}

    constructor(name: String, age: Int) : this("", 0, name, age) {}

}
