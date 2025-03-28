package com.example.tfg.components.DI


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tfg.repository.AuthRepository
import com.example.tfg.repository.AuthRepositoryImpl
import com.example.tfg.repository.UserRepository
import com.example.tfg.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Asegúrate de que esté anotada con @Module y @InstallIn
@Module
@InstallIn(SingletonComponent::class) // Instalar en el componente singleton
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository = AuthRepositoryImpl(auth, db)

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth,db: FirebaseFirestore): UserRepository {
        // Si UserRepository tiene dependencias, las inyectas aquí
        return UserRepositoryImpl(auth,db) // Ejemplo, puede ser otro constructor
    }

}