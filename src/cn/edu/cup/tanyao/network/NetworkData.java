package cn.edu.cup.tanyao.network;

import Jama.Matrix;

/**
 * 整理管网计算中的数据
 * @author tanyao
 * @version 2018.09.13
 */
public class NetworkData {
    /**
     *
     */
    public Matrix simpleNodeLoad;

    /**
     * 节点关联矩阵
     */
    public Matrix branch;

    /**
     * 简化的节点关联矩阵
     */
    public Matrix simpleBranch;

    /**
     * 节点载荷
     */
    public Matrix nodeLoad;

    /**
     * 节点载荷状态
     */
    public int[] nodeLoadState;

    /**
     * 节点压力平方,MPa
     */
    public Matrix nodePressure;

    /**
     * 节点压力状态
     */
    public int[] nodePressureState;

    /**
     * 管段阻抗
     */
    public Matrix pipeImpedance;

    /**
     * 求组分压缩因子
     */
    public BwrsZ bwrs;

    /**
     * 管段高程差,m
     */
    public Matrix elevationDrop;

    public NetworkData() {}

    /**
     * 将一维数组转化为二维数组
     * @param array
     * @return
     */
    static double[][] getArray(double[] array) {
        double[][] doubleArray = new double[array.length][1];
        for(int i = 0; i < array.length; i++) {
            doubleArray[i][0] = array[i];
        }
        return doubleArray;
    }

    /**
     * 初始化数据
     */
    public void setData() {
        BranchMatrix B = new BranchMatrix();
        B.setData();
        this.branch = B.generateMatrix();
        this.simpleBranch = B.generateSimpleMatrix();

        NodeFlow N = new NodeFlow();
        N.setData();
        this.nodeLoad = new Matrix(getArray(N.nodeLoad)).times(1.0 / 24 / 3600);
        this.nodePressure = new Matrix(getArray(N.nodePressure)).times(1000000)
                .arrayTimesEquals(new Matrix(getArray(N.nodePressure)).times(1000000));
        this.nodeLoadState = N.nodeLoadState;
        this.nodePressureState = N.nodePressureState;
        this.simpleNodeLoad = new Matrix(getArray(N.simpleNodeLoad)).times(1.0 / 24 / 3600);

        GasProperty G = new GasProperty();
        G.setData();
        PipeData P = new PipeData();
        P.setData();

        double[][] impedance = new double[P.pipeLength.length][1];
        for(int i = 0; i < P.pipeLength.length; i++) {
            impedance[i][0] = Math.pow(StandardCondition.C0, 2)
                    * Math.pow(P.diameter[i], 5) / P.frictionFactor[i]
                    / G.relativeDensity / StandardCondition.temperature
                    / P.pipeLength[i];
        }
        pipeImpedance = new Matrix(impedance);

        //设定气体组分
        double[] com = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        BwrsZ bwrs = new BwrsZ(com);
        this.bwrs = bwrs;

        //设定管段高程差
        Elevation E = new Elevation();
        E.setData();
        this.elevationDrop = B.generateMatrix().transpose().times(new Matrix(getArray(E.elevation)));
    }

    public static void main(String[] args) {
        NetworkData net = new NetworkData();
        net.setData();
        net.elevationDrop.print(2, 2);
    }

}
