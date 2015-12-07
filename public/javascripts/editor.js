$('[name="componentCreationItem"]').click(function() {
    var spl = $(this).attr('id').split('-');
    var id = spl[spl.length - 1];

    var urlSpl = location.pathname.split('/');
    var proj = urlSpl[urlSpl.length - 1];

    console.log('Do AJAX for component ' + id + ' and project ' + proj + '...');

    var xhr = getXMLHTTP();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            console.log(xhr.status);
            console.log(xhr.responseText);

            //  Change me!

            location.reload();
        }
    }

    var params = 'id=' + proj + '&componente=' + id;

    console.log('params: ' + params);

    xhr.open('POST', '/projecto/componente', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    xhr.send(params);
});

$("#save").click(function() {
    for (var idx in editors) {
        console.log('Editor contents: ' + editors[idx].value());
    }

    console.log('Now do something with this.');
});