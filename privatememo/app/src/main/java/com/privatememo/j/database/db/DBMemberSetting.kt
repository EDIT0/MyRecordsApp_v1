package com.privatememo.j.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.privatememo.j.database.dao.DaoMemberSetting
import com.privatememo.j.database.table.EntityMemberSetting

@Database(entities = [EntityMemberSetting::class], version = 1)
abstract class DBMemberSetting : RoomDatabase() {
    abstract fun DaoMemberSetting(): DaoMemberSetting
}