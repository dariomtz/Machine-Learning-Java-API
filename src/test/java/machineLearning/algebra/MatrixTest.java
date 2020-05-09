package machineLearning.algebra;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Vector;

public class MatrixTest {

    @Test
    public void testConstructorMethods() throws Exception{

        //Basic constructor
        Matrix m1 = new Matrix(4,3);
        Assert.assertEquals("The rowSize must be 4",4,m1.rowSize);
        Assert.assertEquals("The colSize must be 3",3,m1.colSize);

        //Copy constructor
        Matrix m2 = new Matrix(m1);
        Assert.assertEquals("The rowSize must be the same than in matrix",m1.rowSize,m2.rowSize);
        Assert.assertEquals("The colSize must be the same than in matrix",m1.colSize,m2.colSize);

        //Vector constructor
        Vector<Vector<Double>>v1 = new Vector<>(6);
        v1.setSize(6);
        for(int i = 0; i < v1.size(); i++){
            v1.set(i,new Vector<>(40));
            v1.get(i).setSize(40);
        }
        Matrix m3 = new Matrix(v1);
        Assert.assertEquals("The rowSize must be 40",40,m3.rowSize);
        Assert.assertEquals("The colSize must be 6",6,m3.colSize);


        //Basic constructor Exception LessThanMinimumSizeForMatrix
        boolean correct = false;
        Matrix m4;
        try{
            m4 = new Matrix(0,3);
        }catch (Exception ex){
            m4 = null;
            if(ex instanceof LessThanMinimumSizeForMatrix)
                correct = true;
        }
        Assert.assertTrue("The LessThanMinimumSizeForMatrix Exception caused by row value",correct);

        correct = false;
        try{
            m4 = new Matrix(3,-1);
        }catch (Exception ex){
            m4 = null;
            if(ex instanceof LessThanMinimumSizeForMatrix)
                correct = true;
        }
        Assert.assertTrue("The LessThanMinimumSizeForMatrix Exception caused by col value",correct);

        //Vector constructor Exception LessThanMinimumSizeForMatrix
        correct = false;
        Vector<Vector<Double>> v2 = new Vector<>();
        try{
            m4 = new Matrix(v2);
        }catch (Exception ex){
            m4 = null;
            if(ex instanceof LessThanMinimumSizeForMatrix)
                correct = true;
        }
        Assert.assertTrue("The LessThanMinimumSizeForMatrix Exception caused by empty vector col value",correct);

        correct = false;
        v2.add(new Vector<>());
        try{
            m4 = new Matrix(v2);
        }catch (Exception ex){
            m4 = null;
            if(ex instanceof LessThanMinimumSizeForMatrix)
                correct = true;
        }
        Assert.assertTrue("The LessThanMinimumSizeForMatrix Exception caused by empty vector row value",correct);

        correct = false;
        v2.get(0).setSize(10);
        v2.add(new Vector<>());
        v2.get(1).setSize(7);
        try{
            m4 = new Matrix(v2);
        }catch (Exception ex){
            m4 = null;
            if(ex instanceof DifferentVectorSizeFound)
                correct = true;
        }
        Assert.assertTrue("The DifferentVectorSizeFound Exception is showed for invalid vector",correct);



    }

    @Test
    public void testSetsGetsMethods() throws Exception{
        Matrix m1 = new Matrix(4,5);
        m1.set(0,0,43.8);
        Assert.assertEquals("The value in <0,0> must be 43.8",(Double) 43.8,m1.get(0,0));

        Vector<Double> v1= new Vector<>(5);
        Collections.addAll(v1,0.0,1.0,2.0,3.0,4.0);
        m1.setRow(1,v1);
        Assert.assertEquals("The values in row 1 must be <0,1,2,3,4>",v1,m1.getRow(1));

        v1.setSize(4);
        m1.setCol(3,v1);
        Assert.assertEquals("The values in col 3 must be <0,1,2,3>",v1,m1.getCol(3));

        //OutOfRangeMatrixPosition exception
        boolean correct = false;
        try{
            m1.set(5,0,4440.444);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixPosition is showed in set for row access",correct);

        correct = false;
        try{
            m1.set(0,10,40.4);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixPosition is showed in set for col access",correct);

        correct = false;
        try{
            m1.get(5,0);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixPosition is showed in get for row access",correct);

        correct = false;
        try{
            m1.get(0,10);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixPosition is showed in get for col access",correct);

        // DifferentVectorSizeFound exceptions
        correct = false;
        v1.setSize(27);
        try{
            m1.setRow(0,v1);
        }catch (Exception ex){
            if(ex instanceof DifferentVectorSizeFound)
                correct = true;
        }
        Assert.assertTrue("The DifferentVectorSizeFound is showed in setRow",correct);

        correct = false;
        try{
            m1.setCol(0,v1);
        }catch (Exception ex){
            if(ex instanceof DifferentVectorSizeFound)
                correct = true;
        }
        Assert.assertTrue("The DifferentVectorSizeFound is showed in setCol",correct);

        //OutOfRangeMatrixColPosition exceptions
        correct = false;
        v1.setSize(5);
        try{
            m1.setRow(27,v1);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixRowPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixRowPosition is showed in setRow",correct);

        correct = false;
        v1.setSize(4);
        try{
            m1.setCol(459,v1);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixColPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixColPosition is showed in setCol",correct);

        correct = false;
        try{
            m1.getRow(27);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixRowPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixColPosition is showed in getRow",correct);

        correct = false;
        try{
            m1.getCol(459);
        }catch (Exception ex){
            if(ex instanceof OutOfRangeMatrixColPosition)
                correct = true;
        }
        Assert.assertTrue("The OutOfRangeMatrixColPosition is showed in getCol",correct);
    }

    @Test
    public void testSumMethod() throws Exception{
        Matrix m1 = new Matrix(4,5);
        Matrix m2 = new Matrix(4,5);
        double a = 0;
        for(int i = 0; i< m1.rowSize;i++){
            for(int j = 0; j<m1.colSize; j++){
                m1.set(i,j,a);
                m2.set(i,j,0.0);
                a++;
            }
        }
        Matrix m3 = Matrix.sum(m1,m2,m2,m2,m2);
        Assert.assertEquals("The matrix product of the sum is equal to m1",m1.Matrix,m3.Matrix);

        //DifferentMatrixSizeFound Exception
        boolean correct = false;
        Matrix m4 = new Matrix(3,3);
        try{
            Matrix.sum(m1,m2,m2,m4,m2);
        }catch (Exception ex){
            if(ex instanceof DifferentMatrixSizeFound)
                correct =true;
        }
        Assert.assertTrue("The DifferentMatrixSizeFound is showed in sum",correct);
    }

    @Test
    public void testSubtractMethod() throws Exception{
        Matrix m1 = new Matrix(4,5);
        Matrix m2 = new Matrix(4,5);
        double a = 0;
        for(int i = 0; i< m1.rowSize;i++){
            for(int j = 0; j<m1.colSize; j++){
                m1.set(i,j,a);
                m2.set(i,j,0.0);
                a++;
            }
        }
        Matrix m3 = Matrix.subtract(m1,m2);
        Assert.assertEquals("The subtraction of m1-m2 must be m1",m1.Matrix,m3.Matrix);
        Matrix m4 = Matrix.subtract(m2,m1);
        Assert.assertEquals("The subtraction of m2-(m2-m1) must be m1",m1.Matrix,Matrix.subtract(m2,m4).Matrix);

        boolean correct = false;
        Matrix m5 = new Matrix(10,10);
        try{
            Matrix.subtract(m1,m5);
        }catch (Exception ex){
            if(ex instanceof DifferentMatrixSizeFound)
                correct =true;
        }
        Assert.assertTrue("The DifferentMatrixSizeFound is showed in subtract",correct);
    }
}