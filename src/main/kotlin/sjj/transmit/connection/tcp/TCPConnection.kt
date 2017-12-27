package sjj.transmit.connection.tcp

import sjj.transmit.connection.ConnectInterface
import java.net.InetAddress
import java.net.Socket

class TCPConnection(private val ip: String = "192.168.2.221", private val port: Int = 5760) : ConnectInterface {
    private var socket: Socket? = null
    override fun openConnection() {
        socket = Socket(InetAddress.getByName(ip), port)
    }

    override fun readDataBlock(buffer: ByteArray): Int {
        return socket!!.getInputStream().read(buffer)
    }

    override fun sendBuffer(buffer: ByteArray) {
        socket!!.getOutputStream().write(buffer)
    }

    override fun closeConnection() {
        socket?.close()
        socket = null
    }
}