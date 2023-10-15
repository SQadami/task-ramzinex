package ir.salman.ramzinex.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.salman.ramzinex.data.usecase.FetchPairIdsUseCase
import ir.salman.ramzinex.data.usecase.OptimizeInvestmentUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoDataSourceModule {

    @Provides
    @Singleton
    fun provideFetchPairIdsUseCase(
        apiService: CryptoApiService,
        coroutineDispatcher: CoroutineDispatcher,
    ): FetchPairIdsUseCase = FetchPairIdsUseCase(apiService, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideOptimizeInvestmentUseCase(): OptimizeInvestmentUseCase = OptimizeInvestmentUseCase()

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher =
        Dispatchers.IO

    @Provides
    @Singleton
    fun provideCryptoApiService(retrofit: Retrofit): CryptoApiService =
        retrofit.create(CryptoApiService::class.java)
}