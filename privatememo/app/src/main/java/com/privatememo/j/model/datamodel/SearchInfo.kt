package com.privatememo.j.model.datamodel

data class SearchInfo(
    var result: ArrayList<SearchInfo2>
) {
    data class SearchInfo2(
            var contentnum: Int,
            var title: String,
            var memo: String,
            var date: String,
            var revicedate: String,
            var time: String,
            var revicetime: String,
            var ConBookmark: String,
            var memberlist_email: String,
            var category_catenum: Int,
            var catename: String
    ) {

        fun printDate(): String{
            var re_date = date.split("_")
            return re_date[0] + "." + re_date[1] + "." + re_date[2]
        }

        fun printReViseDate(): String{
            if(revicedate.length > 3) {
                var re_revisedate = revicedate.split("_")
                return re_revisedate[0] + "." + re_revisedate[1] + "." + re_revisedate[2]
            }
            return ""

        }

        fun printTime(): String{
            var re_time = time.split("_")
            return re_time[0] + ":" + re_time[1] + ":" + re_time[2]
        }

        fun printReViseTime(): String{
            if(revicetime.length > 3) {
                var re_revisetime = revicetime.split("_")
                return re_revisetime[0] + ":" + re_revisetime[1] + ":" + re_revisetime[2]
            }
            return ""
        }

        fun setVisible(): Boolean{
            if(revicedate.length > 3){
                return true
            }
            else{
                return false
            }

        }
    }

}