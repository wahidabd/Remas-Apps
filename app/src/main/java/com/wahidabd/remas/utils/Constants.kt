package com.wahidabd.remas.utils

import android.Manifest

object Constants {

    const val STATIC_IMAGE = "https://www.its.ac.id/international/wp-content/uploads/sites/66/2020/02/blank-profile-picture-973460_1280.jpg"
    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    const val REQUEST_CODE_PERMISSIONS = 10

    object MESSAGE {
        const val EMAIL_IS_NOT_EMPTY = "Email tidak boleh kosong"
        const val EMAIL_IS_NOT_VALID = "Email tidak valid"
        const val EMAIL_NOT_VERIFIED = "Email belum divirifikasi"
        const val PASSWORD_TO_SHORT = "password minimal mempunyai 8 karakter"
        const val PASSWORD_IS_NOT_EMPTY = "password tidak boleh kosong"
        const val PASSWORD_NOT_MATCH = "Password tidak sama"
        const val NAME_IS_NOT_EMPTY = "Nama tidak boleh kosong"
        const val USER_NOT_FOUND = "User tidak ditemukan"
        const val WRONG_LOGIN = "Gagal login, cek email atau password anda"
    }

    object TABLE {
        const val USER = "user"
        const val CHAT = "chat"
        const val CHAT_ROOM = "_chat_room"
        const val REPORT = "report"
        const val REPORT_ROOM = "_report_room"
        const val REPORT_STORAGE = "reports"
        const val USER_STORAGE = "images"
    }

}