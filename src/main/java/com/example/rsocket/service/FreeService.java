package com.example.rsocket.service;

import org.reactivestreams.Publisher;

import io.rsocket.Payload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FreeService extends MathService {
	
	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		// TODO Auto-generated method stub
		return Mono.empty();
	}
	
	@Override
	public Mono<Payload> requestResponse(Payload payload) {
		// TODO Auto-generated method stub
		return Mono.empty();
	}
	
	@Override
	public Flux<Payload> requestStream(Payload payload) {
		// TODO Auto-generated method stub
		return super.requestStream(payload).take(3);
	}
	
	@Override
	public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
		// TODO Auto-generated method stub
		return super.requestChannel(payloads).take(3);
	}

}
