package com.wahidabd.remas.utils

object Constants {

    const val STATIC_IMAGE = "https://www.its.ac.id/international/wp-content/uploads/sites/66/2020/02/blank-profile-picture-973460_1280.jpg"

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
    }

}