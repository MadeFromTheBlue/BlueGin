package blue.made.bluegin.core.util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Vectornd implements Vector {
	public double[] vector;

	public Vectornd(double... vector) {
		this.vector = vector;
	}

	public Vectornd(Vector vector) {
		this.vector = new double[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			this.vector[i] = vector.get(i);
		}
	}

	public Vectornd(int size, double value) {
		this.vector = new double[size];
		this.fill(value);
	}

	public Vectornd(int size) {
		this.vector = new double[size];
	}

	@Override
	public int size() {
		return this.vector.length;
	}

	@Override
	public double get(int index) {
		if (index >= 0 && index < vector.length) {
			return this.vector[index];
		}
		return 0;
	}

	@Override
	public void set(int index, double value) {
		if (index >= 0 && index < vector.length) {
			this.vector[index] = value;
		}
	}

	@Override
	public double[] values() {
		double[] out = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			out[i] = vector[i];
		}
		return out;
	}

	@Override
	public double lengthSqr() {
		double sum = 0;
		for (int i = 0; i < vector.length; i++) {
			sum += vector[i] * vector[i];
		}
		return sum;
	}

	@Override
	public Vectornd clone() {
		return new Vectornd(this);
	}

	@Override
	public void copy(Vector from) {
		this.vector = new double[from.size()];
		for (int i = 0; i < from.size(); i++) {
			this.vector[i] = from.get(i);
		}
	}

	@Override
	public void add(Vector other) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] += other.get(i);
		}
	}

	@Override
	public void subtract(Vector other) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] -= other.get(i);
		}
	}

	@Override
	public void multiply(Vector other) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] *= other.get(i);
		}
	}

	@Override
	public void divide(Vector other) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] /= other.get(i);
		}
	}

	@Override
	public void add(double value) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] += value;
		}
	}

	@Override
	public void subtract(double value) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] -= value;
		}
	}

	@Override
	public void multiply(double value) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] *= value;
		}
	}

	@Override
	public void divide(double value) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] /= value;
		}
	}

	@Override
	public void fill(double value) {
		for (int i = 0; i < vector.length; i++) {
			this.vector[i] = value;
		}
	}

	@Override
	public DoubleBuffer dbuff() {
		return DoubleBuffer.wrap(this.vector);
	}

	@Override
	public FloatBuffer fbuff() {
		float[] arr = new float[this.vector.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (float) this.vector[i];
		}
		return FloatBuffer.wrap(arr);
	}
}
