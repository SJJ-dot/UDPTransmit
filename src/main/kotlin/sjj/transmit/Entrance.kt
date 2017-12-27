package sjj.transmit

import sjj.transmit.connection.ConnectInterface
import sjj.transmit.connection.ConnectState
import sjj.transmit.connection.tcp.TCPConnection
import java.util.*

/**
 *默认UDP 监听 14550 端口
 * args  tcp    ip port
 */
fun main(args: Array<String>) {
    val dataService: TransmitService
    if (args.size > 1) {
        dataService = TransmitService(TCPConnection(args[0],args[1].toInt()))
    } else {
        dataService = TransmitService()
    }
    dataService.publish.ofType(ConnectState::class.java).subscribe(::println,::println)
    SocketTransmitService(dataService).start()
}