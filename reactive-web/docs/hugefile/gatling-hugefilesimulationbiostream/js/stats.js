var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "       60",
        "ok": "       46",
        "ko": "       14"
    },
    "minResponseTime": {
        "total": "   19,661",
        "ok": "   19,661",
        "ko": "  120,000"
    },
    "maxResponseTime": {
        "total": "  120,001",
        "ok": "  119,919",
        "ko": "  120,001"
    },
    "meanResponseTime": {
        "total": "   80,310",
        "ok": "   68,230",
        "ko": "  120,001"
    },
    "standardDeviation": {
        "total": "   36,202",
        "ok": "   32,925",
        "ko": "        0"
    },
    "percentiles1": {
        "total": "   92,367",
        "ok": "   69,606",
        "ko": "  120,001"
    },
    "percentiles2": {
        "total": "  119,919",
        "ok": "   94,524",
        "ko": "  120,001"
    },
    "percentiles3": {
        "total": "  120,001",
        "ok": "  119,695",
        "ko": "  120,001"
    },
    "percentiles4": {
        "total": "  120,001",
        "ok": "  119,919",
        "ko": "  120,001"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 46,
    "percentage": 76.66666666666667
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 14,
    "percentage": 23.333333333333332
},
    "meanNumberOfRequestsPerSecond": {
        "total": "      0.4",
        "ok": "     0.31",
        "ko": "     0.09"
    }
},
contents: {
"req_download-file-a--782551782": {
        type: "REQUEST",
        name: "Download file as bio stream",
path: "Download file as bio stream",
pathFormatted: "req_download-file-a--782551782",
stats: {
    "name": "Download file as bio stream",
    "numberOfRequests": {
        "total": "       60",
        "ok": "       46",
        "ko": "       14"
    },
    "minResponseTime": {
        "total": "   19,661",
        "ok": "   19,661",
        "ko": "  120,000"
    },
    "maxResponseTime": {
        "total": "  120,001",
        "ok": "  119,919",
        "ko": "  120,001"
    },
    "meanResponseTime": {
        "total": "   80,310",
        "ok": "   68,230",
        "ko": "  120,001"
    },
    "standardDeviation": {
        "total": "   36,202",
        "ok": "   32,925",
        "ko": "        0"
    },
    "percentiles1": {
        "total": "   92,367",
        "ok": "   69,606",
        "ko": "  120,001"
    },
    "percentiles2": {
        "total": "  119,919",
        "ok": "   94,524",
        "ko": "  120,001"
    },
    "percentiles3": {
        "total": "  120,001",
        "ok": "  119,695",
        "ko": "  120,001"
    },
    "percentiles4": {
        "total": "  120,001",
        "ok": "  119,919",
        "ko": "  120,001"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 46,
    "percentage": 76.66666666666667
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 14,
    "percentage": 23.333333333333332
},
    "meanNumberOfRequestsPerSecond": {
        "total": "      0.4",
        "ok": "     0.31",
        "ko": "     0.09"
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
