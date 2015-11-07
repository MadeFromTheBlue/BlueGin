package blue.made.bluegin.core.util;

public interface Vector extends Cloneable
{
	public int size();
	
	public double get(int index);
	
	public void set(int index, double value);
	
	public double[] values();
	
	public double lengthSqr();
	
	public default double length()
	{
		return Math.sqrt(this.lengthSqr());
	}
	
	public default double normalize()
	{
		return this.scaleToLength(1);
	}
	
	public default double scaleToLength(double length)
	{
		double old = this.length();
		this.multiply(length / old);
		return old;
	}
	
	public Vector clone();
	
	public void copy(Vector from);
	
	public void add(Vector other);
	
	public void subtract(Vector other);
	
	public void multiply(Vector other);
	
	public void divide(Vector other);
	
	public void add(double value);
	
	public void subtract(double value);
	
	public void multiply(double value);
	
	public void divide(double value);
	
	public void fill(double value);
	
	public static double dot(Vector a, Vector b)
	{
		//min not max because once one vector ends, the default zeros will prevent the other vector from making any change to the sum
		int size = Math.min(a.size(), b.size());
		double sum = 0;
		for (int i = 0; i < size; i++)
		{
			sum += a.get(i) * b.get(i);
		}
		return sum;
	}
	
	public static Vector cross(Vector a, Vector b)
	{
		Vectornd out = new Vectornd(Math.max(a.size(), b.size()));
		
		return out;
	}
	
	public static Vector add(Vector a, Vector b)
	{
		Vector out = a.clone();
		out.add(b);
		return out;
	}
	
	public static Vector subtract(Vector a, Vector b)
	{
		Vector out = a.clone();
		out.subtract(b);
		return out;
	}
	
	public static Vector multiply(Vector a, Vector b)
	{
		Vector out = a.clone();
		out.multiply(b);
		return out;
	}
	
	public static Vector divide(Vector a, Vector b)
	{
		Vector out = a.clone();
		out.divide(b);
		return out;
	}
	
	public static Vector mix(Vector a, Vector b, double factor)
	{
		return mix(a, 1.0 - factor, b, factor);
	}
	
	public static Vector mix(Vector a, double acoefficient, Vector b, double bcoefficient)
	{
		Vector A = a.clone();
		Vector B = b.clone();
		A.multiply(acoefficient);
		B.multiply(bcoefficient);
		A.add(B);
		return A;
	}
}
