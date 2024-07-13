package com.ucsm.uniseek.sintaxis

fun main(){
    ifMultiple()
}

fun ifMultipleOr(){
    var pet = "dog"
    var isHappy = true

    if(pet == "dog" || (pet =="cat"&& isHappy)){
        println("Es un gato o un perro")
    }
}
fun ifMultiple(){
    var age = 18
    var parentPermission= false
    var imHappy = true

    if(age>=18 && parentPermission && imHappy){
        println("Puedo beber")
    }

}

fun ifInt(){
    var age = 18
    if(age>=18){
        println("Beber cerveza")
    }else{
        println("Beber jugo")
    }
}


fun ifBoolean(){
    var soyFeliz:Boolean = false

    if(!soyFeliz){
        println("Estoy triste :(")
    }
}
fun ifAnidado(){
    val animal = "dog"
    if (animal == "dog"){
        println("Es un perrito")
    }else if (animal == "cat"){
        println("Es un gato")
    }else if (animal == "bird"){
        println("Es un pajaro")
    }else{
        println("No es uno de los animales esperados")
    }
}
fun ifBasico(){
    val name = "Pablo"
    if(name== "Aris"){
        println("Oye la variable name es ARIS")
    }else{
        println("Este no es Aris")
    }

}