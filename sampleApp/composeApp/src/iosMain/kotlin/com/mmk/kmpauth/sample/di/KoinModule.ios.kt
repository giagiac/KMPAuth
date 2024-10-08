package it.hypernext.modacenter.fidelity.di

import it.hypernext.modacenter.fidelity.api.InsultCensorClient
import it.hypernext.modacenter.fidelity.api.createHttpClient
import it.hypernext.modacenter.fidelity.database.getDatabaseBuilder
import org.koin.dsl.module

actual val targetModule = module {
    single { getDatabaseBuilder() }
    single { InsultCensorClient(httpClient = createHttpClient()) }
}