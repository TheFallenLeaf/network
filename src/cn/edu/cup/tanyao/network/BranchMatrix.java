package cn.edu.cup.tanyao.network;

import Jama.Matrix;

/**
 * 生成节点关联矩阵
 *
 * @author tanyao
 * @version 2018.08.22
 */
public class BranchMatrix {
    /**
     * 管段起点编号
     */
    private static int beginNumber[];

    /**
     * 管段终点编号
     */
    private static int endNumber[];

    /**
     * 构造方法
     */
    public BranchMatrix() {
        int[] begin = new int[PutIn.getRowCount(1) - 1];
        int[] end = new int[PutIn.getRowCount(1) - 1];
        for(int i = 0; i < begin.length; i++) {
            begin[i] = (int)PutIn.getElement(i + 2, 2, 1);
            end[i] = (int)PutIn.getElement(i + 2, 3, 1);
        }
        this.beginNumber = begin;
        this.endNumber = end;
    }

    /**
     *求一个数组最大的元素
     * @param max
     * @return
     */
    private static int max(int[] max) {
        int value = 0;
        for(int i = 0; i < max.length; i++) {
            if(max[i] > value) {
                value = max[i];
            }
        }
        return value;
    }

    /**
     * 生成节点关联矩阵数组
     * @return 二维节点关联数组
     */
    public static double[][] generateArray() {
        //分析节点关联矩阵的行数和列数
        int lineNumber, columnNumber;
        lineNumber = max(beginNumber);
        if(max(endNumber) > max(beginNumber)) {
            lineNumber = max(endNumber);
        }
        columnNumber = beginNumber.length;

        //生成节点关联数组
        double[][] pipe = new double[lineNumber][columnNumber];
        for(int i = 0; i < columnNumber; i++) {
            pipe[beginNumber[i]-1][i] = -1;
            pipe[endNumber[i]-1][i] = 1;
        }
        return pipe;
    }

    /**
     * 根据节点关联数组生成节点关联矩阵
     * @return 节点关联矩阵
     */
    public Matrix generateMatrix() {
        return new Matrix(generateArray());
    }

    /**
     * 判断给定的节点是否已知压力
     * @param a 给定的节点编号
     * @param b 已知压力的节点编号数组
     * @return
     */
    public static boolean belong(int a, int[] b) {
        boolean belong = false;
        for(int i = 0; i < b.length; i++) {
            if(b[i] == a) {
                belong = true;
            }
        }
        return belong;
    }

    /**
     * 根据节点关联数组和节点压力
     * 是否已知生成简化的节点关联矩阵
     * @param known 存放已知压力的节点编号
     * @return 简化的节点关联矩阵
     */
    public Matrix generateMatrix(int[] known) {
        double[][] pipe = generateArray();
        double[][] simplePipe = new double[pipe.length - known.length][pipe[0].length];

        int count = 0;
        for(int i = 0; i < pipe.length; i++) {
            if(belong(i, known) == false) {
                System.arraycopy(pipe, i, simplePipe, count, 1);
                count++;
            }
        }
        return new Matrix(simplePipe);
    }

    /**
     * 读取压力已知的节点编号
     * @return 压力已知的节点编号数组
     */
    public static int[] getKnown() {
        int count = 0;
        int[] knownPressure = new int[PutIn.getRowCount(2) - 1];
        for(int i = 0; i < knownPressure.length; i++) {
            knownPressure[i] = (int)PutIn.getElement(2 + i, 3, 2);
            count += knownPressure[i];
        }
        int[] known = new int[count];
        for(int i = 0, j = 0; i < knownPressure.length; i++) {
            if(knownPressure[i] == 1) {
                known[j] = i;
                j++;
            }
        }
        return known;
    }

    public static void main(String[] args) {
        System.out.println("节点关联矩阵：");
        BranchMatrix matrix = new BranchMatrix();
        matrix.generateMatrix().print(2, 0);
        matrix.generateMatrix().transpose().print(2, 0);
        /*
        System.out.println("简化的节点关联矩阵：");
        BranchMatrix simpleMatrix = new BranchMatrix(begin, end);
        simpleMatrix.generateMatrix(getKnown()).print(2, 0);
        */

    }
}

