package app.deadmc.devnetworktool.helpers

import android.text.Html
import android.text.Spanned

fun formatString(text: String): String {

    val json = StringBuilder()
    var indentString = ""

    for (i in 0 until text.length) {
        val letter = text[i]
        when (letter) {
            '{', '[' -> {
                json.append("\n" + indentString + letter + "\n")
                indentString += "\t"
                json.append(indentString)
            }
            '}', ']' -> {
                indentString = indentString.replaceFirst("\t".toRegex(), "")
                json.append("\n" + indentString + letter)
            }
            ',' -> json.append(letter + "\n" + indentString)

            else -> json.append(letter)
        }
    }

    return json.toString()
}

fun getPingFromString(rawStringArg: String): Float {
    var rawString = rawStringArg
    rawString = rawString.trim { it <= ' ' }.replace(" ", "")
    val index = rawString.lastIndexOf("time=")
    val lastIndex = rawString.lastIndexOf("ms---")
    var value = 0f
    try {
        val result = rawString.substring(index + 5, lastIndex)
        value = java.lang.Float.parseFloat(result)
    } catch (e: Exception) {

    }

    return value

}

fun getTtlFromString(rawStringArg: String): Int {
    var rawString = rawStringArg
    rawString = rawString.trim { it <= ' ' }.replace(" ", "")
    val index = rawString.lastIndexOf("ttl=")
    val lastIndex = rawString.lastIndexOf("time=")
    var value = 0
    //Log.e("ping1", pingString);
    //Log.e("ping2", "lastIndex ="+ lastIndex);
    try {
        val result = rawString.substring(index + 4, lastIndex)
        value = Integer.parseInt(result)
    } catch (e: Exception) {

    }

    return value

}

fun getIpAddressFromString(rawStringArg: String): String {
    var rawString = rawStringArg
    rawString = rawString.trim { it <= ' ' }.replace(" ", "")
    val index = rawString.indexOf("(")
    val lastIndex = rawString.indexOf(")")
    //Log.e("ping1", pingString);
    //Log.e("ping2", "lastIndex ="+ lastIndex);
    var result = ""
    try {
        result = rawString.substring(index + 1, lastIndex)
    } catch (e: Exception) {

    }

    return result

}

@SuppressWarnings("deprecation")
fun fromHtml(html: String): Spanned {
    val result: Spanned
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        result = Html.fromHtml(html)
    }
    return result
}

fun String.toCapitalizedFirstLetterString():String {
    return this.substring(0, 1).toUpperCase() + this.substring(1)
}
