package cn.edu.cup.tanyao.network;

/**
 * 根据BWRS方程求天然气物性参数
 *
 * @author tanyao
 * @version 2018.09.29
 */
public class BwrsZ {

    /**
     * 压缩因子
     */
    private double Z;

    /**
     * 摩尔密度，kmol/m3
     */
    private double rou_mole;

    /**
     * 气体组分，总和等于1，分别为C1, C2, C3, iC4, nC4, iC5, nC5, C6, N2, CO2
     */
    private double[] component;

    /**
     * 混合物的11个参数
     */
    private Parameter p;

    /**
     * 气体常数,kJ/(kmol·K)
     */
    private final double R = 8.3143;

    /**
     * 混合物压力,kPa
     */
    public double pressure;

    /**
     * 混合物温度,K
     */
    public double temperature;

    /**
     * 构造函数
     *
     * @param component
     *            组分数组， 组分分别为C1, C2, C3, iC4, nC4, iC5, nC5, C6, N2, CO2，总和等于1
     */
    public BwrsZ(double[] component) {
        this.component = component;
    }

    /**
     * 空构造方法
     */
    public BwrsZ() {}

    /**
     * 初始化数据
     * @param pressure 压力,kPa
     * @param temperature 温度,K
     */
    public void setData(double pressure, double temperature) {
        Parameter p = new Parameter();
        p.setData(this.component);
        this.p = p;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public void setData(double pressure, double temperature,
                        double[] component) {
        Parameter p = new Parameter();
        p.setData(component);
        this.pressure = pressure;
        this.temperature = temperature;
        this.component = component;
    }

    public double getZ() {

        //正割法求组分密度
        double Rou_0;
        double Rou_1 = 0;// 两个初始密度，kmol/m3
        rou_mole = pressure / R / temperature;// 摩尔密度，kmol/m3
        do {
            Rou_0 = Rou_1;
            Rou_1 = rou_mole;
            double f0 = Rou_0
                    * R
                    * temperature
                    + (p.B * R * temperature - p.A - p.C / Math.pow(temperature, 2)
                    + p.D / Math.pow(temperature, 3) - p.E
                    / Math.pow(temperature, 4)) * Math.pow(Rou_0, 2)
                    + (p.b * R * temperature - p.a - p.d / temperature)
                    * Math.pow(Rou_0, 3) + p.rfa * (p.a + p.d / temperature)
                    * Math.pow(Rou_0, 6) + p.c * Math.pow(Rou_0, 3)
                    / Math.pow(temperature, 2)
                    * (1 + p.gama * Math.pow(Rou_0, 2))
                    * Math.exp(-p.gama * Math.pow(Rou_0, 2)) - pressure;
            double f1 = Rou_1
                    * R
                    * temperature
                    + (p.B * R * temperature - p.A - p.C / Math.pow(temperature, 2)
                    + p.D / Math.pow(temperature, 3) - p.E
                    / Math.pow(temperature, 4)) * Math.pow(Rou_1, 2)
                    + (p.b * R * temperature - p.a - p.d / temperature)
                    * Math.pow(Rou_1, 3) + p.rfa * (p.a + p.d / temperature)
                    * Math.pow(Rou_1, 6) + p.c * Math.pow(Rou_1, 3)
                    / Math.pow(temperature, 2)
                    * (1 + p.gama * Math.pow(Rou_1, 2))
                    * Math.exp(-p.gama * Math.pow(Rou_1, 2)) - pressure;
            rou_mole = (Rou_0 * f1 - Rou_1 * f0) / (f1 - f0);
        } while (Math.abs(rou_mole - Rou_1) > 0.01);

        Z = 1
                + (p.B - p.A / R / temperature - p.C / R / Math.pow(temperature, 3)
                + p.D / R / Math.pow(temperature, 4) - p.E / R
                / Math.pow(temperature, 5)) * rou_mole
                + (p.b - p.a / R / temperature - p.d / R / temperature / temperature)
                * Math.pow(rou_mole, 2) + p.rfa / R / temperature
                * (p.a + p.d / temperature) * Math.pow(rou_mole, 5) + p.c
                * Math.pow(rou_mole, 2) / R / Math.pow(temperature, 3)
                * (1 + p.gama * Math.pow(rou_mole, 2))
                * Math.exp(-p.gama * Math.pow(rou_mole, 2));
        return Z;
    }

    public static void main(String[] args) {
        double[] com = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        BwrsZ A = new BwrsZ(com);
        A.setData(3000.25, 293.15);
        System.out.println(A.getZ());
    }
}
