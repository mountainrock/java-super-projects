/**
 * jQuery jqGalScroll Plugin
 * Examples and documentation at: http://benjaminsterling.com/2007/08/24/jquery-jqgalscroll-photo-gallery/
 *
 * @author: Benjamin Sterling
 * @version: 2.0
 * @copyright (c) 2007 Benjamin Sterling, KenzoMedia
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *   
 * @requires jQuery v1.2.1 or later
 * @optional jQuery Easing v1.2
 *
 *
 * 
 * changes:
 * 		10/05/2007
 * 			Removed: 	the up and down arrows, were not a useful as 
 * 							originally thought
 * 			Removed: 	the param for the nacArrowOpacity
 * 			Added:		some wrappers to allow for better styling.
 * 			Changed:	marginTop animation to just top
 *			Added:	When an image is too big or too small, it will
 *						align the img into the center.
 *
 * 
 * changes:
 * 		by: Koesmanto Bong
 * 		02/06/2008
 * 		Code was modified to accomodate horizontal photo gallery
 * 		Also enabled clicking on the image to move on to the next image (Ben Sterling contributed to this)
 * 
 * 			
 */
(function($) {
	$.fn.jqGalScroll = function(options){
		return this.each(function(i){
			var curImage = 0;
			var el = this, $this = $(this).css({position:'relative'}), $children = $this.children();
			el.opts = $.extend({}, $.jqGalScroll, options);
			$this.css({height:el.opts.height, width:$children.size()*el.opts.width});
			el.index = i;
			el.container = $('<div id="jqGS'+i+'" class="jqGSContainer">').css({position:'relative'});
			el.ImgContainer = $('<div class="jqGSImgContainer" style="height:'+el.opts.height+'px;position:relative;overflow:hidden">');
			$this.wrap(el.container);
			$this.wrap(el.ImgContainer);
			el.pagination = $('<div class="jqGSPagination">');
			$this.parent().parent().append(el.pagination);

			$children.each(function(){
				var $child = $(this).css({height:el.opts.height,width:el.opts.width});
				var $img = $child.children('img');
				var $loader = $('<div class="jqGSLoader">').appendTo($child);
				//var $titleHolder = $('<div class="jqGSTitle">').appendTo($child).css({opacity:el.opts.titleOpacity}).hide();
				var image = new Image();
				
				$img.click(function(){
					
					var browser=navigator.appName;
					var b_version=navigator.appVersion;
					var version=parseFloat(b_version);
					
					w = $("div.jqGSPagination ul li a.selected").attr("href");
					
					var x = w.split("#");
					
					y = x[1];
					z = parseInt(y) + 1;
					
					if(z == ($children.size())) z = 0;
					
//					console.log(w + ', x=' + x[1] + ', y=' + y + ', z=' + z);
//					
					
					if(browser == 'Microsoft Internet Explorer'){
//                        console.log('href='+ BASE_URL +'#'+ z + ']');
						$('a[href='+ BASE_URL +'#'+ z + ']').click();
					} else {
						$('a[href=#'+ z + ']').click();
					}
					
					})
					.hide();
				image.onload = function(){
					image.onload = null;
					$loader.fadeOut();
					$img.css({marginLeft:-image.width*.5,marginTop:-image.height*.5}).fadeIn();
					var alt = $img.attr('alt');
					if(typeof alt != 'undefined'){
						//$titleHolder.text(alt).fadeIn();
					}
				};
				image.src = $img.attr('src');
			});
			
			var $ul = $('<ul>');
			
			for(var i = 0; i < $children.size(); i++){
				var selected = '';
				if(i == 0) selected = 'selected';
				
//				console.log(i)
				
				var $a = $('<a href="#'+(i)+'" class="'+selected+'">'+(i+1)+'</a>').click(function(){
					var href = this.href.replace(/^.*#/, '');
					el.pagination.find('.selected').removeClass('selected');
					$(this).addClass('selected');
					$this.stop().animate({right:($children.width()*href)},el.opts.speed, el.opts.ease);
					index = href;
					return false;
				});
				$('<li>').appendTo($ul).append($a);
			};
			el.pagination.append($ul);
		}); // end : this.each(function(){
	};
	$.jqGalScroll = {
		ease: null,
		speed:0,
		height: 'auto',
		width: 'auto',
		titleOpacity : .60
	};
})(jQuery);