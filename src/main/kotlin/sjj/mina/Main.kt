package sjj.mina

import org.apache.mina.api.AbstractIoHandler
import org.apache.mina.api.IoSession
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.transport.nio.NioTcpClient
import org.apache.mina.transport.nio.NioTcpServer
import java.net.InetSocketAddress
import java.util.concurrent.ExecutionException

fun main(args: Array<String>) {
    var clientSession: IoSession? = null
    val acceptor = NioTcpServer()
    acceptor.setFilters(LoggingFilter("NioTcpServer"))
    acceptor.ioHandler = object : AbstractIoHandler() {
        override fun sessionOpened(session: IoSession?) {
            println("sessionOpened ${session?.remoteAddress}")
        }

        override fun messageReceived(session: IoSession?, message: Any?) {
            clientSession?.write(message)
        }
    }
    try {
        val address = InetSocketAddress(6789)
        acceptor.bind(address)
    } catch (e: InterruptedException) {
        throw RuntimeException(e)
    }


    val client = NioTcpClient()
    client.setFilters(LoggingFilter("NioTcpClient"))
    client.ioHandler = object : AbstractIoHandler() {
        override fun sessionOpened(session: IoSession?) {
            println("sessionOpened ${session?.hashCode()}")
        }

        override fun messageReceived(session: IoSession?, message: Any?) {
            println("sessionSize ${acceptor.managedSessions.size}")
            acceptor.managedSessions.values.forEach {
                it.write(message)
            }
        }

        override fun messageSent(session: IoSession?, message: Any?) {
//            println("messageSent")
        }

        override fun sessionClosed(session: IoSession?) {
            println("sessionClosed   ${session?.hashCode()}")
        }
    }


    val socketAddress = if (args.size>1) InetSocketAddress(args[0],args[1].toInt()) else InetSocketAddress("localhost", 5760)

    while (true) {
        val future = client.connect(socketAddress)
        try {
//            future.register(object : IoFutureListener<IoSession> {
//                override fun completed(result: IoSession?) {
//                    println("completed $result")
//                }
//
//                override fun exception(t: Throwable?) {
//                    println("exception $t")
//                }
//            })
            val session = future.get()
            clientSession = session
            println("CONNECT COMPLETE ${session.remoteAddress}   ${session?.hashCode()}")
            while (!session.isClosing) {
                Thread.sleep(500)
            }
            clientSession = null
            session.close(true)
            println("CONNECT session.isClosed ")
        } catch (e: ExecutionException) {
            println("ExecutionException $e")
        }
    }
}
