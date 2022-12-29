package dev.amal.onthewakelivekmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform