package com.example.rsocket;

import com.example.rsocket.service.SocketAcceptorImpl;

import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
public class Server {
	
	public static void main(String[] args) {
		
		RSocketServer socketServer = RSocketServer.create(new SocketAcceptorImpl());
		CloseableChannel closeableChannel = socketServer.bindNow(WebsocketServerTransport.create(6565));
		//keep listening
		closeableChannel.onClose().block();
	}
}
