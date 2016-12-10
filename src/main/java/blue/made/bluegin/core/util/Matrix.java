package blue.made.bluegin.core.util;

public interface Matrix {
	int rows();

	int columns();

	double get(int row, int column);

	void set(int row, int column, double value);

	double[][] values();

	double[] row(int row);

	default Vector rowVec(int row) {
		return new Vectornd(this.row(row));
	}

	double[] column(int column);

	default Vector columnVec(int column) {
		return new Vectornd(this.column(column));
	}

	Matrix transpose();

	default boolean isSquare() {
		return this.rows() == this.columns();
	}

	Matrix part(int row0, int row1, int column0, int column1);

	void copyPart(int torow0, int torow1, int tocolumn0, int tocolumn1, int fromrow, int fromcolumn, Matrix from);

	double det();

	Matrix inverse();

	Vector transform(Vector in);

	Matrix transform(Matrix in);

	static Matrix composition(Matrix a, Matrix b) {
		return b.transform(a);
	}
}
