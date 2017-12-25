package test

import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

fun main(args: Array<String>) {
    val socket = Socket()
    socket.connect(InetSocketAddress("127.0.0.1", 6789))
    val buf = ByteArray(4096)
    do {
        val len = socket.getInputStream().read(buf)
        println("读取数据长度：$len")
    } while (len > 0)
}