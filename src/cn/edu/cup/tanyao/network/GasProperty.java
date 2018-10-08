package cn.edu.cup.tanyao.network;

/**
 * 气体基本物性
 */
public class GasProperty implements StandardCondition{
    /**
     * 压缩因子
     */
    public double Z;

    /**
     * 气体的实际密度,kg/m³
     */
    public double density;

    /**
     * 气体的相对密度
     */
    public double relativeDensity;

    /**
     * 气体温度,K
     */
    private double temperature = 293.15;

    /**
     * 气体压力,MPa
     */
    private double pressure = 3;

    /**
     * 气体组分
     */
    private double[] component = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public void setData() {
        Bwrs A = new Bwrs(component);
        A.init(3001.325, temperature);
        this.Z = A.getZ();
        this.density = A.getRou_weight();
        relativeDensity = density / StandardCondition.airDensity;
    }

    public void setComponent(double[] component) {
        this.component = component;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public static void main(String[] args) {
        GasProperty G = new GasProperty();
        G.setData();
        System.out.println(G.Z);
        //System.out.println("密度：" + G.density + ",相对密度：" + G.relativeDensity);
    }
}
