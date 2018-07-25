package app.deadmc.devnetworktool.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.helpers.XMLReaderFactory

import java.io.ByteArrayInputStream
import java.util.regex.Pattern

const val UNDEFINED = 0
const val JSON = 1
const val XML = 2
const val HTML = 3

fun getTypeOfString(value: String): Int {
    if (isJson(value))
        return JSON

    if (isXml(value))
        return JSON

    return if (isHtml(value)) HTML else UNDEFINED
}

fun isHtml(value: String): Boolean {
    val htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL)
    return htmlPattern.matcher(value).matches()
}

fun isJson(value: String): Boolean {
    try {
        JSONObject(value)
    } catch (ex: JSONException) {
        try {
            JSONArray(value)
        } catch (ex1: JSONException) {
            return false
        }

    }

    return true
}

fun isXml(value: String): Boolean {
    try {
        val parser = XMLReaderFactory.createXMLReader()
        parser.contentHandler = DefaultHandler()
        val source = InputSource(ByteArrayInputStream(value.toByteArray()))
        parser.parse(source)
    } catch (e: Exception) {
        return false
    }

    return true
}

