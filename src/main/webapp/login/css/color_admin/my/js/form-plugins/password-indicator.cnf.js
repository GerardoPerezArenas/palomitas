$(document).ready(function () {
    "use strict";
    if ($('.password-indicator-default').length) {
        $('.password-indicator-default').passwordStrength();
    }
    if ($('.password-indicator-visible').length) {
        $('.password-indicator-visible').passwordStrength({targetDiv: '#passwordStrengthDiv2'});
    }
});