package test

import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

fun main(args: Array<String>) {
//    val socket = Socket()
//    socket.connect(InetSocketAddress("127.0.0.1", 6789))
//    val buf = ByteArray(4096)
//    do {
//        val len = socket.getInputStream().read(buf)
//        println("读取数据长度：$len")
//    } while (len > 0)
    val p1 = P(0.0, 0.0)
    val p2 = P(1.0, 1.0)
    val p3 = P(0.0, 1.0)
    test(p1, p2, p3)
    test(p1, p3, p2)
    test(p1, p2, P(2.0, 2.0))
}

fun test(p1: P, p2: P, p3: P) {
    println(if (d(p1, p2, p3) > 0) "逆时针" else if (d(p1, p2, p3) < 0) "顺时针" else "000")
}

fun d(p1: P, p2: P, p3: P): Double {
    return (p1.x - p3.x) * (p2.y - p3.y) - (p1.y - p3.y) * (p2.x - p3.x)
}

data class P(val x: Double, val y: Double)