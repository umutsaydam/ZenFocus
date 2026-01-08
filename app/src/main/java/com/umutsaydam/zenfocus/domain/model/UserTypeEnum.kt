package com.umutsaydam.zenfocus.domain.model

enum class UserTypeEnum(val type: String) {
    NORMAL_USER("Normal User"),
    AD_FREE_USER("Ad Free User");

    companion object {
        fun fromType(type: String?): UserTypeEnum =
            entries.firstOrNull() { it.type == type } ?: NORMAL_USER
    }
}