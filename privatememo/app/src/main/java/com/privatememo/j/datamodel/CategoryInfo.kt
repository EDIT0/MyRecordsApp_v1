package com.privatememo.j.datamodel

data class CategoryInfo(
    var result: ArrayList<CategoryInfo2>
) {
    data class CategoryInfo2(
            var catename: String,
            var explanation: String,
            var catepicPath: String,
            var memberlist_email: String,
            var CateBookmark: String,
            var catenum: String
    ){}
}