package test

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.*

fun main(args: Array<String>) {
    val socket = DatagramSocket(12345)
    val buf = DatagramPacket(ByteArray(4096), 4096)
    val send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    send.socketAddress = InetSocketAddress("192.168.2.226",12340)
    while (true) {
        socket.receive(buf)
        if (buf.length > 0) {
//            println(Arrays.copyOf(buf.data, buf.length).hex())
            send.data = Arrays.copyOf(buf.data, buf.length)
            socket.send(send)
        } else {
            Thread.sleep(100)
        }
    }
}
fun ByteArray.toHexString(): String = map { String.format("%02X ", it) }.reduce { acc, s -> acc + " " + s }

fun ByteArray.hex(): String = map { String.format("%02X ", it) }.reduce { acc, s -> acc + s }

fun ByteArray.toHexString(start: Int, length: Int): String {
    val sb = StringBuilder()
    for (index in start until start + length) {
        sb.append(String.format("%02X ", get(index)))
    }
    return sb.toString()
}

fun Int.hex() = String.format("%02X ", this)