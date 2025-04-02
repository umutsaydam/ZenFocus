package com.umutsaydam.zenfocus.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.umutsaydam.zenfocus.data.local.db.PomodoroSessionsDao
import com.umutsaydam.zenfocus.data.local.db.TasksDao
import com.umutsaydam.zenfocus.data.local.db.ZenFocusDB
import com.umutsaydam.zenfocus.data.local.manager.ExoPlayerManagerImpl
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
//import com.umutsaydam.zenfocus.data.remote.repository.GoogleProductsInAppRepositoryImpl
//import com.umutsaydam.zenfocus.data.remote.repository.IntegrateInAppReviewsRepositoryImpl
//import com.umutsaydam.zenfocus.data.remote.service.GoogleAdServiceImpl
import com.umutsaydam.zenfocus.data.local.repository.PomodoroSessionRepositoryImpl
import com.umutsaydam.zenfocus.data.remote.repository.AwsPomodoroSessionsRepositoryImpl
import com.umutsaydam.zenfocus.domain.manager.ExoPlayerManager
// These lines are commented for the open source contribution.
//import com.umutsaydam.zenfocus.data.remote.repository.GoogleProductsInAppRepositoryImpl
//import com.umutsaydam.zenfocus.data.remote.service.GoogleAdServiceImpl
import com.umutsaydam.zenfocus.domain.manager.LocalUserManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroServiceManager
import com.umutsaydam.zenfocus.domain.repository.RingerModeRepository
import com.umutsaydam.zenfocus.domain.manager.TimeOutRingerManager
import com.umutsaydam.zenfocus.domain.manager.VibrationManager
import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import com.umutsaydam.zenfocus.domain.repository.local.NetworkCheckerRepository
import com.umutsaydam.zenfocus.domain.repository.local.PomodoroSessionRepository
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.domain.repository.local.ToDoRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AwsPomodoroSessionsRepository
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
// These line is commented for the open source contribution.
//import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
// These line is commented for the open source contribution.
//import com.umutsaydam.zenfocus.domain.service.GoogleAdService
import com.umutsaydam.zenfocus.domain.usecases.local.DeviceRingerModeCases
import com.umutsaydam.zenfocus.domain.usecases.local.ExoPlayerManagerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.ExoPlayerManagerUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.FocusSoundUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCase
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroManagerUseCaseImpl
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.PomodoroServiceUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.ThemeRepositoryUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.ThemeRepositoryUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.TimeOutRingerManagerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.TimeOutRingerManagerUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.VibrationManagerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.VibrationManagerUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.DeleteUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.DeleteUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.ReadAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.ReadAppLang
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.ReadUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.ReadUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.ReadVibrateState
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appEntryCases.SaveAppEntry
import com.umutsaydam.zenfocus.domain.usecases.local.cases.appLangCases.SaveAppLang
//import com.umutsaydam.zenfocus.domain.usecases.local.cases.appReviewCases.ReadAvailableForReview
//import com.umutsaydam.zenfocus.domain.usecases.local.cases.appReviewCases.SaveAvailableForReview
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
import com.umutsaydam.zenfocus.domain.usecases.local.cases.timeOutRingerStateCases.ReadTimeOutRingerState
import com.umutsaydam.zenfocus.domain.usecases.local.cases.timeOutRingerStateCases.SaveTimeOutRingerState
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userIdCases.SaveUserId
import com.umutsaydam.zenfocus.domain.usecases.local.cases.userTypeCases.SaveUserType
import com.umutsaydam.zenfocus.domain.usecases.local.cases.vibrateCases.SaveVibrateState
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCases
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsPomodoroSessionsUseCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsPomodoroSessionsUseCasesImpl
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsStorageCases
// These lines are commented for the open source contribution.
//import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCases
//import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleAdUseCasesImpl
//import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCases
//import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCasesImpl
//import com.umutsaydam.zenfocus.domain.usecases.remote.IntegrateInAppReviewsUseCases
//import com.umutsaydam.zenfocus.domain.usecases.remote.IntegrateInAppReviewsUseCasesImpl
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
    fun provideLocalUserCases(
        localUserManager: LocalUserManager
    ): LocalUserDataStoreCases {
        return LocalUserDataStoreCases(
            saveAppEntry = SaveAppEntry(localUserManager),
            readAppEntry = ReadAppEntry(localUserManager),
            saveVibrateState = SaveVibrateState(localUserManager),
            readVibrateState = ReadVibrateState(localUserManager),
            saveTimeOutRingerState = SaveTimeOutRingerState(localUserManager),
            readTimeOutRingerState = ReadTimeOutRingerState(localUserManager),
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
            readPomodoroWorkDuration = ReadPomodoroWorkDuration(localUserManager),
//            saveAvailableForReview = SaveAvailableForReview(localUserManager),
//            readAvailableForReview = ReadAvailableForReview(localUserManager)
        )
    }

    @Provides
    fun provideTimeOutRingerManagerUseCases(
        timeOutRingerManager: TimeOutRingerManager
    ): TimeOutRingerManagerUseCases = TimeOutRingerManagerUseCasesImpl(timeOutRingerManager)

    @Provides
    fun provideVibrationUseCases(
        vibrationManager: VibrationManager
    ): VibrationManagerUseCases = VibrationManagerUseCasesImpl(vibrationManager)

    @Provides
    fun provideThemeRepositoryUseCases(
        themeRepository: ThemeRepository
    ): ThemeRepositoryUseCases = ThemeRepositoryUseCasesImpl(themeRepository)

    @Provides
    fun provideExoPlayerManagerUseCases(
        exoPlayerManager: ExoPlayerManager
    ): ExoPlayerManagerUseCases = ExoPlayerManagerUseCasesImpl(exoPlayerManager)

    @Provides
    fun provideFocusSoundUseCases(
        focusSoundManager: FocusSoundManager
    ): FocusSoundUseCases = FocusSoundUseCasesImpl(focusSoundManager)

    @Provides
    fun providePomodoroManagerUseCases(
        pomodoroManager: PomodoroManager
    ): PomodoroManagerUseCase = PomodoroManagerUseCaseImpl(pomodoroManager)

    @Provides
    fun provideAwsPomodoroSessionsUseCases(
        repository: AwsPomodoroSessionsRepository
    ): AwsPomodoroSessionsUseCases = AwsPomodoroSessionsUseCasesImpl(repository)

    @Provides
    fun providePomodoroServiceUseCases(
        pomodoroServiceManager: PomodoroServiceManager
    ): PomodoroServiceUseCases = PomodoroServiceUseCasesImpl(pomodoroServiceManager)

//    @Provides
//    fun provideIntegrateInAppReviewsUseCases(
//        integrateInAppReviewsRepository: IntegrateInAppReviewsRepository
//    ): IntegrateInAppReviewsUseCases =
//        IntegrateInAppReviewsUseCasesImpl(integrateInAppReviewsRepository)

    @Provides
    fun provideDeviceRingerModeCases(
        ringerModeRepository: RingerModeRepository
    ): DeviceRingerModeCases {
        return DeviceRingerModeCases(
            readRingerMode = ReadRingerMode(ringerModeRepository)
        )
    }

//    @Provides
//    fun provideIntegrateInAppReviewsRepository(): IntegrateInAppReviewsRepository =
//        IntegrateInAppReviewsRepositoryImpl()

    @Provides
    fun provideNetworkCheckerUseCases(
        networkCheckerRepository: NetworkCheckerRepository
    ): NetworkCheckerUseCases = NetworkCheckerUseCasesImpl(networkCheckerRepository)

    @Provides
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
    fun provideAwsStorageUsesCases(
        awsStorageRepository: AwsStorageServiceRepository
    ): AwsStorageCases {
        return AwsStorageCases(
            readThemeList = ReadThemeList(awsStorageRepository),
            downloadSelectedThemeList = DownloadSelectedTheme(awsStorageRepository)
        )
    }

    @Provides
    fun provideToDoUsesCases(
        toDoRepository: ToDoRepository
    ): ToDoUsesCases {
        return ToDoUsesCases(
            getTasks = GetTasks(toDoRepository),
            upsertTask = UpsertTask(toDoRepository),
            deleteTask = DeleteTask(toDoRepository)
        )
    }

    @Provides
    fun providePomodoroSessionsUseCases(
        sessionRepository: PomodoroSessionRepository
    ): PomodoroSessionsUseCases = PomodoroSessionsUseCasesImpl(sessionRepository)

//    @Provides
//    fun provideGoogleAdServiceUseCases(
//        googleAdService: GoogleAdService
//    ): GoogleAdUseCases = GoogleAdUseCasesImpl(googleAdService)
//
//    @Provides
//    fun provideGoogleProductsInAppUseCases(
//        googleProductsInAppRepository: GoogleProductsInAppRepository
//    ): GoogleProductsInAppUseCases = GoogleProductsInAppUseCasesImpl(googleProductsInAppRepository)

    @Provides
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
        timeOutRingerManagerUseCases: TimeOutRingerManagerUseCases,
        vibrationManagerUseCases: VibrationManagerUseCases,
        pomodoroSessionsUseCases: PomodoroSessionsUseCases
    ): PomodoroManager = PomodoroManagerImpl(
        focusSoundManager,
        timeOutRingerManagerUseCases,
        vibrationManagerUseCases,
        pomodoroSessionsUseCases
    )

    @Provides
    @Singleton
    fun provideVibrationManager(
        ringerModeCases: DeviceRingerModeCases, application: Application
    ): VibrationManager = VibrationManagerImpl(ringerModeCases, application)

    @Provides
    @Singleton
    fun provideSoundManager(
        application: Application
    ): TimeOutRingerManager = TimeOutRingerManagerImpl(application)

    @Provides
    @Singleton
    fun provideExoPlayerManager(
        application: Application
    ): ExoPlayerManager = ExoPlayerManagerImpl(application)

    @Provides
    @Singleton
    fun provideFocusSoundManager(
        application: Application
    ): FocusSoundManager = FocusSoundManagerImpl(application)

    @Provides
    @Singleton
    fun provideNetworkChecker(
        application: Application
    ): NetworkCheckerRepository = NetworkCheckerRepositoryImpl(application)

//    @Provides
//    fun provideGoogleProductsInAppRepository(
//        @ApplicationContext context: Context
//    ): GoogleProductsInAppRepository = GoogleProductsInAppRepositoryImpl(context)


    @Provides
    fun provideRepository(
        tasksDao: TasksDao
    ): ToDoRepository = ToDoRepositoryImpl(tasksDao)

    @Provides
    fun providePomodoroSessionRepository(
        pomodoroSessionsDao: PomodoroSessionsDao
    ): PomodoroSessionRepository = PomodoroSessionRepositoryImpl(pomodoroSessionsDao)

    @Provides
    fun provideStorageRepository(
        application: Application
    ): AwsStorageServiceRepository = AwsStorageServiceRepositoryImpl(application)

    @Provides
    fun provideThemeRepository(
        application: Application
    ): ThemeRepository = ThemeRepositoryImpl(application)

    @Provides
    fun provideRingerModeRepository(
        application: Application
    ): RingerModeRepository = RingerModeRepositoryImpl(application)
// These lines are commented for the open source contribution.
//    @Provides
//    @Singleton
//    fun provideGoogleAdService(application: Application): GoogleAdService =
//        GoogleAdServiceImpl(application)

    @Provides
    fun provideAwsPomodoroSessionsRepository(
        pomodoroSessionsUseCases: PomodoroSessionsUseCases
    ): AwsPomodoroSessionsRepository =
        AwsPomodoroSessionsRepositoryImpl(pomodoroSessionsUseCases)

    @Provides
    @Singleton
    fun provideAwsAuthService(): AwsAuthService = AwsAuthServiceImpl()

    @Provides
    @Singleton
    fun provideZenFocusDatabase(
        application: Application
    ): ZenFocusDB {
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                """
                CREATE TABLE IF NOT EXISTS `pomodoro_sessions` (
                    `session_id` TEXT PRIMARY KEY NOT NULL,
                    `work_duration` LONG NOT NULL,
                    `break_duration` LONG NOT NULL,
                    `session_date` TEXT NOT NULL
                )
                """.trimIndent()
            }
        }
        return Room.databaseBuilder(
            context = application.applicationContext,
            klass = ZenFocusDB::class.java,
            name = "ZenFocusDB"
        )
            .addMigrations(migration1to2)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        zenFocusDB: ZenFocusDB
    ): TasksDao = zenFocusDB.tasksDao

    @Provides
    @Singleton
    fun providePomodoroSessionsDao(
        zenFocusDB: ZenFocusDB
    ): PomodoroSessionsDao = zenFocusDB.pomodoroSessionsDao
}