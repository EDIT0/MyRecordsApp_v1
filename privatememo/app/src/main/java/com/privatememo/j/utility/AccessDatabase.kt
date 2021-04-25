package com.privatememo.j.utility

import android.app.Activity
import androidx.room.Room
import com.privatememo.j.database.db.DBMemberSetting

class AccessDatabase {
    companion object {
        private var instance: AccessDatabase? = null

        @JvmStatic
        fun getInstance(): AccessDatabase = instance ?: synchronized(this){
            instance ?: AccessDatabase().also {
                instance = it
            }
        }
    }

    fun MemberSetting(activity: Activity): DBMemberSetting {
        return Room.databaseBuilder(activity, DBMemberSetting::class.java, "MemberSetting")
            .allowMainThreadQueries().build()
    }
}