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

    $("#fullPageLoading").show();
});

$("#save").click(function() {
    var urlSpl = location.pathname.split('/');

    var proj = 0;

    if (urlSpl[urlSpl.length - 2] == 'version')
        proj = urlSpl[urlSpl.length - 3];
    else
        proj = urlSpl[urlSpl.length - 1];

    var xhr = getXMLHTTP();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            console.log(xhr.status);
            console.log(xhr.responseText);

            console.log("Name and description changed!");

            var componentObj = {
                id: proj
            };

            for (var idx in editors)
                componentObj[editors[idx].element.id] = editors[idx].value();

            console.log(componentObj);

            var xhr2 = getXMLHTTP();

            xhr2.open('POST', '/projecto/editar', true);
            xhr2.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

            xhr2.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    console.log("We good? We good.");

                    $("#fullPageLoading").hide();

                    location.href = '/editor/' + proj;
                }
            }

            xhr2.send($.param(componentObj));
        }
    }

    xhr.open('POST', '/projecto/nome', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var nameParams = $.param({
        id: proj,
        nome: $("#title").val(),
        descricao: $("#description").val()
    });

    xhr.send(nameParams);

    $("#fullPageLoading").show();
});

$("#add-tag").click(function() {
    console.log("Yeah!");

    var urlSpl = location.pathname.split('/');
    var proj = urlSpl[urlSpl.length - 1];

    tags.push($("#new-tag").val());

    var xhr = getXMLHTTP();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            console.log("Done!");

            $("#tags-group").append('<span class="label label-default" id="span-' + $("#new-tag").val() + '">' + $("#new-tag").val() + '&nbsp;&nbsp;<a href="#" id="tag-' + $("#new-tag").val() + '" name="tag-remove"><span aria-hidden="true">&times;</span></a></span>&nbsp;');

            $("#fullPageLoading").hide();
        }
    }

    xhr.open('POST', '/projecto/tag', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var params = $.param({
        project: proj,
        tags: JSON.stringify(tags)
    });

    console.log("Tags: " + JSON.stringify(tags));

    xhr.send(params);

    $("#fullPageLoading").show();

    return false;
});

$(document).on('click', '[name="tag-remove"]', function() {
    var urlSpl = location.pathname.split('/');
    var proj = urlSpl[urlSpl.length - 1];

    var spl = $(this).attr('id').split('-');
    var tagName = spl[spl.length - 1];

    var index = tags.indexOf(tagName);

    tags.splice(index, 1);

    var xhr = getXMLHTTP();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            console.log("Done!");

            $("#span-" + $("#new-tag").val()).remove();

            $("#fullPageLoading").hide();
        }
    }

    xhr.open('POST', '/projecto/tag', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var params = $.param({
        project: proj,
        tags: JSON.stringify(tags)
    });

    xhr.send(params);

    $("#fullPageLoading").show();

    return false;
});


$(document).on('click', '[name="file-remove"]', function() {
    var spl = $(this).attr('id').split('-');
    var fileID = spl[spl.length - 1];


    var xhr = getXMLHTTP();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            console.log("Removing file ID: " + fileID);
            $("#para-"+fileID).remove();
            $("#fullPageLoading").hide();
        }
    }

    xhr.open('get', '/ficheiro/remover/' + fileID, true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send();

    $("#fullPageLoading").show();

    return false;
});

$(document).on('change', '.btn-file :file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

