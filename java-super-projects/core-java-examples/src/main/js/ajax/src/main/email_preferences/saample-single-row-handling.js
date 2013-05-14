/**
 * Variable to enable disable debugging
 * 
 * @type Boolean
 */
DEBUG = false;

/**
 * When the document is loaded set the events.
 */
$(document).ready(setEvents);
/**
 * Sets the callback for UI event.
 */

function setEvents() {
	
 	//deal with exceptional events
	$("#result").ajaxError(function(event, request, settings){
		   $(this).text("An error occured while sending your request to server!!");
		   $("#statusLI").hide();
 	  });
 	  
 	//handle normal events  
 	 $("#subscribe").click(function(event) {
	 		$('#subscribeOptionsListId').show();
	 		$('#subscribeListId').hide();
	});
 	 
 	 
	$("#settings").click(function(event) {
				$('#subscribeOptionsListId').show();
				$('#subscribeListId,#unsubscribeListId').hide();
			});

	$("#cancel").click(function(event) {
				$('#subscribeOptionsListId').hide();
				var status=$("#status").val();
				if(status =='true'){
			 	 $('#unsubscribeListId').show();
				}else{
				 $('#subscribeListId').show();
				}
				
			});	

	$("#confirm").click(function(event) {
				$.log("clicked confirm");
				$("#confirm,#cancel").attr("disabled", "true"); 
				$("#result").text('');			
				$("#statusLI").show();
				$('#subscribeOptionsListId').hide();
				
				$("#status").val("true");

				event.preventDefault();
				var postParams = buildPostParams();
				
				/* toSource() doesn't work well in ie */
				if ($.browser.mozilla)
					$.log("Subscribing with :" + postParams.toSource());

				$.post('mailprefs', postParams, function(data) {

							if (data.string == 'Success') {								
								$('#unsubscribeListId,#tick').show();
								
							} else {
								$('#tick').attr({
											'src' : '/images/cross.gif'
										}).show();								
								$("#result").text("" + data.string);
								$('#subscribeOptionsListId').show();
							}
							$("#confirm,#cancel").removeAttr("disabled");
							$("#statusLI").hide();
						}, 'json');
			});	
	$("#unsubscribe,#tickLink").click(function(event) {
		$('#confirmUnsubscribeListId').show();
		$('#subscribeListId,#subscribeOptionsListId,#unsubscribeListId').hide();
	});

	$("#unsubscribeCancel").click(function(event) {
		$('#confirmUnsubscribeListId').hide();
		$('#unsubscribeListId').show();
	});
	
	$("#unsubscribeConfirm").click(function(event) {
				$.log("clicked unsubscribe");
				$("#result").text('');
				$("#unsubscribeConfirm,#unsubscribeCancel").attr("disabled", "true"); 
				$("#status").val("false");
				
				$("#statusLI").show();
				$('#confirmUnsubscribeListId').hide();
				
				event.preventDefault();
				var postParams = buildPostParams();
				if ($.browser.mozilla)
					$.log("UnSubscribing with :" + postParams.toSource());

				$.post('mailprefs', postParams, function(data) {
							if (data.string == 'Success') {
							
								$('#subscribeListId').show();
								
							} else {
								$("#result").text("" + data.string);
								$('#confirmUnsubscribeListId').show();
							}
							$("#unsubscribeConfirm,#unsubscribeCancel").removeAttr("disabled"); 
							$("#statusLI").hide();
						}, 'json');
				
				return false;
			});

	if (($("#status").val() == 'true')) {	
		$('#unsubscribeListId,#tick').show('fast');		
	} else {
		$('#subscribeListId').show('fast');	
	}
	
}

 function buildPostParams(){
 	var postParams = {
					'articleAlertsTO.status' : $('#status').val(),
					'articleAlertsTO.emailFormat' : $('#emailFormat').val(),
					'articleAlertsTO.frequency' : $('#frequency').val()
				};
	return postParams;
 }

