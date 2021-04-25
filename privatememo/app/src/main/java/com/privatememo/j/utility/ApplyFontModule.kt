package com.privatememo.j.utility

import com.privatememo.j.R

class ApplyFontModule {

    object a {
        fun FontCall(): Int{
            if(MemberSettingModule.Font == "기본체"){
                return R.style.custom1
            }
            else if(MemberSettingModule.Font == "휴먼편지체"){
                return R.style.custom2
            }
            else if(MemberSettingModule.Font == "나눔바른고딕체"){
                return R.style.custom3
            }
            else if(MemberSettingModule.Font == "맑은 고딕체"){
                return R.style.custom4
            }
            return R.style.custom1
        }

    }

}