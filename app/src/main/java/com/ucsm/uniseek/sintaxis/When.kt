package com.ucsm.uniseek.sintaxis

fun main(){
    getMonth(4)
    result("ddd")
    getSemestre(3)
}
fun result(value:Any){
    when(value){
        is Int -> value + value
        is  String -> print (value)
        is Boolean -> if (value) print ("holiwi")
    }
}

fun getSemestre(month:Int):String{
   return when(month){
        in 1 .. 6 ->"primer semestre"
        in 7 .. 12 ->"segundo semestre"
        !in 1..12 -> "Semestre no valido"
        else -> "dsd"
    }
}
fun getTrimestrer(month:Int){
    when(month){
        1,2,3->println("primer trimestre")
        4,5,6->println("segundo trimestre")
        7,8,9->println("tercer trimestre")
        10,11,12->println("cuarto trimestre")
        else -> println ("Trimestre no válido")

    }
}
fun getMonth(month:Int){
    when(month){
        1->println("enero")
        2->println("febrero")
        3->println("marzo")
        4->{
            println("abril")
            println("abril")
            println("abril")
        }
        5->println("mayo")
        else -> println("Es un mes entre junio y Diciembre")
    }
}
