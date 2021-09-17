package com.example.rsocket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.example.rsocket.client.CallbackService;
import com.example.rsocket.dto.RequestDto;
import com.example.rsocket.util.ObjectUtil;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02CallbackTest {
	
	private RSocket rSocket;
	
	@BeforeAll
	public void setup() {
		this.rSocket = RSocketConnector.create()
				.acceptor(SocketAcceptor.with(new CallbackService()))
				.connect(WebsocketClientTransport.create("localhost", 6565))
				.block();
	}
	
	@Test
	public void callback() throws InterruptedException {
		RequestDto requestDto = new RequestDto(5);
		Mono<Void> mono = this.rSocket.fireAndForget(ObjectUtil.toPayload(requestDto));
		
		StepVerifier.create(mono)
			.verifyComplete();
		
		System.out.println("going to wait");
		
		Thread.sleep(12000);
		
	}
}
