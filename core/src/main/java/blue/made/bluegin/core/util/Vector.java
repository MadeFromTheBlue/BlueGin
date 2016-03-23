package blue.made.bluegin.core.util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Vector extends Cloneable {
	int size();

	double get(int index);

	void set(int index, double value);

	double[] values();

	double lengthSqr();

	default double length() {
		return Math.sqrt(this.lengthSqr());
	}

	default double normalize() {
		return this.scaleToLength(1);
	}

	default double scaleToLength(double length) {
		double old = this.length();
		this.multiply(length / old);
		return old;
	}

	Vector clone();

	void copy(Vector from);

	void add(Vector other);

	void subtract(Vector other);

	void multiply(Vector other);

	void divide(Vector other);

	void add(double value);

	void subtract(double value);

	void multiply(double value);

	void divide(double value);

	void fill(double value);

	DoubleBuffer dbuff();

	FloatBuffer fbuff();

	static double dot(Vector a, Vector b) {
		//min not max because once one vector ends, the default zeros will prevent the other vector from making any change to the sum
		int size = Math.min(a.size(), b.size());
		double sum = 0;
		for (int i = 0; i < size; i++) {
			sum += a.get(i) * b.get(i);
		}
		return sum;
	}

	static Vector3d cross(Vector a, Vector b) {
		Vector3d out = new Vector3d();

		out.x += a.get(1) * b.get(2);
		out.x -= a.get(2) * b.get(1);

		out.y -= a.get(0) * b.get(2);
		out.y += a.get(2) * b.get(0);

		out.z += a.get(0) * b.get(1);
		out.z -= a.get(1) * b.get(0);

		return out;
	}

	static Vector add(Vector a, Vector b) {
		Vector out = a.clone();
		out.add(b);
		return out;
	}

	static Vector subtract(Vector a, Vector b) {
		Vector out = a.clone();
		out.subtract(b);
		return out;
	}

	static Vector multiply(Vector a, Vector b) {
		Vector out = a.clone();
		out.multiply(b);
		return out;
	}

	static Vector divide(Vector a, Vector b) {
		Vector out = a.clone();
		out.divide(b);
		return out;
	}

	static Vector mix(Vector a, Vector b, double factor) {
		return mix(a, 1.0 - factor, b, factor);
	}

	static Vector mix(Vector a, double acoefficient, Vector b, double bcoefficient) {
		Vector A = a.clone();
		Vector B = b.clone();
		A.multiply(acoefficient);
		B.multiply(bcoefficient);
		A.add(B);
		return A;
	}
}
