package test

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.*
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val socket = DatagramSocket(12340)
    rec(socket)
    val send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    send.socketAddress = InetSocketAddress("192.168.2.226",12345)
    val data = byteArrayOf(1,2,3,4,5,6)
    while (true) {
        send.data = data
        socket.send(send)
        Thread.sleep(10)
    }
}

fun rec(socket:DatagramSocket) {
    thread {
        val buf = DatagramPacket(ByteArray(4096), 4096)
        while (true) {
            socket.receive(buf)
            if (buf.length > 0) {
                println(Arrays.copyOf(buf.data, buf.length).hex())
            } else {
                Thread.sleep(100)
            }
        }
    }
}