package com.my.lesson2


fun svetaIvanova (first: String, last: String):String {

    require(!first.isBlank()){"Нужно вставить имя"}
    require(!last.isBlank()){"Нужно вставить фамилию"}
//    requireNotNull(){}
    return "Hello, $first $last"
}


fun main() {
//     println(svetaIvanova(readLine().toString(), readLine().toString()))
//    val line = "aaa 123 gh5"
//    val regexp = """\d{3}"""
//    println()
//
//    do {
//        println("введи \"quit\"")
//        val command = readLine()
//    } while (command != "quit")
}
