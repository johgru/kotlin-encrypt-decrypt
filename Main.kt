package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {
    val argsMap = args.toList().chunked(2).associate { it[0] to it[1] }

    val mode = argsMap.getOrDefault("-mode", "enc")
    val algo = argsMap.getOrDefault("-alg", "shift")
    val fileIn = argsMap.getOrDefault("-in", "")
    val fileOut = argsMap.getOrDefault("-out", "")
    val key = argsMap.getOrDefault("-key", "0").toInt()
    val data = argsMap.getOrDefault("-data", "")

    if (data.isEmpty() && fileIn.isEmpty()) {
        when (mode) {
            "enc" -> for (i in data) print(encryptChar(i, key, algo))
            "dec" -> for (i in data) print(decryptChar(i, key, algo))
        }
    } else if (data.isEmpty()) {
        when (mode) {
            "enc" -> encryptFile(fileIn, key, fileOut, algo)
            "dec" -> decryptFile(fileIn, key, fileOut, algo)
        }
    } else {
        when (mode) {
            "enc" -> for (i in data) print(encryptChar(i, key, algo))
            "dec" -> for (i in data) print(decryptChar(i, key, algo))
        }
    }
}

fun encryptFile(fileName: String?, key: Int, fileNameOut: String?, algo: String) {
    val inputMessage = File(fileName!!).readText()
    val encryptedMessage = mutableListOf<String>()

    for (i in inputMessage) encryptedMessage.add(encryptChar(i, key, algo).toString())

    val myFile = File(fileNameOut!!)
    myFile.writeText(encryptedMessage.joinToString(""))
}

fun decryptFile(fileName: String?, key: Int, fileNameOut: String?, algo: String) {
    val inputMessage = File(fileName!!).readText()
    val decryptedMessage = mutableListOf<String>()

    for (i in inputMessage) decryptedMessage.add(decryptChar(i, key, algo).toString())

    val myFile = File(fileNameOut!!)
    myFile.writeText(decryptedMessage.joinToString(""))
}

fun encryptChar(letter: Char, key: Int, algo: String): Char {
    if (algo == "shift") {
        return when (letter.code) {
            in 65..90 -> {
                ((letter.code + key - 65) % 26 + 65).toChar()
            }
            in 97..122 -> {
                ((letter.code + key - 97) % 26 + 97).toChar()
            }
            else -> letter
        }
    }
    return (letter.code + key).toChar()
}

fun decryptChar(letter: Char, key: Int, algo: String): Char {
    if (algo == "shift") {
        if (letter.isLetter()) {
            println("LETTERCODE: ${ letter.code } - $key")
            if (letter.code - key < 'a'.code) return (122 - (97 % (letter.code - key)) + 1).toChar()
            if (letter.code - key >= 'a'.code) return (letter.code - key).toChar()
        } else return letter
    }
    return (letter.code - key).toChar()
}
