package app.deadmc.devnetworktool.models

class ResponseDev {

    var headers: String = ""
    var body: String = ""
    var code: Int = 0
    var delay: Int = 0

    constructor(headers: String, body: String, code: Int, delay: Int) {
        this.headers = headers
        this.body = body
        this.code = code
        this.delay = delay
    }


}
