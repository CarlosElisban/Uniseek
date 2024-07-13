package com.ucsm.uniseek.sintaxis

fun main(){
    var name:String = "Aristemo"

    val weekDays= arrayOf("Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo")
    println(weekDays[3])
    println(weekDays.size)

    //Bucles para array
    for(position in weekDays.indices){
        println(weekDays[position])
    }
    for((position,value) in weekDays.withIndex()){
        println("La posicion $position contiene $value")
    }
    for (weekDay in weekDays){
        println ("Ahora es $weekDay")
    }
}