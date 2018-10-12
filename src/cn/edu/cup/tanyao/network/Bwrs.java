package cn.edu.cup.tanyao.network;

/**
 * 根据Bwrs状态方程求气体物性参数
 *
 * @author cheng
 */
public class Bwrs {

    /**
     * 压缩因子
     */
    private double Z;

    /**
     * 组分的摩尔质量，kg/kmol
     */
    private double M;

    /**
     * 摩尔密度，kmol/m3
     */
    private double rou_mole;

    /**
     * 质量密度，kg/m3
     */
    private double rou_weight;

    /**
     * 低压定压比热，kJ/(kmol.K)
     */
    private double cp_low;

    /**
     * 低压定容比热，kJ/(kmol.K)
     */
    private double cv_low;

    /**
     * 高压定压比热，kJ/(kmol.K)
     */
    private double cp_high;

    /**
     * 高压定容比热，kJ/(kmol.K)
     */
    private double cv_high;

    /**
     * 节流效应系数，K/MPa
     */
    private double Di;

    /**
     * 体积绝热指数
     */
    private double kv;

    /**
     * 温度绝热指数
     */
    private double kt;

    /**
     * 气体组分，总和等于1，分别为C1, C2, C3, iC4, nC4, iC5, nC5, C6, N2, CO2
     */
    private double[] component;

    /**
     * 构造函数
     *
     * @param component
     *            组分数组， 组分分别为C1, C2, C3, iC4, nC4, iC5, nC5, C6, N2, CO2，总和等于1
     */
    public Bwrs(double[] component) {
        this.component = component;
    }

    /**
     * 对类进行初始化，求解在指定温度压力下的气体物性参数
     *
     * @param pressure
     *            压力，kPa
     *
     * @param temperature
     *            温度，K
     */
    public void init(double pressure, double temperature)// 输出结果
    {
        double R = 8.3143;// 气体常数，kJ/(kmol.K);
        // 通用常数A，B值
        double A1 = 0.443690, B1 = 0.115449;
        double A2 = 1.28438, B2 = -0.920731;
        double A3 = 0.356306, B3 = 1.70871;
        double A4 = 0.544979, B4 = -0.270896;
        double A5 = 0.528629, B5 = 0.349261;
        double A6 = 0.484011, B6 = 0.754130;
        double A7 = 0.0705233, B7 = -0.044448;
        double A8 = 0.5040870, B8 = 1.32245;
        double A9 = 0.0307452, B9 = 0.179433;
        double A10 = 0.0732828, B10 = 0.463492;
        double A11 = 0.0064500, B11 = -0.022143;

        double Tc[] = { 190.69, 305.38, 369.89, 408.13, 425.18, 460.37, 469.49,
                507.28, 126.15, 304.09 };// 临界温度，K
        double rou_c[] = { 10.05, 6.7566, 4.9994, 3.8012, 3.9213, 3.2469,
                3.2149, 2.7167, 11.099, 10.638 };// 临界密度，kmol/m3
        double w[] = { 0.013, 0.1018, 0.157, 0.183, 0.197, 0.226, 0.252, 0.302,
                0.035, 0.21 };// 偏心因子
        double M0[] = { 16.042, 30.068, 44.094, 58.12, 58.12, 72.146, 72.146,
                86.172, 28.016, 44.01 };// 相对分子质量

        // 纯组分的11个系数
        double A0[] = new double[10];
        double B0[] = new double[10];
        double C0[] = new double[10];
        double D0[] = new double[10];
        double E0[] = new double[10];
        double a0[] = new double[10];
        double b0[] = new double[10];
        double c0[] = new double[10];
        double d0[] = new double[10];
        double rfa0[] = new double[10];
        double gama0[] = new double[10];

        for (int i = 0; i < 10; i++) {
            B0[i] = (A1 + B1 * w[i]) / rou_c[i];
            A0[i] = (A2 + B2 * w[i]) * R * Tc[i] / rou_c[i];
            C0[i] = (A3 + B3 * w[i]) * R * Math.pow(Tc[i], 3) / rou_c[i];
            gama0[i] = (A4 + B4 * w[i]) / Math.pow(rou_c[i], 2);
            b0[i] = (A5 + B5 * w[i]) / Math.pow(rou_c[i], 2);
            a0[i] = (A6 + B6 * w[i]) * R * Tc[i] / Math.pow(rou_c[i], 2);
            rfa0[i] = (A7 + B7 * w[i]) / Math.pow(rou_c[i], 3);
            c0[i] = (A8 + B8 * w[i]) * R * Math.pow(Tc[i], 3)
                    / Math.pow(rou_c[i], 2);
            D0[i] = (A9 + B9 * w[i]) * R * Math.pow(Tc[i], 4) / rou_c[i];
            d0[i] = (A10 + B10 * w[i]) * R * Math.pow(Tc[i], 2)
                    / Math.pow(rou_c[i], 2);
            E0[i] = (A11 + B11 * w[i] * Math.exp(-3.8 * w[i])) * R
                    * Math.pow(Tc[i], 5) / rou_c[i];
        }
        // 二元交互作用系数
        double k[][] = {
                { 0, 0.01, 0.023, 0.0275, 0.031, 0.036, 0.041, 0.05, 0.025, 0.05 },
                { 0, 0, 0.0031, 0.004, 0.0045, 0.005, 0.006, 0.007, 0.07, 0.048 },
                { 0, 0, 0, 0.003, 0.0035, 0.004, 0.0045, 0.005, 0.1, 0.045 },
                { 0, 0, 0, 0, 0, 0.008, 0.001, 0.0015, 0.11, 0.05 },
                { 0, 0, 0, 0, 0, 0.008, 0.001, 0.0015, 0.12, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.134, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.148, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.172, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };// 0.008有待考证
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                k[i][j] = k[j][i];
            }
        }

        M = 0;// 混合物的摩尔质量，kg/kmol
        for (int i = 0; i < 10; i++) {
            M += M0[i] * component[i];
        }

        // 求混合物的11个系数
        double A = 0;
        double B = 0;
        double C = 0;
        double D = 0;
        double E = 0;
        double a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        double rfa = 0;
        double gama = 0;

        for (int i = 0; i < 10; i++) {
            B += component[i] * B0[i];
            gama += component[i] * Math.cbrt(gama0[i]);
            b += component[i] * Math.cbrt(b0[i]);
            a += component[i] * Math.cbrt(a0[i]);
            rfa += component[i] * Math.cbrt(rfa0[i]);
            c += component[i] * Math.cbrt(c0[i]);
            d += component[i] * Math.cbrt(d0[i]);
            for (int j = 0; j < 10; j++) {
                A += component[i] * component[j] * Math.sqrt(A0[i])
                        * Math.sqrt(A0[j]) * (1 - k[i][j]);
                C += component[i] * component[j] * Math.sqrt(C0[i])
                        * Math.sqrt(C0[j]) * Math.pow((1 - k[i][j]), 3);
                D += component[i] * component[j] * Math.sqrt(D0[i])
                        * Math.sqrt(D0[j]) * Math.pow((1 - k[i][j]), 4);
                E += component[i] * component[j] * Math.sqrt(E0[i])
                        * Math.sqrt(E0[j]) * Math.pow((1 - k[i][j]), 5);
            }
        }

        gama = Math.pow(gama, 3);
        b = Math.pow(b, 3);
        a = Math.pow(a, 3);
        rfa = Math.pow(rfa, 3);
        c = Math.pow(c, 3);
        d = Math.pow(d, 3);

        // 用正割法求组分密度
        double Rou_0;
        double Rou_1 = 0;// 两个初始密度，kmol/m3
        rou_mole = pressure / R / temperature;// 摩尔密度，kmol/m3
        do {
            Rou_0 = Rou_1;
            Rou_1 = rou_mole;
            double f0 = Rou_0
                    * R
                    * temperature
                    + (B * R * temperature - A - C / Math.pow(temperature, 2)
                    + D / Math.pow(temperature, 3) - E
                    / Math.pow(temperature, 4)) * Math.pow(Rou_0, 2)
                    + (b * R * temperature - a - d / temperature)
                    * Math.pow(Rou_0, 3) + rfa * (a + d / temperature)
                    * Math.pow(Rou_0, 6) + c * Math.pow(Rou_0, 3)
                    / Math.pow(temperature, 2)
                    * (1 + gama * Math.pow(Rou_0, 2))
                    * Math.exp(-gama * Math.pow(Rou_0, 2)) - pressure;
            double f1 = Rou_1
                    * R
                    * temperature
                    + (B * R * temperature - A - C / Math.pow(temperature, 2)
                    + D / Math.pow(temperature, 3) - E
                    / Math.pow(temperature, 4)) * Math.pow(Rou_1, 2)
                    + (b * R * temperature - a - d / temperature)
                    * Math.pow(Rou_1, 3) + rfa * (a + d / temperature)
                    * Math.pow(Rou_1, 6) + c * Math.pow(Rou_1, 3)
                    / Math.pow(temperature, 2)
                    * (1 + gama * Math.pow(Rou_1, 2))
                    * Math.exp(-gama * Math.pow(Rou_1, 2)) - pressure;
            rou_mole = (Rou_0 * f1 - Rou_1 * f0) / (f1 - f0);
        } while (Math.abs(rou_mole - Rou_1) > 0.01);
        rou_weight = rou_mole * M;
        // 压缩因子
        Z = 1
                + (B - A / R / temperature - C / R / Math.pow(temperature, 3)
                + D / R / Math.pow(temperature, 4) - E / R
                / Math.pow(temperature, 5)) * rou_mole
                + (b - a / R / temperature - d / R / temperature / temperature)
                * Math.pow(rou_mole, 2) + rfa / R / temperature
                * (a + d / temperature) * Math.pow(rou_mole, 5) + c
                * Math.pow(rou_mole, 2) / R / Math.pow(temperature, 3)
                * (1 + gama * Math.pow(rou_mole, 2))
                * Math.exp(-gama * Math.pow(rou_mole, 2));
        // 求比热
        cp_low = 0;
        // 压力对密度和温度的偏导数
        double p_rou = R
                * temperature
                + 2
                * (B * R * temperature - A - C / Math.pow(temperature, 2) + D
                / Math.pow(temperature, 3) - E
                / Math.pow(temperature, 4))
                * rou_mole
                + 3
                * (b * R * temperature - a - d / temperature)
                * Math.pow(rou_mole, 2)
                + 6
                * rfa
                * (a + d / temperature)
                * Math.pow(rou_mole, 5)
                + 3
                * c
                * Math.pow(rou_mole, 2)
                / Math.pow(temperature, 2)
                * (1 + gama * Math.pow(rou_mole, 2) - 2.0 / 3.0 * gama * gama
                * Math.pow(rou_mole, 4))
                * Math.exp(-gama * Math.pow(rou_mole, 2));
        double p_T = rou_mole
                * R
                + (B * R + 2 * C / Math.pow(temperature, 3) - 3 * D
                / Math.pow(temperature, 4) + 4 * E
                / Math.pow(temperature, 5)) * Math.pow(rou_mole, 2)
                + (b * R + d / temperature / temperature)
                * Math.pow(rou_mole, 3) - rfa * d * Math.pow(rou_mole, 6)
                / temperature / temperature - 2 * c * Math.pow(rou_mole, 3)
                / Math.pow(temperature, 3) * (1 + gama * Math.pow(rou_mole, 2))
                * Math.exp(-gama * Math.pow(rou_mole, 2));
        // 求解比热的常数值
        double AA[][] = {
                { 2.39359, -22.18007 * Math.pow(10, -4),
                        57.4022 * Math.pow(10, -7),
                        -372.7905 * Math.pow(10, -11),
                        35.49085 * Math.pow(10, -14) },
                { 1.10899, -1.88512 * Math.pow(10, -4),
                        39.6558 * Math.pow(10, -7),
                        -314.0209 * Math.pow(10, -11),
                        80.08189 * Math.pow(10, -14) },
                { 0.72265, 7.08716 * Math.pow(10, -4),
                        29.23895 * Math.pow(10, -7),
                        -261.5071 * Math.pow(10, -11),
                        70.00448 * Math.pow(10, -14) },
                { 0.19545, 25.23143 * Math.pow(10, -4),
                        1.95651 * Math.pow(10, -7),
                        -77.26149 * Math.pow(10, -11),
                        23.86087 * Math.pow(10, -14) },
                { 0.4127, 20.28601 * Math.pow(10, -4),
                        7.02953 * Math.pow(10, -7),
                        -102.5871 * Math.pow(10, -11),
                        28.83394 * Math.pow(10, -14) },
                { -0.1319, 35.41155 * Math.pow(10, -4),
                        -13.33225 * Math.pow(10, -7),
                        25.14633 * Math.pow(10, -11),
                        -1.29589 * Math.pow(10, -14) },
                { -0.0117, 33.16498 * Math.pow(10, -4),
                        -11.7051 * Math.pow(10, -7),
                        19.96476 * Math.pow(10, -11),
                        -0.86652 * Math.pow(10, -14) },
                { 0.95923, -6.14724 * Math.pow(10, -4),
                        61.42103 * Math.pow(10, -7),
                        -616.0952 * Math.pow(10, -11),
                        208.6819 * Math.pow(10, -14) },
                { 1.06849, -1.34096 * Math.pow(10, -4),
                        2.15569 * Math.pow(10, -7),
                        -7.86319 * Math.pow(10, -11),
                        0.69851 * Math.pow(10, -14) },
                { 0.47911, 7.62159 * Math.pow(10, -4),
                        -3.59392 * Math.pow(10, -7),
                        8.47438 * Math.pow(10, -11),
                        -0.57752 * Math.pow(10, -14) } };
        // AA[1][4]有待考证，35.49085 * Math.pow(10, -14)
        for (int i = 0; i < 10; i++) {
            cp_low += component[i]
                    * ((AA[i][0] + 2 * AA[i][1] * temperature + 3 * AA[i][2]
                    * temperature * temperature + 4 * AA[i][3]
                    * Math.pow(temperature, 3) + 5 * AA[i][4]
                    * Math.pow(temperature, 4)) * M0[i]);
        }
        cv_low = cp_low - R;
        cv_high = cv_low
                + (6 * C / Math.pow(temperature, 3) - 12 * D
                / Math.pow(temperature, 4) + 20 * E
                / Math.pow(temperature, 5))
                * rou_mole
                + d
                / Math.pow(temperature, 2)
                * Math.pow(rou_mole, 2)
                - 2
                * rfa
                * d
                / 5
                / Math.pow(temperature, 2)
                * Math.pow(rou_mole, 5)
                + 3
                * c
                / gama
                / Math.pow(temperature, 3)
                * ((gama * Math.pow(rou_mole, 2) + 2)
                * Math.exp(-gama * rou_mole * rou_mole) - 2);
        cp_high = cv_high + temperature / rou_mole / rou_mole * p_T * p_T
                / p_rou;
        // 求节流效应系数Di, K/MPa
        Di = 1.0
                / cp_high
                * (temperature / rou_mole / rou_mole * p_T / p_rou - 1.0 / rou_mole)
                * 1000;
        // 求绝热指数
        kv = rou_mole / pressure * cp_high / cv_high * p_rou;
        kt = 1.0 / (1 - pressure / rou_mole / rou_mole / cp_high * p_T / p_rou);
    }

    public double getZ() {
        return Z;
    }

    public double getM() {
        return M;
    }

    public double getRou_mole() {
        return rou_mole;
    }

    public double getRou_weight() {
        return rou_weight;
    }

    public double getCp_low() {
        return cp_low;
    }

    public double getCp_high() {
        return cp_high;
    }

    public double getCv_low() {
        return cv_low;
    }

    public double getCv_high() {
        return cv_high;
    }

    public double getDi() {
        return Di;
    }

    public double getKt() {
        return kt;
    }

    public double getKv() {
        return kv;
    }

    public void setComponent(double[] c) {
        component = c;
    }
    public static void main(String[] args){
        double[] com = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Bwrs A = new Bwrs(com);
        A.init(101.325, 293.15);
        System.out.println(A.getRou_weight());

    }
}
