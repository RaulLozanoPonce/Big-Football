var yMin;
var yMax;
if(::yAxisMin:: == null) {
    yMin = null;
} else {
    yMin = ::yAxisMin::.value;
}
if(::yAxisMax:: == null) {
    yMax = null;
} else {
    yMax = ::yAxisMax::.value;
}

Highcharts.setOptions({
    lang: {
      decimalPoint: ',',
      thousandsSep: '.'
    }
});

Highcharts.chart(::container::, {
    chart: {
        type: ::type::
    },
    title: {
        text: ::title::
    },
    xAxis: {
        categories: ::xAxisValues::,
        title: {
            text: ::xAxisUnit::
        },
        crosshairs: true,
        tickInterval: ::tickInterval::
    },
    yAxis: {
        title: {
            text: ::unit::
        },
        min: yMin,
        max: yMax
    },
    series : ::series::,
    tooltip: {
        shared: true,
        valueDecimals: ::decimals::,
        ::custom-tooltip::
    },
    legend: {
        enabled: false
    },
    credits: {
        enabled: false
    },
    plotOptions: {
        column: {
            stacking: 'normal',
            pointPadding: 0,
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                inside: true,
                format: '{y:.::decimals::f}'
            }
        },
        areaspline: {
            fillOpacity: 0.3
        },
        pie: {
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
            }
        },
        gauge: {
            dataLabels: {
                enabled: true,
                format: '{y:.2f}'
            }
        },
        area: {
            stacking: 'normal'
        },
        scatter: {
            marker: {
                radius: 2.5,
                symbol: 'circle',
                states: {
                    hover: {
                        enabled: true,
                        lineColor: 'rgb(100,100,100)'
                    }
                }
            },
            states: {
                hover: {
                    marker: {
                        enabled: false
                    }
                }
            },
            jitter: {
                x: 0.005
            }
        }
    }
});