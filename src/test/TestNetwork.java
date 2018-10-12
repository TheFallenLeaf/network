package test;

import Jama.Matrix;
import cn.edu.cup.tanyao.network.*;

/**
 * 枝状管网计算
 * @author tanyao
 * @version 2018.09.13
 */
public class TestNetwork {

    /**
     * 管网数据
     */
    public NetworkData net;

    public void setData() {
        this.net = new NetworkData();
        net.setData();
    }

    /**
     * 将一维列矩阵转化为二维数组
     * @param matrix 一维列矩阵
     * @return
     */
    private static double[][] getArray(Matrix matrix) {
        double[][] temp = new double[matrix.getRowDimension()][1];
        for(int i = 0; i < matrix.getRowDimension(); i++) {
            temp[i][0] = matrix.getMatrix(i, i, 0, 0).det();
        }
        return temp;
    }

    /**
     * 计算管段的压缩因子
     * @param net
     * @return
     */
    private static Matrix getZ(NetworkData net) {
        Matrix var1 = net.branch.transpose().times(net.nodePressure); //P1²-P2²
        Matrix var2 = net.branch.transpose().times(getActualPressure(net.nodePressure)); //P1-P2
        Matrix pressure = var2.arrayLeftDivide(var1).times(0.5); //管段的平均压力(P1²-P2²)/(P1-P2)/2
        double[][] var3 = getArray(pressure);
        double[][] var4 = new double[var3.length][1];
        for(int i = 0; i < var3.length; i++) {
            net.bwrs.setData(var3[i][0]/1000, 293.15);
            var4[i][0] = net.bwrs.getZ();
        }
        return new Matrix(var4);
    }

    /**
     * 计算方程的函数值
     * @param network
     * @return
     */
    private static Matrix functionValue(NetworkData network) {
        Matrix temp = network.pipeImpedance.arrayRightDivide(getZ(network));
        Matrix temp0 = network.branch.transpose().times(network.nodePressure).arrayTimes(temp);
        double[][] temp1 = temp0.getArray();

        for(int i = 0; i < temp1.length; i++) {
            temp1[i][0] = Math.abs(temp1[i][0]) / temp1[i][0] * Math.sqrt(Math.abs(temp1[i][0]));
        }
        temp0 = new Matrix(temp1).copy();
        return network.simpleBranch.times(temp0).minus(network.simpleNodeLoad);
    }

    public static Matrix jacobi(NetworkData network) {
        int n = network.simpleNodeLoad.getRowDimension();
        Matrix jacobi = new Matrix(n, n);

        //以差分代替微分，计算雅各比矩阵
        int count = 0;
        double dx = 0.001;
        Matrix fx = functionValue(network);         //fx
        for(int i = 0; i < network.nodePressureState.length; i++) {
            if(network.nodePressureState[i] == 0) {
                //变量加dx
                network.nodePressure.set(i, 0, network.nodePressure.getMatrix(i, i, 0, 0).det() + dx);
                Matrix fx1 = functionValue(network);
                Matrix J = fx1.minus(fx).times(1.0 / dx);

                jacobi.setMatrix(0, n - 1,new int[]{count},J);

                count ++;

                if(count > n) {
                    System.out.println("雅各比矩阵计算出错");

                }
                network.nodePressure.set(i, 0, network.nodePressure.getMatrix(i, i, 0, 0).det() - dx);

            }
        }
        return jacobi;
    }

    /**
     * 计算实际压力
     * @param nodePressure
     * @return
     */
    public static Matrix getActualPressure(Matrix nodePressure) {
        double[][] pressure = getArray(nodePressure);
        for(int i = 0; i < pressure.length; i++) {
            pressure[i][0] = Math.sqrt(pressure[i][0]) / 1000000;
        }
        return new Matrix(pressure);
    }



    public static void main(String[] args) {
        TestNetwork T = new TestNetwork();
        T.setData();

        Matrix dy;
        int i;
        for(i = 0; i < 500; i++) {
            //计算迭代增量
            try{
                dy = jacobi(T.net).solve(functionValue(T.net).times(-1));
            } catch(Exception e) {

                System.out.println("第" + i + "次计算出错");
                e.printStackTrace();
                break;
            }

            //改变变量的值
            int count = 0;
            for(int j = 0; j < T.net.nodeLoad.getRowDimension(); j++) {
                if(T.net.nodePressureState[j] == 0) {
                    T.net.nodePressure.set(j, 0, T.net.nodePressure.getMatrix(j, j, 0, 0).det()
                            + dy.getMatrix(count, count,0,0).det());
                    count ++;
                }
            }

            if(dy.normInf() < 1) {
                break;
            }

            if(i > 495) {
                System.out.println("计算不收敛");
                break;
            }

        }

        System.out.println("迭代次数：" + i);
        //计算流量
        Matrix Flow = T.net.branch.transpose().times(T.net.nodePressure).arrayTimes(T.net.pipeImpedance.arrayRightDivide(getZ(T.net)));
        double[][] q = getArray(Flow);
        for(int k = 0; k < q.length; k++) {
            q[k][0] = Math.sqrt(Math.abs(q[k][0]));
        }
        Flow = new Matrix(q);
        //输出节点载荷
        T.net.branch.times(Flow).times(24 * 3600).print(5, 0);
        //输出压力
        getActualPressure(T.net.nodePressure).print(1, 5);
    }
}
