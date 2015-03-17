function deleteIssuePage(issuePageId){
		   $.ajax({
					url: "/magazine/deleteIssuePage/"+issuePageId ,
					success:function(data) {
						  window.location.reload(); 
					    }
					  });
	}

    function openPreview(issueId){
		var popup = window.open('/magazine/preview/'+issueId,'IssuePreview', 'menubar=no,location=no,resizable=yes,scrollbars=no,status=no' );
		popup.moveTo(0, 0);
		popup.resizeTo(screen.width, screen.height-50);
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
    
    function initShowAddIssueDate(){
    	    $( "#publishingDate" ).datepicker( { dateFormat: 'dd-mm-yy' } );
    }
