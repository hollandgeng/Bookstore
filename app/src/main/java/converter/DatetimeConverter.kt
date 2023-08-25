package converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DatetimeConverter
{
    @TypeConverter
    fun toLocalDateTime(value: String?) :LocalDateTime?
    {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun toDBTimestamp(date: LocalDateTime?) : String?
    {
        return date?.toString()
    }
}