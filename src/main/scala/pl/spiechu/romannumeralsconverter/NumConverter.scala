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

    val strNumTable = convertToCharSeq(romanNum.get)

    if (!validateRomanStrSeq(strNumTable)) {
      throw new IllegalArgumentException("Illegal char sequence")
    }

    for (char <- strNumTable) {
      arabicResult += flippedNumConversions.get(char).get
    }

    ArabicNum(arabicResult)
  }

  private def convertToCharSeq(str: String): List[String] = {
    val listBuffer = mutable.ListBuffer[String]()

    val romanNumStr = str.toUpperCase
    val arrayed = romanNumStr.toCharArray

    var currIdx = 0
    val arrMaxIndex = arrayed.length - 1

    while (currIdx <= arrMaxIndex) {
      breakable {
        if (currIdx + 1 <= arrMaxIndex) {
          val keyCandidate = arrayed(currIdx).toString + arrayed(currIdx + 1).toString
          if (flippedNumConversions.contains(keyCandidate)) {
            listBuffer.append(keyCandidate)
            currIdx = currIdx + 2
            break()
          }
        }

        val keyCandidate = arrayed(currIdx).toString
        if (flippedNumConversions.contains(keyCandidate)) {
          listBuffer.append(keyCandidate)
          currIdx = currIdx + 1
        }
        else {
          throw new IllegalArgumentException(s"Illegal char: $keyCandidate")
        }
      }
    }

    listBuffer.toList
  }

  private def validateRomanStrSeq(sequence: List[String]): Boolean = {
    var previousVar: Option[Int] = None
    var ocurrencies = 0

    for (char <- sequence) {
      if (!flippedNumConversions.contains(char)) {
        throw new IllegalArgumentException(s"Illegal char: $char")
      }

      val numChar = flippedNumConversions.get(char).get

      if (previousVar.isEmpty) {
        ocurrencies += 1
        previousVar = Option(numChar)
      }
      else {
        if (previousVar.get < numChar) {
          return false
        }
        else if (previousVar.get == numChar) {
          ocurrencies += 1
          if (ocurrencies > 3) {
            return false
          }

          val normalizedPrev = normalizeInt(previousVar.get)
          val normalizedChar = normalizeInt(numChar)
          if ((normalizedPrev % 4 == 0 && normalizedChar % 4 == 0) || (normalizedPrev % 9 == 0 && normalizedChar % 9 == 0)) {
            return false
          }
        }
        else {
          ocurrencies = 0
        }
        previousVar = Option(numChar)
      }
    }

    true
  }

  private def normalizeInt(num: Int): Int = {
    var normalized = num
    while (normalized % 10 == 0) {
      normalized = normalized / 10
    }

    normalized
  }
}
