package test

import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    var i = 0
    while (true) {
        newThreadTest(i++)
    }
}

fun newThreadTest(int: Int = 0) {
    val t = thread {
        while (true) {
            try {
                println("aaaaaa  $int")
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    Thread.sleep(1000)
    t.stop()
}