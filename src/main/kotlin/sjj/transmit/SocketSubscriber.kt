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
        accept?.close()
        accept = null
        sendT?.interrupt()
        sendT = null
    }

    override fun onError(t: Throwable?) {
        accept?.close()
        accept = null
        sendT?.interrupt()
        sendT = null
    }

    override fun onSubscribe(s: Subscription) {
        sendT?.interrupt()
        sendT = thread {
            val input = accept?.getInputStream() ?: return@thread
            val buf = ByteArray(4096)
            while (true) {
                try {
                    val read = input.read(buf)
                    if (read > 0) {
                        send(Arrays.copyOf(buf, read))
                    }
                } catch (e: Exception) {

                }
            }
        }
    }
}