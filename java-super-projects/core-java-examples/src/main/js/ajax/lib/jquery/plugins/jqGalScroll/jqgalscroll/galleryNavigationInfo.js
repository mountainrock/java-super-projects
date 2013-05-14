/**
 * @author koes
 */
function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

$(function(){
    var browser=navigator.appName;
    
    // crazy crazy hack
    // not sure why IE doesn't do cookie.indexOf well
    // might just be me though
    if(browser == 'Microsoft Internet Explorer'){
        if(document.cookie && document.cookie.indexOf("seen_rb_instruction") > -1)
            $('.gallery_nav').hide();
        else 
            $('.gallery_nav').show();
    } else {
        if(document.cookie && document.cookie.indexOf("seen_rb_instruction") > -1)
            $('.gallery_nav').hide();
    }
    
    
//    if(document.cookie && document.cookie.indexOf("seen_rb_instruction") > -1){
//        if(browser == 'Microsoft Internet Explorer')
//            $('.gallery_nav').show();
//        else
//            $('.gallery_nav').hide();
//    }

    $('#gallery_box, .jqGSContainer, .jqGSImgContainer, .jqGSPagination ul li a, .close').bind('click', function(){
        $('.gallery_nav').hide();
        createCookie('seen_rb_instruction', 'yes', 365);
    })
})
