package com.nikolam.audiobookmate.di

import com.nikolam.book_overview.misc.NavManager
import org.koin.dsl.module

val navigationModule  = module {
    single{ NavManager() }
}