package sjj.transmit

import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import sjj.transmit.udp.UDPTransmitService
import java.net.ServerSocket
import kotlin.concurrent.thread

class SocketTransmitService() {
    private val socket = ServerSocket(6789)
    fun start() {
        println("服务端IP:${socket.localSocketAddress}")
        val service = UDPTransmitService()
        service.connect()
        while (true) {
            val accept = socket.accept()
            println("客户端接入：${accept.remoteSocketAddress} ${service.listener.size}" )
            SocketSubscriber(accept, service.listener) {
                service.push(it)
            }
        }
    }
}