var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "       60",
        "ok": "       60",
        "ko": "        0"
    },
    "minResponseTime": {
        "total": "      546",
        "ok": "      546",
        "ko": "        -"
    },
    "maxResponseTime": {
        "total": "    1,351",
        "ok": "    1,351",
        "ko": "        -"
    },
    "meanResponseTime": {
        "total": "      761",
        "ok": "      761",
        "ko": "        -"
    },
    "standardDeviation": {
        "total": "      126",
        "ok": "      126",
        "ko": "        -"
    },
    "percentiles1": {
        "total": "      731",
        "ok": "      731",
        "ko": "        -"
    },
    "percentiles2": {
        "total": "      827",
        "ok": "      827",
        "ko": "        -"
    },
    "percentiles3": {
        "total": "      972",
        "ok": "      972",
        "ko": "        -"
    },
    "percentiles4": {
        "total": "    1,351",
        "ok": "    1,351",
        "ko": "        -"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 39,
    "percentage": 65.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 20,
    "percentage": 33.33333333333333
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 1.6666666666666667
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "     1.94",
        "ok": "     1.94",
        "ko": "        -"
    }
},
contents: {
"req_download-file-a-1154253454": {
        type: "REQUEST",
        name: "Download file as nio stream",
path: "Download file as nio stream",
pathFormatted: "req_download-file-a-1154253454",
stats: {
    "name": "Download file as nio stream",
    "numberOfRequests": {
        "total": "       60",
        "ok": "       60",
        "ko": "        0"
    },
    "minResponseTime": {
        "total": "      546",
        "ok": "      546",
        "ko": "        -"
    },
    "maxResponseTime": {
        "total": "    1,351",
        "ok": "    1,351",
        "ko": "        -"
    },
    "meanResponseTime": {
        "total": "      761",
        "ok": "      761",
        "ko": "        -"
    },
    "standardDeviation": {
        "total": "      126",
        "ok": "      126",
        "ko": "        -"
    },
    "percentiles1": {
        "total": "      731",
        "ok": "      731",
        "ko": "        -"
    },
    "percentiles2": {
        "total": "      827",
        "ok": "      827",
        "ko": "        -"
    },
    "percentiles3": {
        "total": "      972",
        "ok": "      972",
        "ko": "        -"
    },
    "percentiles4": {
        "total": "    1,351",
        "ok": "    1,351",
        "ko": "        -"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 39,
    "percentage": 65.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 20,
    "percentage": 33.33333333333333
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 1,
    "percentage": 1.6666666666666667
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "     1.94",
        "ok": "     1.94",
        "ko": "        -"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
