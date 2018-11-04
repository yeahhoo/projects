package com.example.binary_ops

/**
	* Contains functions to operate on bits. Inspired by:
	* http://www.oxfordmathcenter.com/drupal7/node/43
	* https://www.h-schmidt.net/FloatConverter/IEEE754.html
	**/
object BinaryOps {

	/** Finds value of bit in given number by index. Index is 0 based */
	def findBitByIndex(value: Int, index: Int): Int = {
		(value >> index) & 1
	}

	/** Finds range of bits in given number by start and end indexes. Start and end are 0 based */
	def findBitsByRange(value: Int, start: Int, end: Int): Int = {
		val startedDropped = value >> start
		val mask = (~0 & (1 << (end - start + 1))) - 1
		startedDropped & mask
	}

	/** Replaces range of bits in given number with specified bits. Start and end are 0 based */
	def replaceBitsByRange(value: Int, replacement: Int, start: Int, end: Int): Int = {
		val max = ~0
		val leftPart = (((1 << start) - 1) & max) & value
		val maxRightOffset = max - ((1 << end + 1) - 1)
		val rightWithAddedReplacement = (value & maxRightOffset) | (replacement << start)
		return rightWithAddedReplacement | leftPart
	}

	/** Converts given number from 10-base to 2-base system. */
	def toBinary(n: Long): String = {
		n match {
			case 0 => "0"
			case 1 => "1"
			case _ => toBinary(n / 2) + (n % 2).toString
		}
	}

	/** Converts given number from 2-base to 10-base system. */
	def fromBinary(s: String): Long = {
		import scala.math.pow
		s match {
			case "0" => 0
			case "1" => 1
			case _ => (Integer.parseInt(s(0).toString) * pow(2, s.length - 1)).toLong + fromBinary(s.substring(1))
		}
	}

	/** Converts positive and negative decimal numbers to binary format with given capacity. */
	def toBinaryWithNegativeValues(n: Int, capacity: Int): String = {
		def _plusOne(v: String): String = {
			if (v == "") return ""
			else if (v(v.length - 1) == '0') return v.substring(0, v.length - 1) + "1"
			else return _plusOne(v.substring(0, v.length - 1)) + "0"
		}

		if (n >= 0) return List.fill(capacity - toBinary(n).length)(0).mkString + toBinary(n)
		val numberOfFreeBits = capacity - toBinary(-1 * n).length
		val binPositiveValue = List.fill(numberOfFreeBits)(0).mkString + toBinary(-1 * n)
		val revertedValue = binPositiveValue.map(i => if (i == '0') "1" else "0").mkString
		_plusOne(revertedValue)
	}

	/** Represents given float number in binary format according to IEEE754 standard. */
	def toIEEE754BinaryFormat(value: Float): String = {

		def _findMantissa(value: Double, roundLimit: Int): String = {
			if (roundLimit == 0) return ""
			value match {
				case 1.0 => return "1"
				case _ => {
					val nextValue = value * 2
					if (nextValue == 1) "1"
					else if (nextValue > 1) "1" + _findMantissa(nextValue - 1, roundLimit - 1)
					else "0" + _findMantissa(nextValue, roundLimit - 1)
				}
			}
		}

		def _formalizeValue(value: Float, shift: Int = 0): (Float, Int) = {
			if (value >= 1 && value < 2) return (value, shift)
			if (value > 1) return _formalizeValue(value / 2, shift + 1)
			else _formalizeValue(value * 2, shift - 1)
		}

		val sign = if (value < 0) "1" else "0"
		val formalizedValue = _formalizeValue(scala.math.abs(value))

		val initCapacity = 127
		val exponentBinary = toBinaryWithNegativeValues(initCapacity + formalizedValue._2, 8)

		val partMan = _findMantissa(formalizedValue._1 - 1, 23)
		val mantissaBinary = partMan + List.fill(23 - partMan.length)(0).mkString
		return sign + " " + exponentBinary + " " + mantissaBinary
	}

	/** Converts given number presented in binary format back to float according to IEEE754 standard. */
	def fromIEEE754BinaryFormat(binStr: String): Float = {
		val value = binStr.replaceAll("\\s", "")
		val sign = if (value(0) == '0') 1 else -1
		val expStr = value.substring(1, 9)
		val manStr = value.substring(9, 32)
		val bias = (fromBinary(expStr) - 127).toInt
		val decValue: Float = manStr.view.zipWithIndex.toList.foldLeft(0f)((a, p) => {
			val m = if (p._1 == '1') 1 else 0
			a + m * scala.math.pow(2, -1 * p._2.toInt - 1).toFloat
		})
		return sign * (1 + decValue) * scala.math.pow(2, bias).toFloat
	}
}
