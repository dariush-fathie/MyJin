package ir.paad.audiobook.utils

object StringUtil {

    fun removeSpaces(string: String): String {
        return string.replace("\\s".toRegex(), "")
    }

}
