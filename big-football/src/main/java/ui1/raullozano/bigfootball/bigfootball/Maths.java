package ui1.raullozano.bigfootball.bigfootball;

public class Maths {
    public static double round(double value, int nDecimals) {
        return Math.round(value * Math.pow(10, nDecimals)) / Math.pow(10, nDecimals);
    }
}
