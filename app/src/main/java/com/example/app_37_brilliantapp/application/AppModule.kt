package com.example.app_37_brilliantapp.application

import com.example.app_37_brilliantapp.data.curentdiamond.CurrentDiamondDataSource
import com.example.app_37_brilliantapp.data.curentdiamond.FirebaseCurrentDiamondDataSource
import com.example.app_37_brilliantapp.data.earneddiamonds.EarnedDiamondsDataSource
import com.example.app_37_brilliantapp.data.earneddiamonds.FirebaseEarnedDiamondsDataSource
import com.example.app_37_brilliantapp.data.ideas.FirebaseIdeasDataSource
import com.example.app_37_brilliantapp.data.ideas.IdeasDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindCurrentDiamondDataSource(dataSource: FirebaseCurrentDiamondDataSource): CurrentDiamondDataSource

    @Singleton
    @Binds
    abstract fun bindEarnedDiamondsDataSource(dataSource: FirebaseEarnedDiamondsDataSource): EarnedDiamondsDataSource

    @Singleton
    @Binds
    abstract fun bindIdeasDataSource(dataSource: FirebaseIdeasDataSource): IdeasDataSource

}