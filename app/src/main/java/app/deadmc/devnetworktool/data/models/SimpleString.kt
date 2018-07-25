package app.deadmc.devnetworktool.data.models

class SimpleString {

    var text: String
    var isBold: Boolean = false

    constructor(text: String) {
        this.text = text
    }

    constructor(text: String, bold: Boolean) {
        this.text = text
        this.isBold = bold
    }


}
