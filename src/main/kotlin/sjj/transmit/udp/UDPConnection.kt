package sjj.transmit.udp

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress

class UDPConnection : ConnectInterface {
    private val UDP_IP = "192.168.2.224"
    private val UDP_PORT = 14550
    private val UDP_RECEIVE_PORT = 14550
    private var receive: DatagramPacket = DatagramPacket(ByteArray(4096), 4096)
    private var send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    private var conn: DatagramSocket? = null
    override fun openConnection() {
        conn?.close()
        conn = DatagramSocket(UDP_RECEIVE_PORT)
        send.socketAddress = InetSocketAddress(UDP_IP,UDP_PORT)
    }

    override fun readDataBlock(buffer: ByteArray): Int {
        val conn = conn ?: throw IOException("连接未打开")
        conn.receive(receive)
        System.arraycopy(receive.data, receive.offset, buffer, 0, receive.length)
        return receive.length
    }

    override fun sendBuffer(buffer: ByteArray) {
        send.data = buffer
        conn?.send(send)
    }

    override fun closeConnection() {
        conn?.close()
        conn = null
    }
}