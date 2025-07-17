$(document).ready(function () {
    "use strict";
    if ($('.rangeSlider').length) {
        $('.rangeSlider').ionRangeSlider();
    }

    if ($('.advance_rangeSlider').length) {
        $('.advance_rangeSlider').ionRangeSlider({
            min: 0,
            max: 5000,
            from: 30000,
            to: 90000,
            type: 'double',
            step: 500,
            prefix: "$",
            postfix: " €",
            maxPostfix: "+",
            prettify: false,
            hasGrid: true,
            values: [
                'January', 'February', 'March',
                'April', 'May', 'June',
                'July', 'August', 'September',
                'October', 'November', 'December'
            ]
        });
    }
});