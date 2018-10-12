package cn.edu.cup.tanyao.network;

/**
 * 前苏联近期计算摩阻公式
 * 适用于粗糙度大于0.04mm
 */
public class FrictionQianSuLian0 {
    /**
     * 管道内径,m
     */
    public double diameter;

    /**
     * 管壁绝对粗糙度,mm
     */
    public double roughness;

    public FrictionQianSuLian0() {}

    /**
     * 构造方法
     * @param diameter 管径,m
     * @param roughness 管壁粗糙度mm
     */
    public FrictionQianSuLian0(double diameter, double roughness) {
        this.diameter = diameter;
        this.roughness = roughness;
    }

    public void setData(double diameter, double roughness) {
        this.roughness = roughness;
        this.diameter = diameter;
    }

    public double calculateFrictionQianSuLian() {
        return 0.383 / Math.pow(diameter / 2 / roughness, 0.4);
    }

    public static void main(String[] args) {
        FrictionQianSuLian0 f = new FrictionQianSuLian0();
        f.setData(0.1, 0.03);
        System.out.println(f.calculateFrictionQianSuLian());
    }
}
