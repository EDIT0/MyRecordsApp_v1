package com.privatememo.j.model.datamodel

data class MemoImageInfo(
    var result: ArrayList<MemoImageInfo2>
) {
    data class MemoImageInfo2(
            var imagePath: String
    ){

    }
}