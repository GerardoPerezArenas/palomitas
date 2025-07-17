$(document).ready(function(){
    "use strict";    
    if ($('.jquery-tagIt').length) {
        $('.jquery-tagIt').tagit({
            availableTags: ["Tag 1", "Tag 2"]
        });
    }
});