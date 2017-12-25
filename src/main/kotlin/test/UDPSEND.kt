package test

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress

fun main(args: Array<String>) {
    val socket = DatagramSocket(12340)
    val send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    send.socketAddress = InetSocketAddress("192.168.2.226",12345)
    val data = byteArrayOf(1,2,3,4,5,6)
    while (true) {
        send.data = data
        socket.send(send)
        Thread.sleep(500)
    }
}