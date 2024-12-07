package com.umutsaydam.zenfocus.util

object FileNameFromUrl {
    fun getFileNameFromUrl(fileUrl: String): String{
        return fileUrl.split("/").last()
    }
}