# Roman Numerals Converter

[![Build Status](https://travis-ci.org/spiechu/romannumeralsconverter.svg?branch=master)](https://travis-ci.org/spiechu/romannumeralsconverter)
[![Coverage Status](https://coveralls.io/repos/spiechu/romannumeralsconverter/badge.png?branch=master)](https://coveralls.io/r/spiechu/romannumeralsconverter?branch=master)

This is small, one-day project to test some Scala stuff.

## Overview

Converter provides easy way to transform Arabic numbers to Roman numerals and vice versa.

## Usage

Basically it is enough that you instantiate one of `ConvertableNum` case classes and execute `convert`.

```scala
val arabic = ArabicNum(2014)
val roman = arabic.convert 
roman == RomanNum("MMXIV") // true
```