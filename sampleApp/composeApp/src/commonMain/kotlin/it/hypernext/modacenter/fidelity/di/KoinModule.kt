package it.hypernext.modacenter.fidelity.di

import it.hypernext.modacenter.fidelity.data.getRoomDatabase
import it.hypernext.modacenter.fidelity.presentation.screen.details.DetailsViewModel
import it.hypernext.modacenter.fidelity.presentation.screen.card.CardViewModel
import it.hypernext.modacenter.fidelity.presentation.screen.login.LoginViewModel
import it.hypernext.modacenter.fidelity.presentation.screen.manage.ManageViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val targetModule: Module

val sharedModule = module {
    single { getRoomDatabase(get()) }
    viewModelOf(::LoginViewModel)
    viewModelOf(::CardViewModel)
    viewModelOf(::ManageViewModel)
    viewModelOf(::DetailsViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}