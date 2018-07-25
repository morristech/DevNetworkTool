package app.deadmc.devnetworktool.utils

import java.util.ArrayList
import java.util.TreeMap

/**
 * Class which contains list of http headers.
 * You can get headers as public static string values
 * You can get headers from hashmap<String></String>,ArrayList<String>> where:
 * key parameter String = Header Name
 * value parameter ArrayList<String> is List which contains possible values for this parameter
</String></String> */

object AllHeaders {

    val ACCEPT = "Accept"
    val ACCEPT_CHARSET = "Accept-Charset"
    val ACCEPT_ENCODING = "Accept-Encoding"
    val ACCEPT_LANGUAGE = "Accept-Language"
    val FROM = "From"
    val PRAGMA = "Pragma"
    val REFERER = "Referer"
    val USER_AGENT = "User-Agent"

    /**
     * You can get headers from  this Hashmap<String></String>,ArrayList<String>> where:
     * key parameter String is Header`s name
     * value parameter ArrayList<String> is a List which contains possible values for this parameter
     * @return Hashmap<String></String>,ArrayList<String>>
    </String></String></String> */

    val headersHashmap: TreeMap<String, ArrayList<String>>

    init {
        headersHashmap = TreeMap()
        addAccept()
        addAcceptCharset()
        addAcceptEncoding()
        addAcceptLanguage()
        addFrom()
        addPragma()
        addReferer()
        addUserAgent()
    }

    private fun addAccept() {
        val arrayList = ArrayList<String>()
        arrayList.add("text/html")
        arrayList.add("text/plain")
        arrayList.add("image/*")
        arrayList.add("application/xml")
        arrayList.add("application/xhtml+xml")
        headersHashmap.put(ACCEPT, arrayList)
    }

    private fun addAcceptCharset() {
        val arrayList = ArrayList<String>()
        arrayList.add("utf-8")
        arrayList.add("iso-8859-1")
        headersHashmap.put(ACCEPT_CHARSET, arrayList)
    }

    private fun addAcceptEncoding() {
        val arrayList = ArrayList<String>()
        arrayList.add("gzip")
        arrayList.add("compress")
        arrayList.add("deflate")
        arrayList.add("br")
        arrayList.add("identity")
        arrayList.add("*")
        headersHashmap.put(ACCEPT_ENCODING, arrayList)
    }

    private fun addAcceptLanguage() {
        val arrayList = ArrayList<String>()
        arrayList.add("en")
        arrayList.add("en-US")
        arrayList.add("en-BR")
        arrayList.add("ru")
        arrayList.add("ru-RU")
        arrayList.add("id")
        arrayList.add("in")
        arrayList.add("de")
        arrayList.add("de-CH")
        arrayList.add("fr")
        arrayList.add("es")
        arrayList.add("*")
        headersHashmap.put(ACCEPT_LANGUAGE, arrayList)
    }

    private fun addFrom() {
        val arrayList = ArrayList<String>()
        arrayList.add("client")
        headersHashmap.put(FROM, arrayList)
    }

    private fun addPragma() {
        val arrayList = ArrayList<String>()
        arrayList.add("no-cache")
        headersHashmap.put(PRAGMA, arrayList)
    }

    private fun addReferer() {
        val arrayList = ArrayList<String>()
        arrayList.add("http://www.w3.org")
        headersHashmap.put(REFERER, arrayList)
    }

    private fun addUserAgent() {
        val arrayList = ArrayList<String>()
        arrayList.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
        arrayList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
        arrayList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
        arrayList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
        arrayList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/602.4.8 (KHTML, like Gecko) Version/10.0.3 Safari/602.4.8")
        arrayList.add("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0")
        headersHashmap.put(USER_AGENT, arrayList)
    }


}
