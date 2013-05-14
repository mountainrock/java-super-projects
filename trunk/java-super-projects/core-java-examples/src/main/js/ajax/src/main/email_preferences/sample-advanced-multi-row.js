/**
 * Variable to enable disable debugging
 * 
 * @type Boolean
 */
DEBUG = false;

/**
 * Maintain the row number.
 * @type Number
 */
var newRowNum = 1;
/**
 * When the document is loaded set the events.
 */
$(document).ready(setEvents);
/**
 * Set the callback for UI event.
 */
function setEvents() {
	//initialize row size
	newRowNum=eval($('#listSize').val()) + 1;
 	//deal with exceptional events
	$("#result").ajaxError(ajaxErrorHandler);
 	//add more alerts
    $("#addMoreAlertsId").click(addMoreAlertsIdHandler);

 	// "add more" event handling
    $("#addMoreButton").click(addMoreButtonHandler);

 	$("#cancelAddMoreButton").click(cancelAddMoreButtonHandler);
 	  
 	//set event handler's per article alert
 	 	
 	var actionElements =['subscribe', 'settings', 'cancel', 'confirm', 'unsubscribe', 'tickLink', 'unsubscribeCancel','unsubscribeConfirm'];
 	
 	for (var i = 0; i < actionElements.length; i++) {
 		var handler=eval(actionElements[i]+'Handler');
 		$("[id^="+actionElements[i]+"-]").click(handler);  //id^=xyz - for checking any id starting with xyz 
 	}
 	
}
/**
 * Returns an event handler.
 * The default convention is to use the id as prefix followed by 'Handler' to evaluate the available handler.
 * Modify this based on custom needs.
 * @param {} id
 * @return {}
 */
function getEventHandler(id){
	var handlerName = id+'Handler';
	try{
	var handler= eval(handlerName);
	$.log('Found handler'+handlerName);
	}catch(err){
		$.log('Failed to find handler'+handlerName);
		return null;
	}
	return handler;
}
//GENERIC
/**
 * An error handler for AJAX calls
 * @param {} event
 * @param {} request
 * @param {} settings
 */
 function ajaxErrorHandler(event, request, settings){
		   $(this).text("An error occured while submitting your request to server!! ");
		   var index =  getIndex($(this).attr('id') );
		 
		   for (var i = 0; i < newRowNum; i++) {
		   	 $.enableAndDisable(['confirm','cancel'], null ,i);	
		   }		   
 }
 
 function buildPostParams(index){
 			var postParams = {
				'articleAlertsTO.journalId' : $('input[id=journalId-'+index+']').val(),
				'articleAlertsTO.status' : $('input[id=status-'+index+']').val(),
				'articleAlertsTO.emailFormat' : $('select[id=emailFormat-'+index+']').val(),
				'articleAlertsTO.frequency' : $('select[id=frequency-'+index+']').val()
			};
			if ($.browser.mozilla)
				$.log("Post param's :" + postParams.toSource());
			return postParams;
 }
//EVENT HANDLER'S

 function addMoreAlertsIdHandler(event) {
	  		$.hideAndShow(['articleAlertsAddListId'], ['articleAlertsOptionsListId'], null); 	  	
 }
 function addMoreButtonHandler(event) {
 	  	 	  	if($('#journalIdAdd').val() == ''){
 	  				 $('#journalRequiredLabel').show();
 	  	 	  		return;
 	  	 	  	}
 	  	 	  	$('#journalRequiredLabel').hide();
	 	  		var newAlert = $("#articleAlertsListTemplate li").clone(true);
														
				/*loop through the inputs in the new row
				 and update the ID and name attributes. 
				 ID is appended with index.
				*/	
	 	  		$(newAlert).each(function(i){
		 	  		var id=$(this).attr('id');
		 	  		var newID = id +'-'+newRowNum; 		 	  		
		 	  		$(this).attr('id',newID);
	 	  		});
				var fields=newAlert.find("input,a,select,img,em");
				$(fields).each(function(i){
					
					var id=$(this).attr('id');					
					var newID = id +'-'+newRowNum; // couldn't escape [ using selectors
					
					var name=$(this).attr('name');		
					if(name==null || name==''){
						name=id;
					}
					var newName = name + '[' + newRowNum +']';
					
					$(this).attr('id',newID).attr('name',newName);
					
					//NOTE: clone doesn't  copy over events in deep hierarchy  ??? .Hence have to bind event's here :(.
					var eventHandler= getEventHandler(id);
					if(eventHandler){
						$(this).bind('click', eventHandler);
					}
					
					if(id=='journalId' || id=='emailFormat' || id=='frequency'){
						var addId='#'+id +'Add';
						var valId=$(addId).val();
						$(this).val(valId);
						
						$.log('Setting value into id: '+ newID +' from id: '+id +' , with value '+valId );
					}
					
					$.log("Preparing new alert : "+newID);
				});
				

				$.log("Appending new alert : "+newAlert.attr('id'));
				$("#articleAlertsList").append(newAlert);
				
				//insert the journal name
				var labelIdAr=["journalLabel-A","journalLabel-B","journalLabel-BB","journalLabel-C","journalLabel-CC","journalLabel-D","journalLabel-DD"];
				var journalName=$('#journalIdAdd').find("[value="+$('#journalIdAdd').val()+"]").text();								
				
				for (var i = 0; i < labelIdAr.length; i++) {
					var labelId=labelIdAr[i];
					$('[id='+labelId+'-'+newRowNum+']').text(journalName);
				}							
				
	 	  		$.log('Posting article alert');
	 	  		$('#journalIdAdd').find("[value="+$('#journalIdAdd').val()+"]").remove();
	 	  		submitAlert(newRowNum);
	 	  		newRowNum += 1;	
}

 function cancelAddMoreButtonHandler(event) {
	 	  	$.hideAndShow(['articleAlertsOptionsListId','journalRequiredLabel'], ['articleAlertsAddListId'], null);
}
 	  
 function subscribeHandler(event) {
 	    var index =  getIndex($(this).attr('id') );
 	    $.hideAndShow(['subscribeListId'],['subscribeOptionsListId'], index);
  }
  
 function confirmHandler(event) {
		var index =  getIndex($(this).attr('id') );
		event.preventDefault();
		submitAlert(index);
}

  function tickLinkHandler(event) {
	var index =  getIndex($(this).attr('id') );
	$.hideAndShow(['subscribeListId','unsubscribeListId','unsubscribeListId']
				,['confirmUnsubscribeListId']
				,index );
}
function unsubscribeConfirmHandler(event) {
			var index =  getIndex($(this).attr('id') );
			
			$.log("clicked unsubscribe");
			$("#result").text('');	
			$.enableAndDisable(null,['unsubscribeConfirm','unsubscribeCancel'],index);
			$.hideAndShow(['confirmUnsubscribeListId'], ['statusLI'], index);
			
			$('input[id=status-'+index+']').val("false");
			event.preventDefault();
			var postParams= buildPostParams(index);

			$.post('mailprefs', postParams, function(data) {
						if (data.string == 'Success') {
							$.hideAndShow(['confirmUnsubscribeListId','statusLI'], ['subscribeListId'], index);
										
							$('#loading').hide();
						} else {
							$("#result").text("" + data.string);
							$.hideAndShow(['statusLI'], ['confirmUnsubscribeListId'] ,index);
						}
						$.enableAndDisable(['unsubscribeConfirm','unsubscribeCancel'], null ,index);
					}, 'json');
			
			return false;
}

function unsubscribeCancelHandler(event) {
		var index =  getIndex($(this).attr('id') );
		$.hideAndShow(['confirmUnsubscribeListId'],['unsubscribeListId'],index );
}

function cancelHandler(event) {
		 var index =  getIndex($(this).attr('id') );
		 var status = $('input[id=status-'+index+']').val();
		 if(status =='true')
		 {
		  $.hideAndShow(['subscribeOptionsListId'],['unsubscribeListId'], index);
		 }else{
		  $.hideAndShow(['subscribeOptionsListId'],['subscribeListId'], index);
		 }
}

function unsubscribeHandler(event){
		var index =  getIndex($(this).attr('id') );
		$.hideAndShow(['subscribeListId','unsubscribeListId','unsubscribeListId']
					,['confirmUnsubscribeListId']
					,index );
}

function settingsHandler(event) {
			$.log('Clicked settings');
			 var index = getIndex($(this).attr('id') );		
			 $.hideAndShow(['subscribeListId','unsubscribeListId']
			 			 ,['subscribeOptionsListId']
			 			 , index);
}
			
function submitAlert(index){

				$.log("clicked confirm");
				$.enableAndDisable(null , ['confirm','cancel'] ,index);
				$('input[id=status-'+index+']').val("true");
				
				$.hideAndShow(['subscribeOptionsListId'], ['statusLI'], index);
											
				$("#result").text('');			
				
				var postParams = buildPostParams(index);
				
				/* toSource() doesn't work well in ie */
				if ($.browser.mozilla)
					$.log("Subscribing with :" + postParams.toSource());

				$.post('mailprefs', postParams, function(data) {

							if (data.string == 'Success') {
								$.hideAndShow(['statusLI','subscribeListId','subscribeOptionsListId']
											,['unsubscribeListId','tick']
											, index);

  							    
							} else {
								$.hideAndShow(['statusLI'], ['subscribeOptionsListId'], index);	
								$('li[id=tick-'+index+']').attr({
											'src' : '/images/cross.gif'
										}).show();								
								$("#result").text("" + data.string);
								
							}
				 			$.enableAndDisable(['confirm','cancel'], null ,index);
				 			if($('#journalIdAdd option').size()==1)
									$("#articleAlertsAdd").hide();
						}, 'json');
}

/**
 * Get the index from identifier
 * @param {} indexStr
 * @return {}
 */
function getIndex(indexStr){
	 var index= eval(indexStr.split('-')[1]);
	 return index;
}
