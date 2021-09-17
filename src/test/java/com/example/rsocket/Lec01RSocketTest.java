package com.example.rsocket;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.example.rsocket.dto.ChartResponseDto;
import com.example.rsocket.dto.RequestDto;
import com.example.rsocket.dto.ResponseDto;
import com.example.rsocket.util.ObjectUtil;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec01RSocketTest {
	
	private RSocket rSocket;
	
	@BeforeAll
	public void setup() {
		this.rSocket = RSocketConnector.create()
				.connect(TcpClientTransport.create("localhost", 6565))
				.block();
	}
	
	@Test
	public void fireAndForget() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Mono<Void> mono = this.rSocket.fireAndForget(payload);
		
		StepVerifier.create(mono)
			.verifyComplete();
	}
	
	@Test
	public void requestResponse() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Mono<ResponseDto> mono = this.rSocket.requestResponse(payload)
				.map(p -> ObjectUtil.toObject(p, ResponseDto.class))
				.doOnNext(System.out::println);
		
		StepVerifier.create(mono)
		.expectNextCount(1)
		.verifyComplete();
	}
	
	@Test
	public void requestStream() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Flux<ResponseDto> flux = this.rSocket.requestStream(payload)
				.map(p -> ObjectUtil.toObject(p, ResponseDto.class))
				.doOnNext(System.out::println)
				.take(4);
		
		StepVerifier.create(flux)
		.expectNextCount(4)
		.verifyComplete();
	}
	
	@Test
	public void requestChannel() {
		
		Flux<Payload> payloadFlux = Flux.range(-10, 21)
				.delayElements(Duration.ofMillis(500))
				.map(RequestDto::new)
				.map(ObjectUtil::toPayload);
		
		Flux<ChartResponseDto> flux = this.rSocket.requestChannel(payloadFlux)
				.map(p -> ObjectUtil.toObject(p, ChartResponseDto.class));
		
		StepVerifier.create(flux)
			.expectNextCount(21)
			.verifyComplete();
	}

}
