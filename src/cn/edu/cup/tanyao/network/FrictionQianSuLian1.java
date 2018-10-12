package cn.edu.cup.tanyao.network;

/**
 * 前苏联近期计算摩阻公式
 * 适用于粗糙度在0.03mm左右
 */
public class FrictionQianSuLian1 {
    /**
     * 管道内径,m
     */
    public double diameter;

    /**
     * 管壁绝对粗糙度,mm
     */
    public double roughness;

    public FrictionQianSuLian1(double diameter, double roughness) {
        this.diameter = diameter;
        this.roughness = roughness;
    }

    public void setData(double diameter, double roughness) {
        this.roughness = roughness;
        this.diameter = diameter;
    }

    public double caculateFrictionQianSuLian() {
        return 0.067 * Math.pow(2 * roughness / diameter, 0.2);
    }
}
