package com.umutsaydam.zenfocus.data.local.mapper

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String): UUID {
        return uuid.let { UUID.fromString(it) }
    }
}
