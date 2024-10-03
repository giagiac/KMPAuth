package it.hypernext.modacenter.fidelity.di

import it.hypernext.modacenter.fidelity.database.getDatabaseBuilder
import org.koin.dsl.module

actual val targetModule = module {
    single { getDatabaseBuilder() }
}