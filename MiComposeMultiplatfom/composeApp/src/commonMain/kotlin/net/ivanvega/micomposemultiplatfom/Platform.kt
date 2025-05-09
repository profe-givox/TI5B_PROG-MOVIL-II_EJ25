package net.ivanvega.micomposemultiplatfom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform