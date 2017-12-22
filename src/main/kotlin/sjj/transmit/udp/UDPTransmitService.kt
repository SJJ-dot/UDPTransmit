package sjj.transmit.udp

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.concurrent.thread

class UDPTransmitService {
    private val flowable: Flowable<ByteArray> = PublishProcessor.create<ByteArray>().toSerialized()
    private var receive: DatagramPacket = DatagramPacket(ByteArray(4096), 4096)
    private var send: DatagramPacket = DatagramPacket(byteArrayOf(0), 0)
    init {
        val socket = DatagramSocket(14550)
        thread {
            while (true) {

            }
        }
    }
}