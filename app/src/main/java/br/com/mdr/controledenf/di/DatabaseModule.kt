package br.com.mdr.controledenf.di

import androidx.room.Room
import br.com.mdr.controledenf.data.dao.UserDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            UserDatabase::class.java,
            "user_database"
        ).build()
    }
    single {
        get<UserDatabase>().getCompanyDAO()
    }
    single {
        get<UserDatabase>().getPartnerCompanyDAO()
    }
    single {
        get<UserDatabase>().getExpenseCategoryDAO()
    }
    single {
        get<UserDatabase>().getExpenseDAO()
    }
    single {
        get<UserDatabase>().getInvoiceDAO()
    }
}