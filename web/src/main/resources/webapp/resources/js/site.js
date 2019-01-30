$(document).ready(function() {
	$('#mobileMenuBtn').click(function() {
		$('#mainMenu').toggle();
	});
	
	$('#filter a').click(function() {
		if($('.categoryList').css('display') == 'none')
			$('.categoryList').slideDown('slow');
		else
			$('.categoryList').slideUp('slow');
	}); 
	
	$('.cmOpen').click(function() {
		var cm = $(this).attr('data-open');
		
		// get current height
		var currentHeight = $('.compactMenu[id=' + cm + ']').css('height');
		
		// temporarily change height to auto and measure current height
		$('.compactMenu[id=' + cm + ']').css('height', 'auto');
		var autoHeight = $('.compactMenu[id=' + cm + ']').css('height');
		
		// switch back and animate menu from current height to automatic height
		$('.compactMenu[id=' + cm + ']').css('height', currentHeight).animate({height: autoHeight}, 500);
		
		// optionally fade out button :)
		$(this).fadeTo(500, 0).css('z-index', '-1');
	});
});

