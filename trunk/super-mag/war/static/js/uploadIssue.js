function deleteIssuePage(issuePageId){
		   $.ajax({
					url: "/magazine/deleteIssuePage/"+issuePageId ,
					success:function(data) {
						  window.location.reload(); 
					    }
					  });
	}

    function openPreview(issueId){
		window.open('/magazine/preview/'+issueId,'Issue Preview','height=600px,width=800px').focus();
    }
    
    function showImagePreviewDiv(imgUrl){
    	window.open(imgUrl,'Image Preview','height=600px,width=800px').focus();
    }
    
    function uploadFile() {
        if (window.File && window.FileList) {
            var fd = new FormData();
            var files = document.getElementById('fileToUpload').files;
            for ( var i = 0; i < files.length; i++) {
                fd.append("file" + i, files[i]);
            }
            var xhr = new XMLHttpRequest();
            xhr.open("POST", document.getElementById('uploadForm').action);
            xhr.send(fd);

            alert('already saved');
            document.getElementById('uploadForm').value = '';

        } else {
            document.getElementById('uploadForm').submit(); //no html5
        }
    }
