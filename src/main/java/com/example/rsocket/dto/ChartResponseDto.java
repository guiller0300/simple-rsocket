package com.example.rsocket.dto;

public class ChartResponseDto {
	
	private int input;
	private int output;

	public ChartResponseDto() {
		
	}
	
	public ChartResponseDto(int input, int output) {
		super();
		this.input = input;
		this.output = output;
	}
	
	public int getInput() {
		return input;
	}

	public void setInput(int input) {
		this.input = input;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}
	
	@Override
	public String toString() {
		String graphFormat = getFormat(this.output);
		return String.format(graphFormat, this.input, 'X');
	}

	private String getFormat(int value) {
		return "%3s|%" + value + "s";
	}
}
