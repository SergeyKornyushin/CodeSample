package com.yusmp.data.db.common

import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Inject


interface DatabaseSupportFactoryProvider {
    fun provideSupportFactory(): SupportFactory
}

class DefaultDatabaseSupportFactoryProvider @Inject constructor(context: Context) :
    DatabaseSupportFactoryProvider {

    override fun provideSupportFactory(): SupportFactory {
        val passphrase =
            SQLiteDatabase.getBytes("room_secure_unilab".toCharArray())//TODO change for project
        return SupportFactory(passphrase)
    }
}