package machineLearning.models.Clustering;

import java.util.Objects;
import java.util.Vector;

public class Cluster {
    Vector<Double> center;
    Cluster(Vector<Double>center){
        this.center = center;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return Objects.equals(center, cluster.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center);
    }

    @Override
    public String toString() {
        return center.toString();
    }
}