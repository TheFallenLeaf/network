package cn.edu.cup.tanyao.network;

/**
 * 利用威莫斯公式计算紊流摩阻系数
 *
 * @author tanyao
 * @version 2018.08.21
 */
public class FrictionWeymouth {
    /**
     * 管道内径,m
     */
    public double diameter;

    /**
     * 构造方法
     * @param diameter 管径,m
     */
    public FrictionWeymouth(double diameter) {
        this.diameter = diameter;
    }

    /**
     * 空构造方法
     */
    public FrictionWeymouth() {}

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double calculateFrictionFactor() {
        return 0.009407 * Math.pow(diameter, -1.0 / 3.0);
    }
    public static void main(String[] args) {
        FrictionWeymouth f = new FrictionWeymouth(0.148);
        System.out.println(f.calculateFrictionFactor());
    }
}