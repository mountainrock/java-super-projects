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
    
    function initShowAddIssueDate(){
    	    $( "#publishingDate" ).datepicker( { dateFormat: 'dd-mm-yy' } );
    }
