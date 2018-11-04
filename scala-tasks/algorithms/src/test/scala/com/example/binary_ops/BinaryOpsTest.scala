import org.scalatest._
import com.example.binary_ops.BinaryOps

class BinaryOpsTest extends FlatSpec {

  it should "find value of fifth bit" in {
    assert(BinaryOps.findBitByIndex(32, 4) == 0)
  }

  it should "find bits from 1 to 3 indexes in given value" in {
    assert(BinaryOps.findBitsByRange(42, 1, 3) == 5)
  }

  it should "replace bits from 1 to 3 indexes in given value" in {
    assert(BinaryOps.replaceBitsByRange(43, 7, 1, 3) == 47)
  }

  it should "convert given number to 2-base system" in {
    assert(BinaryOps.toBinary(9) == "1001")
  }

  it should "convert given number passed in binary from to 10-base system" in {
    assert(BinaryOps.fromBinary("1001") == 9)
  }

  it should "convert given negative number to binary form with bit capacity equal to 4" in {
    assert(BinaryOps.toBinaryWithNegativeValues(-7, 4) == "1001")
  }

  it should "convert given positive number to binary form with bit capacity equal to 4" in {
    assert(BinaryOps.toBinaryWithNegativeValues(7, 4) == "0111")
  }

  it should "represent given float number in binary format according to IEEE754 specification" in {
    assert(BinaryOps.toIEEE754BinaryFormat(8.32f) == "0 10000010 00001010001111010111000")
  }

  it should "convert given float number passed in binary IEEE754 format back to 10-base form" in {
    assert(BinaryOps.fromIEEE754BinaryFormat("0 10000010 00001010001111010111000") == 8.32f)
  }

  it should "validate that converting values from 10-base to 2-base systems and back returns the same numbers " +
    "as official implementation would do" in {

    convertValueTest(23.125f)
    convertValueTest(-4.87f)
    convertValueTest(0.14f)
    convertValueTest(-0.23f)
    convertValueTest(100000000000.23f)
    convertValueTest(10000000000.23231f)
    convertValueTest(8000000000.384f)
    convertValueTest(0.00030f)
  }

  private def convertValueTest(value: Float) = {
    val binValue = BinaryOps.toIEEE754BinaryFormat(value)
    val restoredDecValue = BinaryOps.fromIEEE754BinaryFormat(binValue)
    assert(value == restoredDecValue)
  }
}