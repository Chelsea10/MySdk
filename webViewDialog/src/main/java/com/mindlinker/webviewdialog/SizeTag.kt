package com.mindlinker.webviewdialog

class SizeTag : SizeLabel() {
    override fun getFilterTags(): Array<String> {
        return arrayOf("size")
    }
}