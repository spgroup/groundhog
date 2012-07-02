package br.cin.ufpe.epona.parser;

public class MutableInt {
	int value = 1;

	public void increment() {
		++value;
	}

	public int get() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}