package com.ilya.platform

import app.cash.sqldelight.db.SqlDriver
import com.ilya.Database


expect class DriverFactory {

    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database{
    val driver = driverFactory.createDriver()
   return Database(driver)

}