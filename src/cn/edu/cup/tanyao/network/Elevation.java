package cn.edu.cup.tanyao.network;

import Jama.Matrix;

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
        BranchMatrix branch = new BranchMatrix();
        branch.setData();
        Elevation E = new Elevation();
        E.setData();
        Matrix ele = new Matrix(NetworkData.getArray(E.elevation));
        branch.generateMatrix().transpose().times(ele).print(2, 1);
    }
}
