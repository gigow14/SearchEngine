$(document).ready(function() {
    $("#upload-file-input").on("change", uploadFile);
});

// Send file via AJAX and Spring Boot Server
function uploadFile() {
    $.ajax({
        url: "/uploadFile",
        type: "POST",
        data: new FormData($("#upload-file-form")[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            // Handle upload success
            $("#upload-file-message").text("File succesfully uploaded");
        },
        error: function () {
            // Handle upload error
            $("#upload-file-message").text(
                "File not uploaded (perhaps it's too much big)");
        }
    });
} // function uploadFile