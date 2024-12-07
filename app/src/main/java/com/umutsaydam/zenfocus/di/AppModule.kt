package com.umutsaydam.zenfocus.di

import android.app.Application
import androidx.room.Room
import com.umutsaydam.zenfocus.data.local.TasksDao
import com.umutsaydam.zenfocus.data.local.TasksDatabase
import com.umutsaydam.zenfocus.data.local.ThemeRepositoryImpl
import com.umutsaydam.zenfocus.data.local.ToDoRepositoryImpl
import com.umutsaydam.zenfocus.data.manager.LocalUserManagerImpl
import com.umutsaydam.zenfocus.data.remote.repository.AwsAuthRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.repository.AwsStorageServiceRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.service.AwsAuthServiceImpl
import com.umutsaydam.zenfocus.data.remote.service.AwsStorageServiceImpl
import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.domain.service.AwsStorageService
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.LocalUserCases
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.DeleteUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.DeleteUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appEntryCases.ReadAppEntry
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appLangCases.ReadAppLang
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.ReadUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.ReadUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.vibrateCases.ReadVibrateState
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appEntryCases.SaveAppEntry
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.appLangCases.SaveAppLang
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.themeCases.ReadTheme
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.themeCases.SaveTheme
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.vibrateCases.SaveVibrateState
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsStorageCases
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUpConfirm
import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.DownloadSelectedTheme
import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.ReadThemeList
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.DeleteTask
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.GetTasks
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.UpsertTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserCases(
        localUserManager: LocalUserManager
    ): LocalUserCases {
        return LocalUserCases(
            saveAppEntry = SaveAppEntry(localUserManager),
            readAppEntry = ReadAppEntry(localUserManager),
            saveVibrateState = SaveVibrateState(localUserManager),
            readVibrateState = ReadVibrateState(localUserManager),
            saveAppLang = SaveAppLang(localUserManager),
            readAppLang = ReadAppLang(localUserManager),
            saveUserId = SaveUserId(localUserManager),
            readUserId = ReadUserId(localUserManager),
            deleteUserId = DeleteUserId(localUserManager),
            saveUserType = SaveUserType(localUserManager),
            readUserType = ReadUserType(localUserManager),
            deleteUserType = DeleteUserType(localUserManager),
            saveTheme = SaveTheme(localUserManager),
            readTheme = ReadTheme(localUserManager)
        )
    }

    @Provides
    @Singleton
    fun provideAwsAuthUsesCases(
        awsAuthRepository: AwsAuthRepository
    ): AwsAuthCases {
        return AwsAuthCases(
            userSignIn = AwsUserSignIn(awsAuthRepository),
            userSignUp = AwsUserSignUp(awsAuthRepository),
            userSignUpConfirm = AwsUserSignUpConfirm(awsAuthRepository),
            userGetId = AwsUserGetUserId(awsAuthRepository),
            readUserInfo = AwsReadUserInfo(awsAuthRepository),
            signOut = AwsSignOut(awsAuthRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAwsStorageUsesCases(
        awsStorageRepository: AwsStorageServiceRepository
    ): AwsStorageCases {
        return AwsStorageCases(
            readThemeList = ReadThemeList(awsStorageRepository),
            downloadSelectedThemeList = DownloadSelectedTheme(awsStorageRepository)
        )
    }

    @Provides
    @Singleton
    fun localUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideToDoUsesCases(
        toDoRepository: ToDoRepository,
        tasksDao: TasksDao
    ): ToDoUsesCases {
        return ToDoUsesCases(
            getTasks = GetTasks(toDoRepository),
            upsertTask = UpsertTask(toDoRepository),
            deleteTask = DeleteTask(toDoRepository)
        )
    }

    @Provides
    @Singleton
    fun provideRepository(
        tasksDao: TasksDao
    ): ToDoRepository = ToDoRepositoryImpl(tasksDao)

    @Provides
    @Singleton
    fun provideAuthRepository(
        awsAuthService: AwsAuthService
    ): AwsAuthRepository = AwsAuthRepositoryImpl(awsAuthService)

    @Provides
    @Singleton
    fun provideStorageRepository(
        awsStorageService: AwsStorageService
    ): AwsStorageServiceRepository = AwsStorageServiceRepositoryImpl(awsStorageService)

    @Provides
    @Singleton
    fun provideThemeRepository(
        application: Application
    ): ThemeRepository = ThemeRepositoryImpl(application)

    @Provides
    @Singleton
    fun provideAwsAuthService(): AwsAuthService = AwsAuthServiceImpl()

    @Provides
    @Singleton
    fun provideAwsStorageService(
        application: Application
    ): AwsStorageService = AwsStorageServiceImpl(
        context = application
    )

    @Provides
    @Singleton
    fun provideTaskDatabase(
        application: Application
    ): TasksDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = TasksDatabase::class.java,
            name = "TaskDatabase"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        tasksDatabase: TasksDatabase
    ): TasksDao = tasksDatabase.tasksDao
}