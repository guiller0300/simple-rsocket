package com.example.rsocket;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03BackpressureTest {

	
	private RSocket rSocket;
	
	@BeforeAll
	public void setup() {
		this.rSocket = RSocketConnector.create()
				.connect(WebsocketClientTransport.create("localhost", 6565))
				.block();
	}
	
	@Test
	public void backpressure() throws InterruptedException {
		
		Flux<String> flux = this.rSocket.requestStream(DefaultPayload.create(""))
			.map(Payload::getDataUtf8)
			.delayElements(Duration.ofSeconds(1))
			.doOnNext(System.out::println);
		
		StepVerifier.create(flux)
			.expectNextCount(1000)
			.verifyComplete();
		
		System.out.println("going to wait");
		
		Thread.sleep(12000);
		
	}
}
