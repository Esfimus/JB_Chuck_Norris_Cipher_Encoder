package chucknorris

class Encrypted {
    private val charBinaryMap = mutableMapOf<Char, String>()

    fun putCharsToMap(line: String) {
        for (ch in line) {
            charBinaryMap[ch] = charToBinary(ch)
        }
    }

    private fun charToBinary(char: Char): String {
        val charBinary = Integer.toBinaryString(char.code)
        return String.format("%07d", charBinary.toInt())
    }

    fun display(line: String) {
        println("\nThe result:")
        for (ch in line) {
            println("$ch = ${charBinaryMap[ch]}")
        }
    }
}

fun encryption() {
    val encryptedLine = Encrypted()
    println("Input string:")
    val userInput = readln()
    encryptedLine.putCharsToMap(userInput)
    encryptedLine.display(userInput)
}

fun main() {
    encryption()
}