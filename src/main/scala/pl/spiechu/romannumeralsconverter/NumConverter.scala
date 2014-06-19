package pl.spiechu.romannumeralsconverter

import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

sealed abstract class ConvertableNum {
  def convert = NumConverter convert this
}

case class ArabicNum(num: Int) extends ConvertableNum {
  def get = num
}

case class RomanNum(num: String) extends ConvertableNum {
  def get = num
}

object NumConverter {

  final val numConversions = List(
    1000 -> "M",
    900 -> "CM",
    500 -> "D",
    400 -> "CD",
    100 -> "C",
    90 -> "XC",
    50 -> "L",
    40 -> "XL",
    10 -> "X",
    9 -> "IX",
    5 -> "V",
    4 -> "IV",
    1 -> "I"
  )

  final val flippedNumConversions = numConversions.map(tup => tup._2 -> tup._1).toMap

  def convert(num: ConvertableNum): ConvertableNum = {
    num match {
      case num: ArabicNum => convertArabic(num)
      case num: RomanNum => convertRoman(num)
    }
  }

  private def convertArabic(arabicNum: ArabicNum) = {
    val romanNumStr = new StringBuilder
    var aNum = arabicNum.get

    while (aNum > 0) {
      breakable {
        for ((a, r) <- numConversions if a <= aNum) {
          aNum -= a
          romanNumStr.append(r)
          break()
        }
      }
    }

    RomanNum(romanNumStr toString())
  }

  private def convertRoman(romanNum: RomanNum) = {
    var arabicResult = 0
    val romanNumStr = romanNum.get.toUpperCase
    val arrayed = romanNumStr.toCharArray

    var currIdx = 0
    val arrMaxIndex = arrayed.length - 1
    val valTable: mutable.MutableList[Int] = mutable.MutableList()

    while (currIdx <= arrMaxIndex) {
      breakable {
        if (currIdx + 1 <= arrMaxIndex) {
          val keyCandidate = arrayed(currIdx).toString + arrayed(currIdx + 1).toString
          if (flippedNumConversions.contains(keyCandidate)) {
            val partialVal = flippedNumConversions.get(keyCandidate).get
            valTable += partialVal
            arabicResult += partialVal
            currIdx = currIdx + 2
            break()
          }
        }

        val keyCandidate = arrayed(currIdx).toString
        if (flippedNumConversions.contains(keyCandidate)) {
          val partialVal = flippedNumConversions.get(keyCandidate).get
          valTable += partialVal
          arabicResult += partialVal
          currIdx = currIdx + 1
        }
        else {
          throw new IllegalArgumentException(s"Illegal char: $keyCandidate")
        }
      }
    }

    if (!validateRomanStrSeq(valTable)) {
      throw new IllegalArgumentException("Illegal char sequence")
    }

    ArabicNum(arabicResult)
  }

  private def validateRomanStrSeq(sequence: mutable.MutableList[Int]): Boolean = {
    var previousVar: Option[Int] = None

    for (char <- sequence) {
      if (previousVar.isEmpty) previousVar = Option(char)
      else {
        if (previousVar.get < char
          || (previousVar.get % 4 == 0 && char % 4 == 0)
          || (previousVar.get % 9 == 0 && char % 9 == 0)
        ) {
          return false
        }
        previousVar = Option(char)
      }
    }

    true
  }
}
