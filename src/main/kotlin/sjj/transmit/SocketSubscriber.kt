package sjj.transmit

import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class SocketSubscriber(private var accept: Socket, private val send: (ByteArray) -> Unit) : FlowableSubscriber<ByteArray> {
    private var alive = true
    private var sendT: Thread? = null
    private var s: Subscription? = null
    override fun onNext(t: ByteArray?) {
        try {
            accept.getOutputStream()?.write(t)
        } catch (e: Exception) {
            disConnect()
        }
    }

    override fun onComplete() {
        println("onComplete")
        disConnect()
    }

    override fun onError(t: Throwable?) {
        println("onError")
        disConnect()
    }

    override fun onSubscribe(s: Subscription) {
        this.s = s

        sendT = thread {
            try {
                val input = accept.getInputStream() ?: return@thread
                val buf = ByteArray(4096)
                while (alive) {
                    val read = input.read(buf)
                    if (read > 0) {
                        //                        println("length $read")
                        send(Arrays.copyOf(buf, read))
                    } else {
                       break
                    }
                }
            }finally {
                disConnect()
            }
        }
        s.request(Long.MAX_VALUE)
    }

    @Synchronized
    private fun disConnect() {
        try {
            println("连接已断开：${accept.remoteSocketAddress}")
            accept.close()
            sendT?.interrupt()
            sendT = null
        } finally {
            alive = false
            s?.cancel()
            s = null
        }
    }
}