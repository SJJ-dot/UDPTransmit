package sjj.transmit.udp

import java.io.IOException
import java.net.*

class UDPConnection : ConnectInterface {
    private val UDP_RECEIVE_PORT = 14550
    private var receive: DatagramPacket = DatagramPacket(ByteArray(4096), 4096)
    private var send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    private var conn: DatagramSocket? = null
    private var socketAddress: SocketAddress? = null
    private var address: InetAddress? = null
    private var port: Int = 0
    override fun openConnection() {
        conn?.close()
        conn = DatagramSocket(UDP_RECEIVE_PORT)
    }

    override fun readDataBlock(buffer: ByteArray): Int {
        val conn = conn ?: throw IOException("连接未打开")
        conn.receive(receive)
        address = receive.address
        port = receive.port
        System.arraycopy(receive.data, receive.offset, buffer, 0, receive.length)
        return receive.length
    }

    override fun sendBuffer(buffer: ByteArray) {
        val address = address ?: return
        send.address = address
        send.port = port
        send.data = buffer
        conn!!.send(send)
    }

    override fun closeConnection() {
        conn?.close()
        conn = null
    }
}