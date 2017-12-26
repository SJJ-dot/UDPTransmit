package sjj.transmit

import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription
import java.net.Socket
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class SocketSubscriber(private var accept: Socket?, val listeners: MutableList<(ByteArray) -> Unit>, private val send: (ByteArray) -> Unit) {
    private val queue = LinkedBlockingQueue<ByteArray>()
    private val listener: (ByteArray) -> Unit = {
        queue.offer(it)
    }
    init {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }


    private var transmit: Thread? = thread {
        try {
            while (true) {
                accept?.getOutputStream()?.write(queue.take())
            }
        } catch (e: Exception) {
            disConnect()
        }
    }
    private var sendT: Thread? = thread {
        val input = accept?.getInputStream() ?: return@thread
        val buf = ByteArray(4096)
        while (true) {
            try {
                val read = input.read(buf)
                if (read > 0) {
//                        println("length $read")
                    send(Arrays.copyOf(buf, read))
//                    println("收到模拟器数据")
                } else {
                    Thread.sleep(10)
                }
            } catch (e: Exception) {

            }
        }
    }

    @Synchronized
    private fun disConnect() {
        try {
            transmit?.interrupt()
            transmit = null
            accept?.close()
            accept = null
            sendT?.interrupt()
            sendT = null
        } finally {
            synchronized(listeners) { listeners.remove(listener) }
            println("连接已断开：${listeners.size}")
        }
    }
}