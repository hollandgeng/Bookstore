package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.softspace.bookstorepoc.interfaces.IBookDAO
import converter.DatetimeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import data.Book
import javax.inject.Singleton

@Database(
    entities = [Book::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatetimeConverter::class)
abstract class BookDB : RoomDatabase() {
    abstract val dao: IBookDAO
}
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : BookDB {
        return Room.databaseBuilder(
            context,
            BookDB::class.java,
            "books.db"
        ).build()
    }

    @Provides
    fun provideBookDao(database: BookDB): IBookDAO {
        return database.dao
    }
}