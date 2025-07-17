$(document).ready(function () {
    "use strict";
    if ($('.email-to').length) {

        var isTagIt = ($.isFunction("tagit")) ? 1 : 0;

        if (isTagIt) {
            $('.email-to').tagit({
                availableTags: ["Tag 1", "Tag 2"]
            });
        }
    }

    if ((".wysihtml5").length) {
        $('.wysihtml5').wysihtml5();
    }
});