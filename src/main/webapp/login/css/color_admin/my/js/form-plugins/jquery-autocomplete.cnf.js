$(document).ready(function () {
    var availableTags = [
        'Tag 1',
        'Tag 2'
    ];

    if ($('.jquery-autocomplete').length) {
        $('.jquery-autocomplete').autocomplete({
            source: availableTags
        });
    }
});