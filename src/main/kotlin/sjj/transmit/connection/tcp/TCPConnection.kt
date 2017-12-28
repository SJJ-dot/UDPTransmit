package sjj.transmit.connection.tcp

import sjj.transmit.connection.ConnectInterface
import java.net.InetSocketAddress
import java.net.Socket

class TCPConnection(private val ip: String = "192.168.2.221", private val port: Int = 5760) : ConnectInterface {
    private lateinit var socket: Socket
    override fun openConnection() {
        socket = Socket()
        socket.connect(InetSocketAddress(ip, port),5000)
    }

    override fun readDataBlock(buffer: ByteArray): Int {
        return socket.getInputStream().read(buffer)
    }

    override fun sendBuffer(buffer: ByteArray) {
        socket.getOutputStream().write(buffer)
    }

    override fun closeConnection() {
        socket.close()
    }
}