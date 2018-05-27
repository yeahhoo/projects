package com.example.binary_ops


object BinaryOps {

  // index is 1 based
  def findBitByIndex(value : Int, index : Int) : Int = {
    (value >> index - 1) & 1
  }

  // start and end are 1 based
  def findBitsByRange(value : Int, start : Int, end : Int) : Int = {
    val startedDropped = value >> start - 1
    val mask = ~(1 << end)
    startedDropped & mask
  }

  // start and end are 1 based
  def replaceBitsByRange(value : Int, replacement : Int, start : Int, end : Int) : Int = {
    val replBits = findBitsByRange(replacement, 1, end - start + 1)
    val right = ((1 << start - 1) - 1)
    val left = ~0 - ((1 << end) - 1)
    val mask = right | left
    (value & mask) | (replBits << start - 1)
  }

  def main(args: Array[String]): Unit = {
    val value = 15
    val replacement = 5
    val bits = replaceBitsByRange(value, replacement, 3, 5)
    println(s"value: ${value.toBinaryString}")
    println(s"replacement: ${replacement.toBinaryString}")
    println(s"result: ${bits.toBinaryString}")
  }
}
