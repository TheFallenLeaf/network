package cn.edu.cup.tanyao.network;

import Jama.Matrix;

/**
 * 节点关联矩阵
 * @author tanyao
 * @version 2018.09.18
 */
public class BranchMatrix {
    /**
     * 管段起点编号
     */
    private int beginNumber[];

    /**
     * 管段终点编号
     */
    private int endNumber[];

    /**
     * 节点压力状态
     */
    private int pressureState[];

    /**
     * 从Excel读入数据
     */
    public void setData() {
        int[] begin = new int[PutIn.getRowCount(1) - 1];
        int[] end = new int[PutIn.getRowCount(1) - 1];
        int[] pressureState = new int[PutIn.getRowCount(2) - 1];
        for(int i = 0; i < begin.length; i++) {
            begin[i] = (int)PutIn.getElement(i + 2, 2, 1);
            end[i] = (int)PutIn.getElement(i + 2, 3, 1);
        }
        for(int i = 0; i < pressureState.length; i++) {
            pressureState[i] = (int)PutIn.getElement(i + 2, 3, 2);
        }
        this.beginNumber = begin;
        this.endNumber = end;
        this.pressureState = pressureState;
    }

    /**
     * 生成节点关联矩阵
     * @return
     */
    public Matrix generateMatrix() {
        return new Matrix(generateArray(this.beginNumber, this.endNumber));
    }

    /**
     * 生成节点关联数组
     * @param beginNumber
     * @param endNumber
     * @return 二维节点关联数组
     */
    private static double[][] generateArray(int[] beginNumber, int[] endNumber) {
        int rowNumber, columnNumber;
        rowNumber = PutIn.getRowCount(2) - 1;
        columnNumber = PutIn.getRowCount(1) - 1;

        double[][] pipe = new double[rowNumber][columnNumber];
        for(int i = 0; i < columnNumber; i++) {
            pipe[beginNumber[i]-1][i] = -1;
            pipe[endNumber[i]-1][i] = 1;
        }
        return pipe;
    }

    /**
     * 生成简化的节点关联矩阵
     * @return
     */
    public Matrix generateSimpleMatrix() {
        int count = 0;
        for(int i = 0; i < this.pressureState.length; i++) {
            count += this.pressureState[i];
        }

        double[][] pipe = generateArray(this.beginNumber, this.endNumber);
        double[][] simplePipe = new double[pipe.length - count][pipe[0].length];

        for(int i = 0, k = 0; i < this.pressureState.length; i++) {
            if(this.pressureState[i] == 0) {
                simplePipe[k] = pipe[i].clone();
                k++;
            }
        }

        return new Matrix(simplePipe);
    }

    public static void main(String[] args) {
        BranchMatrix B = new BranchMatrix();
        B.setData();
        B.generateMatrix().print(2, 0);
        B.generateSimpleMatrix().print(2, 0);
    }
}