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
		//min not max because once one vector ends, the default zeros will prevent the other vector from making any change
		int size = Math.min(a.size(), b.size());
		double sum = 0;
		for (int i = 0; i < size; i++)
		{
			sum += a.get(i) * b.get(i);
		}
		return sum;
	}
}
