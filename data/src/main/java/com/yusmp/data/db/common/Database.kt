package com.yusmp.data.db.common

import android.content.Context
import androidx.room.Room

public object Database {
    public fun build(
        context: Context,
        factory: DatabaseSupportFactoryProvider
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    )
        .openHelperFactory(factory.provideSupportFactory())
        .build()
}
