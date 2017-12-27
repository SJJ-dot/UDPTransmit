package sjj.transmit

import io.reactivex.schedulers.Schedulers
import java.net.ServerSocket

class SocketTransmitService(private val service: TransmitService = TransmitService()) {
    private val socket = ServerSocket(6789)
    fun start() {
        println("服务端IP:${socket.localSocketAddress}")
        service.connect()
        while (true) {
            val accept = socket.accept()
            println("客户端接入：${accept.remoteSocketAddress} ${service.publish.hasSubscribers()}")
            service.publish.observeOn(Schedulers.newThread()).ofType(ByteArray::class.java).onBackpressureBuffer().subscribe(SocketSubscriber(accept) {
                service.push(it)
            })
        }
    }
}