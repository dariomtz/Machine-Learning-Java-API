package machineLearning.algebra;

import java.util.Vector;

public class Matrix{
    protected Vector<Vector<Double>>Matrix;
    public final int rows, cols;

    public Matrix(int row, int col){
        if(row <= 0) throw new IllegalArgumentException("Illegal row size: "+row);
        if(col <= 0) throw new IllegalArgumentException("Illegal col size: "+col);
        this.cols = col;
        this.rows = row;
        Matrix = new Vector<>(row);
        Matrix.setSize(row);
        for (int i = 0; i<row; i++){
            Matrix.set(i,new Vector<>(col));
            Matrix.get(i).setSize(col);
        }
        for(int i = 0; i<row; i++){
            for(int j = 0; j<col; j++){
                set(i,j,0.0);
            }
        }
    }
    public Matrix(Vector<Vector<Double>> vector){
        this(vector.size(),vector.get(0).size());
        for(int i = 0; i< rows; i++){
            if (vector.get(i).size() != cols) throw new DifferentVectorSizeFoundException(cols, vector.get(i).size());
            for(int j = 0; j< cols; j++){
                if(vector.get(i).get(j) != null) set(i,j,vector.get(i).get(j));
                else set(i,j,0);
            }
        }
    }

    public Matrix(Matrix matrix){
        this(matrix.Matrix);
    }

    public <T extends Number>void set(int row, int col,  T value){
        if(row >= rows || row < 0 ){
            throw new IndexOutOfBoundsException("index out of range in row: " + row);
        }
        if( col >= cols || col < 0){
            throw new IndexOutOfBoundsException("index out of range in col: " + col);
        }
        Matrix.get(row).set(col,value.doubleValue());
    }

    public <T extends Number> void setRow(int row, Vector<T>vector){
        if(vector.size() != this.cols) {
            throw new DifferentVectorSizeFoundException(vector.size(),this.cols);
        }
        if(row >= rows || row < 0 ){
            throw  new IndexOutOfBoundsException("index out of range in row: " + row);
        }
        for(int i = 0; i< cols; i++){
            this.set(row,i,vector.get(i));
        }
    }
    public <T extends Number>void setCol(int col, Vector<T>vector){
        if(vector.size() != this.rows) {
            throw new DifferentVectorSizeFoundException(vector.size(),this.rows);
        }
        if(col >= cols || col < 0 ){
            throw new  IndexOutOfBoundsException("index out of range in col: " + col);
        }
        for(int i = 0; i< rows; i++){
            this.set(i,col,vector.get(i));
        }
    }
    public Double get(int row,int col){
        if(row >= rows || row < 0 ){
            throw new IndexOutOfBoundsException("index out of range in row: " + row);
        }
        if( col >= cols || col < 0){
            throw new IndexOutOfBoundsException("index out of range in col: " + col);
        }
        return this.Matrix.get(row).get(col);
    }
    public Vector<Double> getRow(int row){
        if(row >= rows || row < 0 ){
            throw  new IndexOutOfBoundsException("index out of range in row: " + row);
        }
        Vector<Double>vector = new Vector<>(cols);
        vector.setSize(cols);
        for(int i = 0; i < cols; i++)vector.set(i,get(row,i));
        return vector;
    }
    public Vector<Double> getCol(int col){
        if(col >= cols || col < 0 ){
            throw  new IndexOutOfBoundsException("index out of range in col: " + col);
        }
        Vector<Double>vector = new Vector<>(rows);
        vector.setSize(rows);
        for(int i = 0; i < rows; i++)vector.set(i,get(i,col));
        return vector;

    }
    public static Matrix sum(Matrix... matrices){
        int row = matrices[0].rows;
        int col = matrices[0].cols;
        for(Matrix m : matrices)if(m.rows != row || m.cols != col){
            throw new DifferentMatrixSizeFoundException(row,col,m.rows,m.rows);
        }
        Matrix matrix = new Matrix(row,col);
        for(int i = 0; i<col; i++) {
            for (int j = 0; j < row; j++) {
                Double sum = 0.0;
                for (Matrix m : matrices) sum += m.get(j, i);
                matrix.set(j, i, sum);
            }
        }
        return matrix;
    }

    public static Matrix sum(Matrix m, Vector<Double> v){
        if(v.size() != m.cols){
            throw new DifferentVectorSizeFoundException(v.size(),m.cols);
        }
        Matrix newMatrix = new Matrix(m.rows, m.cols);

        for (int i = 0; i < v.size(); i++) {
            newMatrix.setCol(i, Algebra.sum(m.getCol(i), v.get(i)));
        }
        return newMatrix;
    }

    public static Matrix subtract(Matrix a, Matrix b){
        if(a.cols != b.cols || a.rows != b.rows){
            throw new DifferentMatrixSizeFoundException(a.rows,a.cols,b.rows,b.cols);
        }//RETORNA UNA EXCEPTION
        Matrix matrix = new Matrix(a.rows,a.cols);
        for(int i = 0; i<a.cols; i++) {
            matrix.setCol(i,Algebra.subtract(a.getCol(i),b.getCol(i)));
        }
        return matrix;
    }

    public static Vector<Double> subtract(Vector<Double> v, Matrix m){
        if(v.size() != m.cols){
            throw new DifferentVectorSizeFoundException(v.size(),m.cols);
        }
        Vector<Double> sum = new Vector<>();
        for (int i = 0; i < v.size(); i++) {
            sum.add(Algebra.sum(m.getCol(i)));
        }
        return Algebra.subtract(v, sum);
    }

    public static Matrix multiplication(Matrix a, Matrix b){
        if(a.cols != b.rows){
            throw new IncompatibleMatrixSizeOperationException(a.cols,b.rows);
        }
        Matrix matrix = new Matrix(a.rows,b.cols);
        for(int i = 0; i <matrix.cols; i++){
            for(int j = 0; j < matrix.rows; j++) {
                matrix.set(j,i,Algebra.sum(Algebra.mult(a.getRow(j),b.getCol(i))));
            }
        }
        return matrix;
    }
    public static Matrix multiplication(Matrix matrix, Double scale){
        Matrix newMatrix = new Matrix(matrix);
        for(int i = 0; i<matrix.cols; i++){
            newMatrix.setCol(i,Algebra.mult(scale,matrix.getCol(i)));
        }
        return newMatrix;
    }
    public static Vector<Double> multiplication(Matrix matrix, Vector<Double> vector){
        if(vector.size() != matrix.cols){
            throw new DifferentVectorSizeFoundException(vector.size(),matrix.cols);
        }
        Matrix newMatrix = new Matrix(vector.size(),1);
        newMatrix.setCol(0,vector);
        return multiplication(matrix,newMatrix).getCol(0);
    }

    public static Vector<Double> multiplication(Vector<Double> v, Matrix m){
        if(v.size() != m.rows){
            throw new DifferentVectorSizeFoundException(v.size(),m.rows);
        }
        Matrix newMatrix = new Matrix(1,v.size());
        newMatrix.setRow(0,v);
        return multiplication(newMatrix,m).getRow(0);
    }

    public static Matrix transpose(Matrix matrix){
        Matrix newMatrix = new Matrix(matrix.cols,matrix.rows);
        for(int i = 0; i<newMatrix.cols; i++){
            newMatrix.setCol(i,matrix.getRow(i));
        }
        return newMatrix;
    }

    public static Matrix directMultiplication(Matrix a, Matrix b){
        if(a.cols != b.cols || a.rows != b.rows){
            throw new DifferentMatrixSizeFoundException(a.rows,a.cols,b.rows,b.cols);
        }
        Matrix newMatrix = new Matrix(a.rows, a.cols);
        for (int i = 0; i < newMatrix.rows; i++) {
            for (int j = 0; j < newMatrix.cols; j++) {
                newMatrix.set(i, j, a.get(i, j) * b.get(i, j));
            }
        }
        return newMatrix;
    }

    public static double mean(Matrix m){
        int q = 0;
        double sum = 0d;
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                sum += m.get(i, j);
                q++;
            }
        }
        return sum / q;
    }

    public static Matrix round(Matrix m){
        Matrix newMatrix = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                newMatrix.set(i, j, Math.round(m.get(i, j)));
            }
        }
        return newMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;
        Matrix matrix = (Matrix) o;
        return rows == matrix.rows &&
                cols == matrix.cols &&
                Matrix.equals(matrix.Matrix);
    }
    @Override
    public String toString(){
        return Matrix.toString();
    }

    @Override
    protected Object clone() {
        return new Matrix(this);
    }
}

