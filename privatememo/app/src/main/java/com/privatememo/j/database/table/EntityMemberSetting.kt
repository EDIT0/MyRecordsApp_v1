package com.privatememo.j.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityMemberSetting(
    var Email: String,
    var TitleTextSize: Int,
    var ContentTextSize: Int,
    var Font: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}