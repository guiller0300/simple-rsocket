package com.example.rsocket;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
public class Lec04PersistentConnectionTest {
	
	
private RSocketClient rSocketClient;
	
	@BeforeAll
	public void setup() {
		Mono<RSocket> socketMono = RSocketConnector.create()
				.connect(TcpClientTransport.create("localhost", 6565))
				.doOnNext(r -> System.out.println("going to connect"));
		
		this.rSocketClient = RSocketClient.from(socketMono);
	}
	
	@Test
	public void connectionTest() throws InterruptedException {
		
		Flux<String> flux1 = this.rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
			.map(Payload::getDataUtf8)
			.delayElements(Duration.ofMillis(300))
			.take(10)
			.doOnNext(System.out::println);
		
		StepVerifier.create(flux1)
			.expectNextCount(10)
			.verifyComplete();
		
		System.out.println("going to wait");
		Thread.sleep(15000);
		System.out.println("wake up");
		
		Flux<String> flux2 = this.rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
				.map(Payload::getDataUtf8)
				.delayElements(Duration.ofMillis(300))
				.take(10)
				.doOnNext(System.out::println);
			
			StepVerifier.create(flux2)
				.expectNextCount(10)
				.verifyComplete();
		
	}
}
