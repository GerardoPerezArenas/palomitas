$(document).ready(function() {
    "use strict";
    if ($('.table.responsive').length) {
        $('.table.responsive').DataTable({
            responsive: true,
            "info": false,
            "ordering": false,
            "paging": false,
            "searching": false
        });
    }
});