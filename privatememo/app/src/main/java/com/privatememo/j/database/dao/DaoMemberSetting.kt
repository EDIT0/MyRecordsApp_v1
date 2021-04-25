package com.privatememo.j.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.privatememo.j.database.table.EntityMemberSetting

@Dao
interface DaoMemberSetting {
    @Insert
    fun insert(member: EntityMemberSetting)

    /*@Query("DELETE FROM User WHERE name = :NAME")
    fun deleteUsingNAME(NAME: String)*/

    @Query("UPDATE EntityMemberSetting SET TitleTextSize = :title, ContentTextSize = :content, Font = :font WHERE Email = :email")
    fun updateMemberSetting(title: Int, content: Int, font: String, email: String)
    /*var Email: String,
    var TitleTextSize: Int,
    var ContentTextSize: Int,
    var Font: String*/

    @Query("SELECT Email FROM EntityMemberSetting WHERE Email = :email")
    fun checkEmail(email: String): String

    @Query("SELECT * FROM EntityMemberSetting WHERE Email = :email")
    fun getEmailData(email: String): List<EntityMemberSetting>

    @Query("SELECT * FROM EntityMemberSetting")
    fun getAll(): List<EntityMemberSetting>
}