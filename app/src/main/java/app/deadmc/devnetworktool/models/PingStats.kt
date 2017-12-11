package app.deadmc.devnetworktool.models

class PingStats {

    var sended: Int = 0
        private set
    var successful: Int = 0
        private set
    var failed: Int = 0
        private set
    var ttl: Int = 0

    var max: Float = 0.toFloat()
        private set
    var min: Float = 0.toFloat()
        private set
    var average: Float = 0.toFloat()
        get() = Math.round(field * 10) / 10f

    private var all: Float = 0.toFloat()

    private val url: String? = null
    var ipAddress: String? = null

    init {
        sended = 0
        successful = 0
        failed = 0
        ttl = 0
        max = 0f
        min = 0f
        average = 0f
        all = 0f
        ipAddress = ""
    }

    fun addPing(time: Float) {
        sended++
        if (time > 0.1f) {
            successful++

            if (min == 0f)
                min = time

            if (time < min)
                min = time
            if (time > max)
                max = time
            all += time
            average = all / sended
        } else {
            failed++
        }
    }

}
