package com.example.rsocket.service;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

public class MathService implements RSocket {
	
	@Override
	public Mono<Void> fireAndForget(Payload payload){
		System.out.println("Receiving : " + payload.getDataUtf8());
		return Mono.empty();
	}
}
