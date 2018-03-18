package app.deadmc.devnetworktool.models

data class ResponseDev(val headers: String = "", val body: String = "", val code: Int = 0, val delay: Int, val currentUrl:String, val error:Throwable? = null)
