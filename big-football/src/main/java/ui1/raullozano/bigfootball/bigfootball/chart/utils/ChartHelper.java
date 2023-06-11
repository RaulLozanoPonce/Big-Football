package ui1.raullozano.bigfootball.bigfootball.chart.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class ChartHelper {

    public static String getGraph(Class clazz, String container, ChartInfo info) {

        InputStream resource = clazz.getResourceAsStream("/chart.js");
        if(resource != null) {
            try {
                byte[] bytes = resource.readAllBytes();
                return "<script>" + fillFields(new String(bytes), container, info) + "</script>";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "<script></script>";
    }

    private static String fillFields(String html, String container, ChartInfo info) {

        String result = html + "";

        result = result.replaceAll("::container::", "\"" + container + "\"");

        result = result.replaceAll("::series::", new Gson().toJson(info.series()));
        result = result.replaceAll("::tickInterval::", new Gson().toJson(info.tickInterval()));
        result = result.replaceAll("::title::", new Gson().toJson(info.title()));
        result = result.replaceAll("::type::", new Gson().toJson(info.type()));
        result = result.replaceAll("::unit::", new Gson().toJson(info.unit()));
        result = result.replaceAll("::xAxisUnit::", new Gson().toJson(info.xAxisUnit()));
        result = result.replaceAll("::xAxisValues::", new Gson().toJson(info.xAxisValues()));
        result = result.replaceAll("::yAxisMin::", new Gson().toJson(info.yAxisMin()));
        result = result.replaceAll("::yAxisMax::", new Gson().toJson(info.yAxisMax()));
        result = result.replaceAll("::decimals::", new Gson().toJson(info.decimals()));
        result = result.replaceAll("::custom-tooltip::", tooltipOf(info));

        return result;
    }

    private static String tooltipOf(ChartInfo info) {
        switch (info.type()) {
            case "scatter":
                return "pointFormat: '" + info.xAxisUnit() + ": {point.x} <br/> " + info.unit() + ": {point.y}'";
            case "pie":
                return "pointFormat: '" + info.unit() + ": {point.y}'";
            default:
                return "valueSuffix: ' ' + " + new Gson().toJson(info.unit());
        }
    }
}
