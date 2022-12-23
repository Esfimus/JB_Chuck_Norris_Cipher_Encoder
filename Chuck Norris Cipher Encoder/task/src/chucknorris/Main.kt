package chucknorris

class Encrypted(private val line: String) {

    private val charBinaryMap: MutableMap<Char, String>

    init {
        this.charBinaryMap = putCharsToMap()
    }

    private fun putCharsToMap(): MutableMap<Char, String> {
        val map = mutableMapOf<Char, String>()
        for (ch in line) {
            map[ch] = charToBinary(ch)
        }
        return map
    }

    private fun charToBinary(char: Char): String {
        val charBinary = Integer.toBinaryString(char.code)
        return String.format("%07d", charBinary.toInt())
    }

    private fun zeroAndOneTransform(zeroOrOneSequence: String): MutableList<String> {
        val listTransform = mutableListOf<String>()
        if ("""0+""".toRegex().matches(zeroOrOneSequence)) {
            listTransform.add("00")
            listTransform.add("0".repeat(zeroOrOneSequence.length))
        } else if ("""1+""".toRegex().matches(zeroOrOneSequence)) {
            listTransform.add("0")
            listTransform.add("0".repeat(zeroOrOneSequence.length))
        }
        return listTransform
    }

    private fun zeros(): String {
        var binaryString = ""
        for (ch in line) {
            binaryString += charBinaryMap[ch]
        }
        val zeroList = mutableListOf<String>()
        var countOne = ""
        var countZero = ""
        for (ch in binaryString) {
            if (ch == '1') {
                countOne += '1'
                if (countZero.isNotEmpty()) {
                    zeroList.addAll(zeroAndOneTransform(countZero))
                    countZero = ""
                }
            } else if (ch == '0') {
                countZero += '0'
                if (countOne.isNotEmpty()) {
                    zeroList.addAll(zeroAndOneTransform(countOne))
                    countOne = ""
                }
            }
        }
        if (countZero.isNotEmpty()) {
            zeroList.addAll(zeroAndOneTransform(countZero))
        } else if (countOne.isNotEmpty()) {
            zeroList.addAll(zeroAndOneTransform(countOne))
        }
        return zeroList.joinToString(" ")
    }

    fun displayBinary() {
        println("\nThe result:")
        for (ch in line) {
            println("$ch = ${charBinaryMap[ch]}")
        }
    }

    fun displayZeros() {
        println("\nThe result:")
        println(zeros())
    }
}

fun encryption() {
    println("Input string:")
    val encryptedLine = Encrypted(readln())
    encryptedLine.displayZeros()
}

fun main() {
    encryption()
}