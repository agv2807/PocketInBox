package com.vazdautsan.pocketinbox.feature.notes.di

import com.vazdautsan.pocketinbox.feature.notes.NotesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notesModule = module {
    viewModelOf(::NotesViewModel)
}
