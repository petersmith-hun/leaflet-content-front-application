/**
 * Checks if given password and password confirmation are equal
 */
$.fn.passwordCheck = function() {
	
	// fire handler on submit event
	this.on('submit', function(event) {
		
		// form shall not be submitted immediately, check password before
		event.preventDefault();
		
		var password = $(this).find('#UserPassword');
		var passconfirm = $(this).find('#UserPasswordConf');
		
		// one or both password fields are missing, skip checking, and submit form
		if(password === undefined || passconfirm === undefined) {
			
			// turn off event handler before submitting
			$(this).off('submit');
			$(this).submit();
		}
		
		// new password field is empty
		if(password.val().length == 0)
			alert('Nem adtál meg új jelszót!');
		else {
			
			// check password - if they are not equal, notify user
			if(password.val() != passconfirm.val())
				alert('Jelszó és megerősítése nem egyezik!');
			else {
				// okay passwords are equal, turn off event handler and submit form
				$(this).off('submit');
				$(this).submit();
			}	
		}
	});
};

/**
 * Marks active menu. 
 */
$.fn.markActive = function() {
    if(document.location.pathname == '/')
    	this.find('a:first').addClass('active');
    else {
	    this.find('a').each(function () {
	    	if($(this).attr("href") != '/')
	        	if (document.location.href.indexOf($(this).attr("href")) != -1) $(this).addClass("active");
	    });
	}	
};
