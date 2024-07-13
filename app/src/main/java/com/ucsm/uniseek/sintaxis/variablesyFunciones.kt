package com.ucsm.uniseek.sintaxis
//Borrar este archivo es de aprendizaje

val  age:Int = 30

fun main () {
  showMyName()
  showMyAge( currentAge = 70)
  add(30,90)
  val mySubtract = subtract(10,7)
  println (mySubtract)
}
fun showMyName(){
  println("Me llamo Aristemo")
}
fun showMyAge(currentAge:Int){
  println("Tengo $currentAge años")
}
fun add(firstNumber:Int, secondNumber: Int){
  println(firstNumber + secondNumber)
}
fun subtract(firstNumber:Int, secondNumber: Int):Int {
  return firstNumber - secondNumber
}

fun variableAlfanumerica(){
  //char
  val charExample:Char  = 'r'
  val charExample2:Char  = '@'

  //String
  val stringExample: String = "Aristemo"
  val stringExample2: String = "34"
  val stringExample3 = "3"
  val stringExample4 = "87"

  var stringConcatenada:String = "hola"
  println(stringConcatenada)
  stringConcatenada = "Hola me llamo $stringExample y tengo $age años"
  println(stringConcatenada)

  val example123:String = age.toString()
}
fun variablesBoolean(){
  //boolean
  val booleanExample:Boolean = true
  val booleanExample2:Boolean = false
}
fun variablesNumericas(){
  //Int
  var  age2:Int = 35
  age2 = 5

  //Long
  val example:Long = 70

  //Float
  val floatExample:Float = 5.3f

  //Double

  val doubleExample:Double = 5.876

   println ("Sumar:")

   println (age + age2)

   println ("Restar:")

   println (age - age2)

   println ("Multiplicar:")

   println (age * age2)

   println ("Dividir:")

   println (age / age2)

   println ("Modulo:")

   println (age % age2)

  var exampleSuma: Int = age + floatExample.toInt()
}