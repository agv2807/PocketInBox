package com.vazdautsan.pocketinbox.core.data.di

import com.vazdautsan.pocketinbox.core.data.repository.OfflineNotesRepository
import com.vazdautsan.pocketinbox.core.database.di.databaseModule
import com.vazdautsan.pocketinbox.core.domain.repository.NotesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.Clock

val dataModule = module {
    includes(databaseModule)
    single<Clock> { Clock.System }
    singleOf(::OfflineNotesRepository) { bind<NotesRepository>() }
}
