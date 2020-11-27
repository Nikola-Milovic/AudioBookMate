package com.nikolam.audiobookmate.di

import com.nikolam.common.NavManager
import org.koin.dsl.module

val navigationModule  = module {
    single{ com.nikolam.common.NavManager() }
}