function searchPhrase() {
    var input = $('#currentPhrase').val();
    input = input.trim();
    document.getElementById('SearchResult').style.display = "block";
    console.log(input);
    var currentURL = 'http://localhost:8080/searchApi?phrase='+input;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : currentURL,
        dataType : "json",
        success : function(data) {
            console.log(data[0].fileName);
            $("#searchDataTable").empty();
            var htmlColumnName = "<thead><tr><th>Filename</th><th>Number of Matches</th><th>Score</th><th>Query Counter</th><th>Last Modified</th><th>Download</th>";
            htmlColumnName += "</tr></thead>";
            $("#searchDataTable").append(htmlColumnName);

            var dataLength= data.length;
            var htmlTableContent = "<tbody>";
            for (var index = 0; index < dataLength; index++) {
                htmlTableContent += "<tr>";
                htmlTableContent += "<td>" + data[index].fileName + "</td>"
                htmlTableContent += "<td>" + String(data[index].score) + "</td>"
                htmlTableContent += "<td>" + data[index].fragment + "</td>";
                htmlTableContent += "<td>" + String(data[index].queryCounter) + "</td>";
                htmlTableContent += "<td>" + data[index].lastModified + "</td>";
                htmlTableContent += "<td>" + '<a href="download/'+data[index].fileName+'">' + '<img src="../images/download.png" height="30" width="30" />' + "</a>" + "</td>";
                htmlTableContent += "</tr>";
            }
            htmlTableContent += "</tbody>";
            $("#searchDataTable").append(htmlTableContent);
        }
    });

}

function downloadFile()
{

}