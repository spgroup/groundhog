package br.cin.ufpe.groundhog.parser;

public class MutableInt {
	int value;
	
	public MutableInt() {
		value = 1;
	}
	
	public MutableInt(int value) {
		this.value = value;
	}

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