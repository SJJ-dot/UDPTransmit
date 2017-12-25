package sjj.transmit

import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class SocketSubscriber(private var accept: Socket?, private val send: (ByteArray) -> Unit) : FlowableSubscriber<ByteArray> {
    private var sendT: Thread? = null
    override fun onNext(t: ByteArray?) {
        accept?.getOutputStream()?.write(t)
    }

    override fun onComplete() {
        println("onComplete")
        accept?.close()
        accept = null
        sendT?.interrupt()
        sendT = null
    }

    override fun onError(t: Throwable?) {
        println("onError")
        t?.printStackTrace()
        accept?.close()
        accept = null
        sendT?.interrupt()
        sendT = null
    }

    override fun onSubscribe(s: Subscription) {
        println("onSubscribe")
        sendT?.interrupt()
        sendT = thread {
            val input = accept?.getInputStream() ?: return@thread
            val buf = ByteArray(4096)
            while (true) {
                try {
                    val read = input.read(buf)
                    if (read > 0) {
//                        println("length $read")
                        send(Arrays.copyOf(buf, read))
                    } else {
                        Thread.sleep(10)
                    }
                } catch (e: Exception) {

                }
            }
        }
        s.request(Long.MAX_VALUE)
    }
}