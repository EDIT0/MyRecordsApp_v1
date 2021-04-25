package com.privatememo.j.datamodel

class MemberProfileInfo(
    var result: ArrayList<MemberProfileInfo2>
) {
    data class MemberProfileInfo2(
            var email: String,
            var pw: String,
            var nickname: String,
            var motto: String,
            var picPath: String
    ){}
}