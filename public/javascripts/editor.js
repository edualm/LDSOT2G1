$('[name="componentCreationItem"]').click(function() {
    var spl = $(this).attr('id').split('-');
    var id = spl[spl.length - 1];

    var urlSpl = location.pathname.split('/');
    var proj = urlSpl[urlSpl.length - 1];

    console.log('Do AJAX for component ' + id + ' and project ' + proj + '...');
});