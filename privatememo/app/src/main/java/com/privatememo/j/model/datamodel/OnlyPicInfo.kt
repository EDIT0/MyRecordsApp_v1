package com.privatememo.j.model.datamodel

data class OnlyPicInfo(
    var result: ArrayList<OnlyPicInfo2>
) {
    data class OnlyPicInfo2(
            var contentnum: Int,
            var title: String,
            var memo: String,
            var date: String,
            var revicedate: String,
            var time: String,
            var revicetime: String,
            var ConBookmark: String,
            var category_catenum: Int,
            var imagePath: String
    ) {
    }
}