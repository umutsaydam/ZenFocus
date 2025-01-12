package com.umutsaydam.zenfocus.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.umutsaydam.zenfocus.data.local.db.TasksDao
import com.umutsaydam.zenfocus.data.local.db.TasksDatabase
import com.umutsaydam.zenfocus.data.local.repository.ThemeRepositoryImpl
import com.umutsaydam.zenfocus.data.local.repository.ToDoRepositoryImpl
import com.umutsaydam.zenfocus.data.local.manager.LocalUserManagerImpl
import com.umutsaydam.zenfocus.data.local.manager.PomodoroManagerImpl
import com.umutsaydam.zenfocus.data.local.manager.PomodoroServiceManagerImpl
import com.umutsaydam.zenfocus.data.local.manager.TimeOutRingerManagerImpl
import com.umutsaydam.zenfocus.data.local.manager.VibrationManagerImpl
import com.umutsaydam.zenfocus.data.remote.repository.AwsStorageServiceRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.service.AwsAuthServiceImpl
import com.umutsaydam.zenfocus.data.local.repository.RingerModeRepositoryImpl
import com.umutsaydam.zenfocus.data.local.manager.FocusSoundManagerImpl
import com.umutsaydam.zenfocus.data.local.repository.NetworkCheckerRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.repository.GoogleProductsInAppRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.service.GoogleAdServiceImpl
import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroServiceManager
import com.umutsaydam.zenfocus.domain.repository.RingerModeRepository
import com.umutsaydam.zenfocus.domain.manager.TimeOutRingerManager
import com.umutsaydam.zenfocus.domain.manager.VibrationManager
import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import com.umutsaydam.zenfocus.domain.repository.local.NetworkCheckerRepository
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.domain.service.GoogleAdService
import com.umutsaydam.zenfocus.domain.usecases.local.DeviceRingerModeCases
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCaseImpl
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.DeleteUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.DeleteUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.ReadAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.ReadAppLang
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.ReadUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.ReadUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.ReadVibrateState
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.SaveAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.SaveAppLang
import com.umutsaydam.zenfocus.domain.usecases.local.cases.focusSoundCases.ReadFocusSound
import com.umutsaydam.zenfocus.domain.usecases.local.cases.focusSoundCases.SaveFocusSound
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases.ReadPomodoroBreakDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroBreakDurationCases.SavePomodoroBreakDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases.ReadPomodoroCycle
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroCycleCases.SavePomodoroCycle
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases.ReadPomodoroWorkDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.pomodoroWorkDurationCases.SavePomodoroWorkDuration
import com.umutsaydam.zenfocus.domain.usecases.local.cases.ringerModeCases.ReadRingerMode
import com.umutsaydam.zenfocus.domain.usecases.local.cases.themeCases.ReadTheme
import com.umutsaydam.zenfocus.domain.usecases.local.cases.themeCases.SaveTheme
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.SaveVibrateState
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsStorageCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignInWithGoogle
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUpdateUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUpConfirm
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsResendConfirmationCode
import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.DownloadSelectedTheme
import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.ReadThemeList
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.DeleteTask
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.GetTasks
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.cases.UpsertTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserCases(
        localUserManager: LocalUserManager
    ): LocalUserDataStoreCases {
        return LocalUserDataStoreCases(
            saveAppEntry = SaveAppEntry(localUserManager),
            readAppEntry = ReadAppEntry(localUserManager),
            saveVibrateState = SaveVibrateState(localUserManager),
            readVibrateState = ReadVibrateState(localUserManager),
            saveAppLang = SaveAppLang(localUserManager),
            readAppLang = ReadAppLang(localUserManager),
            saveFocusSound = SaveFocusSound(localUserManager),
            readFocusSound = ReadFocusSound(localUserManager),
            saveUserId = SaveUserId(localUserManager),
            readUserId = ReadUserId(localUserManager),
            deleteUserId = DeleteUserId(localUserManager),
            saveUserType = SaveUserType(localUserManager),
            readUserType = ReadUserType(localUserManager),
            deleteUserType = DeleteUserType(localUserManager),
            saveTheme = SaveTheme(localUserManager),
            readTheme = ReadTheme(localUserManager),
            savePomodoroCycle = SavePomodoroCycle(localUserManager),
            readPomodoroCycle = ReadPomodoroCycle(localUserManager),
            savePomodoroBreakDuration = SavePomodoroBreakDuration(localUserManager),
            readPomodoroBreakDuration = ReadPomodoroBreakDuration(localUserManager),
            savePomodoroWorkDuration = SavePomodoroWorkDuration(localUserManager),
            readPomodoroWorkDuration = ReadPomodoroWorkDuration(localUserManager)
        )
    }

    @Provides
    @Singleton
    fun provideFocusSoundUseCases(
        focusSoundManager: FocusSoundManager
    ): FocusSoundUseCases = FocusSoundUseCasesImpl(focusSoundManager)

    @Provides
    @Singleton
    fun providePomodoroManagerUseCases(
        pomodoroManager: PomodoroManager
    ): PomodoroManagerUseCase = PomodoroManagerUseCaseImpl(pomodoroManager)

    @Provides
    @Singleton
    fun providePomodoroServiceUseCases(
        pomodoroServiceManager: PomodoroServiceManager
    ): PomodoroServiceUseCases = PomodoroServiceUseCasesImpl(pomodoroServiceManager)

    @Provides
    @Singleton
    fun provideDeviceRingerModeCases(
        ringerModeRepository: RingerModeRepository
    ): DeviceRingerModeCases {
        return DeviceRingerModeCases(
            readRingerMode = ReadRingerMode(ringerModeRepository)
        )
    }

    @Provides
    @Singleton
    fun provideNetworkCheckerUseCases(
        networkCheckerRepository: NetworkCheckerRepository
    ): NetworkCheckerUseCases = NetworkCheckerUseCasesImpl(networkCheckerRepository)

    @Provides
    @Singleton
    fun provideAwsAuthUsesCases(
        awsAuthService: AwsAuthService
    ): AwsAuthCases {
        return AwsAuthCases(
            userSignIn = AwsUserSignIn(awsAuthService),
            userSignUp = AwsUserSignUp(awsAuthService),
            userSignUpConfirm = AwsUserSignUpConfirm(awsAuthService),
            awsResendConfirmationCode = AwsResendConfirmationCode(awsAuthService),
            userGetId = AwsUserGetUserId(awsAuthService),
            readUserInfo = AwsReadUserInfo(awsAuthService),
            updateUserInfo = AwsUpdateUserInfo(awsAuthService),
            signInWithGoogle = AwsSignInWithGoogle(awsAuthService),
            signOut = AwsSignOut(awsAuthService)
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
    fun provideGoogleAdServiceUseCases(
        googleAdService: GoogleAdService
    ): GoogleAdUseCases = GoogleAdUseCasesImpl(googleAdService)

    @Provides
    @Singleton
    fun provideGoogleProductsInAppUseCases(
        googleProductsInAppRepository: GoogleProductsInAppRepository
    ): GoogleProductsInAppUseCases = GoogleProductsInAppUseCasesImpl(googleProductsInAppRepository)

    @Provides
    @Singleton
    fun localUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun providePomodoroServiceManager(
        application: Application
    ): PomodoroServiceManager = PomodoroServiceManagerImpl(application)

    @Provides
    @Singleton
    fun providePomodoroManager(
        focusSoundManager: FocusSoundManager,
        timeOutRingerManager: TimeOutRingerManager,
        vibrationManager: VibrationManager
    ): PomodoroManager =
        PomodoroManagerImpl(focusSoundManager, timeOutRingerManager, vibrationManager)

    @Provides
    @Singleton
    fun provideVibrationManager(
        ringerModeCases: DeviceRingerModeCases,
        application: Application
    ): VibrationManager = VibrationManagerImpl(ringerModeCases, application)

    @Provides
    @Singleton
    fun provideSoundManager(
        deviceRingerModeCases: DeviceRingerModeCases,
        application: Application
    ): TimeOutRingerManager = TimeOutRingerManagerImpl(deviceRingerModeCases, application)

    @Provides
    @Singleton
    fun provideNetworkChecker(
        application: Application
    ): NetworkCheckerRepository = NetworkCheckerRepositoryImpl(application)

    @Provides
    @Singleton
    fun provideGoogleProductsInAppRepository(
        @ApplicationContext context: Context
    ): GoogleProductsInAppRepository = GoogleProductsInAppRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideSoundRepository(
        deviceRingerModeCases: DeviceRingerModeCases,
        application: Application
    ): FocusSoundManager = FocusSoundManagerImpl(deviceRingerModeCases, application)

    @Provides
    @Singleton
    fun provideRepository(
        tasksDao: TasksDao
    ): ToDoRepository = ToDoRepositoryImpl(tasksDao)

    @Provides
    @Singleton
    fun provideStorageRepository(
        application: Application
    ): AwsStorageServiceRepository = AwsStorageServiceRepositoryImpl(application)

    @Provides
    @Singleton
    fun provideThemeRepository(
        application: Application
    ): ThemeRepository = ThemeRepositoryImpl(application)

    @Provides
    @Singleton
    fun provideRingerModeRepository(
        application: Application
    ): RingerModeRepository = RingerModeRepositoryImpl(application)

    @Provides
    @Singleton
    fun provideGoogleAdService(application: Application): GoogleAdService =
        GoogleAdServiceImpl(application)

    @Provides
    @Singleton
    fun provideAwsAuthService(): AwsAuthService = AwsAuthServiceImpl()

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