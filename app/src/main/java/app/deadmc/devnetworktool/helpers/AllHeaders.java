package app.deadmc.devnetworktool.helpers;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class which contains list of http headers.
 * You can get headers as public static string values
 * You can get headers from hashmap<String,ArrayList<String>> where:
 * key parameter String = Header Name
 * value parameter ArrayList<String> is List which contains possible values for this parameter
 */

public class AllHeaders {

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String FROM = "From";
    public static final String PRAGMA = "Pragma";
    public static final String REFERER = "Referer";
    public static final String USER_AGENT = "User-Agent";

    private static final TreeMap<String, ArrayList<String>> hashMap;
    static {
        hashMap = new TreeMap<>();
        addAccept();
        addAcceptCharset();
        addAcceptEncoding();
        addAcceptLanguage();
        addFrom();
        addPragma();
        addReferer();
        addUserAgent();
    }

    /**
     * You can get headers from  this Hashmap<String,ArrayList<String>> where:
     * key parameter String is Header`s name
     * value parameter ArrayList<String> is a List which contains possible values for this parameter
     * @return Hashmap<String,ArrayList<String>>
     */
    public static TreeMap<String, ArrayList<String>> getHeadersHashmap() {
        return hashMap;
    }

    private static void addAccept() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("text/html");
        arrayList.add("text/plain");
        arrayList.add("image/*");
        arrayList.add("application/xml");
        arrayList.add("application/xhtml+xml");
        hashMap.put(ACCEPT,arrayList);
    }

    private static void addAcceptCharset() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("utf-8");
        arrayList.add("iso-8859-1");
        hashMap.put(ACCEPT_CHARSET,arrayList);
    }

    private static void addAcceptEncoding() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("gzip");
        arrayList.add("compress");
        arrayList.add("deflate");
        arrayList.add("br");
        arrayList.add("identity");
        arrayList.add("*");
        hashMap.put(ACCEPT_ENCODING,arrayList);
    }

    private static void addAcceptLanguage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("en");
        arrayList.add("en-US");
        arrayList.add("en-BR");
        arrayList.add("ru");
        arrayList.add("ru-RU");
        arrayList.add("id");
        arrayList.add("in");
        arrayList.add("de");
        arrayList.add("de-CH");
        arrayList.add("fr");
        arrayList.add("es");
        arrayList.add("*");
        hashMap.put(ACCEPT_LANGUAGE,arrayList);
    }

    private static void addFrom() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("client");
        hashMap.put(FROM,arrayList);
    }

    private static void addPragma() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("no-cache");
        hashMap.put(PRAGMA,arrayList);
    }

    private static void addReferer() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://www.w3.org");
        hashMap.put(REFERER,arrayList);
    }

    private static void addUserAgent() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        arrayList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        arrayList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        arrayList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        arrayList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/602.4.8 (KHTML, like Gecko) Version/10.0.3 Safari/602.4.8");
        arrayList.add("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
        hashMap.put(USER_AGENT,arrayList);
    }




}
