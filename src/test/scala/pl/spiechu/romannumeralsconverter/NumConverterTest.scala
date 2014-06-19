package pl.spiechu.romannumeralsconverter

import org.scalatest.{FlatSpec, Matchers}

class NumConverterTest extends FlatSpec with Matchers {

  "NumConverter" should "take a list of integers and return appropriate roman numerals counterparts" in {
    val testData = List(
      3 -> "III",
      4 -> "IV",
      5 -> "V",
      9 -> "IX",
      23 -> "XXIII",
      18 -> "XVIII",
      1904 -> "MCMIV",
      1954 -> "MCMLIV",
      1990 -> "MCMXC",
      2014 -> "MMXIV"
    )

    for ((arabic, roman) <- testData) {
      val arabicObj = ArabicNum(arabic)
      val romanObj = RomanNum(roman)

      NumConverter convert arabicObj should be(romanObj)
    }
  }

  it should "take a list of numerals and return appropriate arabic ints" in {
    val testData = List(
      "III" -> 3,
      "IV" -> 4,
      "V" -> 5,
      "XXIII" -> 23,
      "XVIII" -> 18,
      "MCMIV" -> 1904,
      "MCMLIV" -> 1954,
      "MCMXC" -> 1990,
      "MMXIV" -> 2014
    )

    for ((roman, arabic) <- testData) {
      val romanObj = RomanNum(roman)
      val arabicObj = ArabicNum(arabic)

      NumConverter convert romanObj should be(arabicObj)
    }
  }

  it should "throw IllegalArgumentException when feeded with chars outside of the numeral table" in {
    val testData = List(
      "IA" -> "A",
      "Vb" -> "B",
      "E" -> "E",
      "WWXXIII" -> "W",
      "INI" -> "N"
    )

    for ((roman, errChar) <- testData) {
      val romanObj = RomanNum(roman)

      val ex = intercept[IllegalArgumentException] {
        NumConverter convert romanObj
      }

      // check if illegal char was displayed in exception msg
      ex.getMessage should be(s"Illegal char: $errChar")
    }
  }

  it should "throw IllegalArgumentException when feeded with right chars, but in wrong order" in {
    val testData = List(
      "VX",
      "IM",
      "CDD",
      "VIX"
    )

    for (roman <- testData) {
      val romanObj = RomanNum(roman)

      intercept[IllegalArgumentException] {
        NumConverter convert romanObj
      }
    }
  }

  it should "throw IllegalArgumentException when feeded with multiple two-letter numerals" in {
    val testData = List(
      "IVIVIV",
      "XLXL",
      "CMCM",
      "IXIX"
    )

    for (roman <- testData) {
      val romanObj = RomanNum(roman)

      intercept[IllegalArgumentException] {
        NumConverter convert romanObj
      }
    }
  }

  "ArabicNum" should "be able to return RomanNum counterpart directly by convert function" in {
    val romanNum = ArabicNum(1).convert
    romanNum should be(RomanNum("I"))
  }

  "RomanNum" should "be able to return ArabicNum counterpart directly by convert function" in {
    val arabicNum = RomanNum("V").convert
    arabicNum should be(ArabicNum(5))
  }
}
