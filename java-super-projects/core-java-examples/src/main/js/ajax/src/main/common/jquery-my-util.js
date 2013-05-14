/**
 * A Jquery utility plugin to maintain commonly used functions. 
 * 
 */
 
 
/**
 * An utility to hide and show list of elements in DOM
 * @param hideList - the list of id's to hide
 * @param showList - the list of id's to show
 * @param index - for multiple elements with same id, index is suffixed with the 'id'.
 * 					Else if index is null, id is treated as is. 
 * 					 
 */
jQuery.hideAndShow = function (hideList, showList, index ){
	var idxSuffix='';
	if(index){
		idxSuffix='-'+index;
	}
	if(hideList){
		for (var i = 0; i < hideList.length; i++) {
			var idxField= "[id=" + hideList[i] +idxSuffix + "]" ;
			$.log('Hiding '+idxField);
			$(idxField).hide();
		}
	}
	if(showList){
		for (var i = 0; i < showList.length; i++) {
			var idxField= "[id=" + showList[i]+idxSuffix + "]" ;
			$.log('Showing '+idxField);
			$(idxField).show();
		}
	}
};

/**
 * An utility to enable and disable list of elements in DOM
 * @param enableList - the list of id's to enable
 * @param disableList - the list of id's to disable
 * @param index - for multiple elements with same id, index is suffixed with the 'id'.
 * 					Else if index is null, id is treated as is.   
 */
jQuery.enableAndDisable = function (enableList, disableList, index){
		var idxSuffix='';
	if(index){
		idxSuffix='-'+index;
	}
	
	if(enableList){
		for (var i = 0; i < enableList.length; i++) {
			var idxField= "[id=" + enableList[i]+idxSuffix+ "]" ;
			$.log('Hiding '+idxField);
			$(idxField).removeAttr("disabled");
		}
	}
	if(disableList){
		for (var i = 0; i < disableList.length; i++) {
			var idxField= "[id=" + disableList[i]+ idxSuffix  + "]" ;
			$.log('Showing '+idxField);
			$(idxField).attr("disabled", "true");
		}
	}
};