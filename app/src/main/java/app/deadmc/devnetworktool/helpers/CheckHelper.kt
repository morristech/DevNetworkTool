package app.deadmc.devnetworktool.helpers

fun isValidIp(ip: String?): Boolean {
    try {
        if (ip == null || ip.isEmpty()) {
            return false
        }

        val parts = ip.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 4) {
            return false
        }

        for (s in parts) {
            val i = Integer.parseInt(s)
            if (i < 0 || i > 255) {
                return false
            }
        }
        return !ip.endsWith(".")
    } catch (nfe: NumberFormatException) {
        return false
    }

}

fun isValidPort(port: Int): Boolean = port in 0..65536

fun portFromString(portString: String): Int {
    var port = 80
    if (!portString.isEmpty()) {
        safe {
            port = Integer.parseInt(portString)
        }
    }
    return port
}


