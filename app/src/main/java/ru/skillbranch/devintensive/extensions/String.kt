package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String{

    val trimmedMes = this.trim()
    return if (trimmedMes.length <= length) trimmedMes else trimmedMes.substring(0, length).trim() + "..."
}

fun String.stripHtml(): String{
    return this.replace("<[^<>]+>".toRegex(),"")    //Remove html tags
        .replace("\\n".toRegex(), "")
        .replace("&[\\S]+;".toRegex(),"")           //Remove html escape sequences
        .replace("[\\n&'\"><}]".toRegex(), "")      //Remove & <> '" \n
        .replace(" +".toRegex(), " ")            //Remove duplicate spaces

}