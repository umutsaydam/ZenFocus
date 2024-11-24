package com.umutsaydam.zenfocus.di

import android.app.Application
import androidx.room.Room
import com.umutsaydam.zenfocus.data.TasksDao
import com.umutsaydam.zenfocus.data.TasksDatabase
import com.umutsaydam.zenfocus.data.ToDoRepositoryImpl
import com.umutsaydam.zenfocus.domain.repository.ToDoRepository
import com.umutsaydam.zenfocus.domain.usecases.tasks.DeleteTask
import com.umutsaydam.zenfocus.domain.usecases.tasks.GetTasks
import com.umutsaydam.zenfocus.domain.usecases.tasks.ToDoUsesCases
import com.umutsaydam.zenfocus.domain.usecases.tasks.UpsertTask
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
    fun provideToDoUsesCases(
        toDoRepository: ToDoRepository,
        tasksDao: TasksDao
    ): ToDoUsesCases {
        return  ToDoUsesCases(
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