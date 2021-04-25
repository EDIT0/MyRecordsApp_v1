package com.privatememo.j.datamodel

data class MemoImageInfo(
    var result: ArrayList<MemoImageInfo2>
) {
    data class MemoImageInfo2(
            var imagePath: String
    ){

    }
}