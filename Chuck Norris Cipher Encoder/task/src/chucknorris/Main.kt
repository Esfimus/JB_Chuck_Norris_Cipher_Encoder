package chucknorris

import kotlin.system.exitProcess

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

    fun displayEncryptedMessage() {
        println("Encoded string:")
        println("${zeros()}\n")
    }
}

class Decrypted(private val line: String) {
    private val binaryString: String

    private val decryptedLine: String

    init {
        this.binaryString = zerosToBinary()
        this.decryptedLine = binaryToLine()
    }

    private fun zerosToBinary(): String {
        var string = ""
        val zerosList = line.trim().split("""\s+""".toRegex())
        var isOne = false
        for (i in zerosList.indices) {
            if (i % 2 == 0) {
                isOne = zerosList[i] == "0"
            } else {
                string += if (isOne) {
                    "1".repeat(zerosList[i].length)
                } else {
                    "0".repeat(zerosList[i].length)
                }
            }
        }
        return string
    }

    private fun binaryToLine(): String {
        val binaryList = binaryString.chunked(7)
        var decryptedString = ""
        for (binSequence in binaryList) {
            decryptedString += Integer.parseInt(binSequence, 2).toChar()
        }
        return decryptedString
    }

    fun displayDecryptedMessage() {
        println("Decoded string:")
        println("$decryptedLine\n")
    }
}

/**
 * Checks input string with blocks of "0"
 */
fun encryptedMessageCheck(message: String): Boolean {
    if (!"""[\s*0+]+""".toRegex().matches(message)) {
        return false
    }
    val listBlocks = message.trim().split("""\s+""".toRegex())
    var binDigitsCount = 0
    for (i in listBlocks.indices) {
        if (i % 2 == 0) {
            if (!(listBlocks[i] == "0" || listBlocks[i] == "00")) {
                return false
            }
        } else {
            binDigitsCount += listBlocks[i].length
        }
    }
    if (listBlocks.size %2 != 0) {
        return false
    }
    if (binDigitsCount % 7 != 0) {
        return false
    }
    return true
}

fun encryption() {
    println("Input string:")
    val encryptedLine = Encrypted(readln())
    encryptedLine.displayEncryptedMessage()
}

fun decryption() {
    println("Input encoded string:")
    val userInput = readln()
    if (encryptedMessageCheck(userInput)) {
        val decryptedLine = Decrypted(userInput)
        decryptedLine.displayDecryptedMessage()
    } else {
        println("Encoded string is not valid.\n")
    }
}

fun encryptionDecryptionApp() {
    do {
        println("Please input operation (encode/decode/exit):")
        when (val userInput = readln()) {
            "encode" -> encryption()
            "decode" -> decryption()
            "exit" -> {
                println("Bye!")
                exitProcess(0)
            }
            else -> println("There is no '$userInput' operation\n")
        }
    } while (true)
}

fun main() {
    encryptionDecryptionApp()
}