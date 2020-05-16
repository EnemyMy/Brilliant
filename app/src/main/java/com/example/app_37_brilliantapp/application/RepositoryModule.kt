package com.example.app_37_brilliantapp.application

import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondRepository
import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondRepositoryImpl
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsRepository
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsRepositoryImpl
import com.example.app_37_brilliantapp.finddiamond.FindDiamondRepository
import com.example.app_37_brilliantapp.finddiamond.FindDiamondRepositoryImpl
import com.example.app_37_brilliantapp.getdiamond.GetDiamondRepository
import com.example.app_37_brilliantapp.getdiamond.GetDiamondRepositoryImpl
import com.example.app_37_brilliantapp.ideas.IdeasRepository
import com.example.app_37_brilliantapp.ideas.IdeasRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCurrentDiamondRepository(repository: CurrentDiamondRepositoryImpl): CurrentDiamondRepository

    @Binds
    @Singleton
    abstract fun bindEarnedDiamondsRepository(repository: EarnedDiamondsRepositoryImpl): EarnedDiamondsRepository

    @Binds
    @Singleton
    abstract fun bindFindDiamondRepository(repository: FindDiamondRepositoryImpl): FindDiamondRepository

    @Binds
    @Singleton
    abstract fun bindGetDiamondRepository(repository: GetDiamondRepositoryImpl): GetDiamondRepository

    @Binds
    @Singleton
    abstract fun bindIdeasRepository(repository: IdeasRepositoryImpl): IdeasRepository

}