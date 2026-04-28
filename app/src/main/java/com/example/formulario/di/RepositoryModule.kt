package com.example.formulario.di

import com.example.formulario.data.repository.FormRepositoryImpl
import com.example.formulario.domain.repository.IFormRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindFormRepository(
        formRepositoryImpl: FormRepositoryImpl
    ): IFormRepository
}
