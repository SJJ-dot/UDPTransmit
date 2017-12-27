package sjj.transmit

import io.reactivex.schedulers.Schedulers
import sjj.transmit.connection.ConnectState
import sjj.transmit.utils.Timer
import java.net.ServerSocket

class SocketTransmitService(private val service: TransmitService = TransmitService()) {
    private val socket = ServerSocket(6789)
    fun start() {
        println("服务端IP:${socket.localSocketAddress}")
        service.connect()
        service.publish.ofType(ConnectState::class.java).subscribe({
            if (it == ConnectState.DISCONNECT) {
                Timer {
                    service.connect()
                }.start(2000)
            }
        }, ::println)
        while (true) {
            val accept = socket.accept()
            println("客户端接入：${accept.remoteSocketAddress} ${service.publish.hasSubscribers()}")
            service.publish.observeOn(Schedulers.newThread()).ofType(ByteArray::class.java).onBackpressureBuffer().subscribe(SocketSubscriber(accept) {
                service.push(it)
            })
        }
    }
}