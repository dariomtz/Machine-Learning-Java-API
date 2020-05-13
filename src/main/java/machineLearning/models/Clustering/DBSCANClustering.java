package machineLearning.models.Clustering;

import machineLearning.algebra.Algebra;
import machineLearning.algebra.Matrix;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

public class DBSCANClustering extends Clustering {
    LinkedList<HashSet<Vector<Double>>>groups;
    int minPoints;
    double radius;

    public DBSCANClustering(Matrix data, int minPoints, double radius){
        super(data);
        setMinPoints(minPoints);
        setRadius(radius);
        train();
    }

    protected void setGroups(LinkedList<HashSet<Vector<Double>>> groups) {
        this.groups = groups;
    }

    public LinkedList<HashSet<Vector<Double>>> getGroups() {
        return groups;
    }

    public void setMinPoints(int minPoints) {
        if(minPoints<=0)throw new IllegalArgumentException("illegal minPoints: "+minPoints);
        this.minPoints = minPoints;
    }
    public int getMinPoints() {
        return minPoints;
    }

    public void setRadius(double radius) {
        if(radius<=0)throw new IllegalArgumentException("illegal radius: "+radius);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void train() {
        LinkedList<Vector<Double>> points = new LinkedList<>();
        for(int i = 0; i<data.rowSize; i++)points.add(data.getRow(i));
        LinkedList<HashSet<Vector<Double>>> groups = new LinkedList<>();
        LinkedList<Vector<Double>>searching = new LinkedList<>();
        while(!points.isEmpty()){
            searching.add(points.poll());
            HashSet<Vector<Double>>group = new HashSet<>();
            while(!searching.isEmpty()){
                Vector<Double> point = searching.poll();
                group.add(point);
                HashSet<Vector<Double>> neigbors = new HashSet<>();
                for(int i = 0; i<data.rowSize; i++){
                    Vector<Double> point2 = data.getRow(i);
                    if(point.equals(point2))continue;
                    if(Algebra.euclideanDistance(point,point2)<=radius) neigbors.add(point2);
                }
                if(neigbors.size()>=minPoints){
                    for (Vector<Double> point2 : neigbors)
                        if(group.add(point2))searching.add(point2);
                }
            }
            if(group.size() >1){
                groups.add(group);
                points.removeAll(group);
            }
        }
        setGroups(groups);
        Vector<Integer> classified = new Vector<>();
        for(Double group: classify(data).getCol(data.colSize))classified.add(group.intValue());
        setClassified(classified);

    }

    @Override
    public Vector<Double> classify(Vector<Double> input) {
        Vector<Double> vector = (Vector<Double>) input.clone();
        double groupNum = -1;
        int i = 0;
        for (HashSet<Vector<Double>>group : groups){
            if(groupNum != -1)break;
            for(Vector<Double> point : group){
                if(Algebra.euclideanDistance(point,input)<radius) {
                    groupNum = i;
                    break;
                }

            }
            i++;
        }
        vector.add(groupNum);
        return vector;
    }

    @Override
    public Matrix classify(Matrix input) {
        Matrix matrix = new Matrix(input.rowSize,input.colSize+1);
        for(int i = 0; i<matrix.rowSize;i++)matrix.setRow(i,classify(input.getRow(i)));
        return matrix;
    }
}