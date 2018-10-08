package cn.edu.cup.tanyao.network;

/**
 * 常数类
 * @author tanyao
 * @version 2018.09.12
 */
public interface StandardCondition {
    /**
     * 标况温度,k
     */
    double temperature = 293.15;

    /**
     * 标况压力,Pa
     */
    double pressure = 101325;

    /**
     * 标况空气密度
     */
    double airDensity = 1.205;

    /**
     * 管段特性方程常数
     */
    double C0 = 0.03848;

}
