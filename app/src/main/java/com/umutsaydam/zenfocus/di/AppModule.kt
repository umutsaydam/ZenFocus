package com.umutsaydam.zenfocus.di

import android.app.Application
import androidx.room.Room
import com.umutsaydam.zenfocus.data.local.TasksDao
import com.umutsaydam.zenfocus.data.local.TasksDatabase
import com.umutsaydam.zenfocus.data.local.ToDoRepositoryImpl
import com.umutsaydam.zenfocus.data.manager.LocalUserManagerImpl
import com.umutsaydam.zenfocus.data.remote.AuthRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.AwsServiceImpl
import com.umutsaydam.zenfocus.domain.localUserManager.LocalUserManager
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.domain.service.AwsService
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
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.localUserCases.cases.vibrateCases.SaveVibrateState
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignUpConfirm
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
            deleteUserType = DeleteUserType(localUserManager)
        )
    }

    @Provides
    @Singleton
    fun provideAwsAuthUsesCases(
        authRepository: AuthRepository
    ): AwsAuthCases {
        return AwsAuthCases(
            userSignIn = AwsUserSignIn(authRepository),
            userSignUp = AwsUserSignUp(authRepository),
            userSignUpConfirm = AwsUserSignUpConfirm(authRepository),
            userGetId = AwsUserGetUserId(authRepository),
            readUserInfo = AwsReadUserInfo(authRepository),
            signOut = AwsSignOut(authRepository)
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
        awsService: AwsService
    ): AuthRepository = AuthRepositoryImpl(awsService)

    @Provides
    @Singleton
    fun provideAwsService(): AwsService = AwsServiceImpl()

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