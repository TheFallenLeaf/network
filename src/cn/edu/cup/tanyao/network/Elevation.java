package cn.edu.cup.tanyao.network;

/**
 * 节点海拔
 * @author tanyao
 * @version 2018.10.04
 */
public class Elevation {
    /**
     * 节点海拔,m
     */
    public double[] elevation;

    public void setData() {
        double[] elevation = new double[PutIn.getRowCount(2) - 1];
        for(int i = 0; i < elevation.length; i++) {
            elevation[i] = PutIn.getElement(2 + i, 6, 2);
        }
        this.elevation = elevation;

    }

    public static void main(String[] args) {
        Elevation E = new Elevation();
        E.setData();
        for(int i = 0; i < E.elevation.length; i++) {
            System.out.println(E.elevation[i]);
        }
    }
}
