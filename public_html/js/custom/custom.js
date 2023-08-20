/*========================================== MASTER JAVASCRIPT ===================================================================

	Project     :	HOTELBOOKING LANDING PAGE
	Version     :	1.0
	Last Change : 	19/10/2019
	Primary Use :   HOTELBOOKING LANDING PAGE

=================================================================================================================================*/

$(document).ready(function () {
	"use strict"; //Start of Use Strict
	var menu_li = $('.navbar-nav li a');
	var collapse = $('.navbar-collapse');
	var top_nav = $('.navbar-menu');
	
	/* Add & Remove active class in Menu and Submenu based on url(location) Start*/
        var url = window.location;
    // Will only work if string in href matches with location
        $('ul.navbar-nav a[href="' + url + '"]').parent().addClass('active');

    // Will also work for relative and absolute hrefs
        $('ul.navbar-nav a').filter(function () {
            return this.href == url;
		}).parent().addClass('active').parent().parent().addClass('active');

    /* Add & Remove active class in Menu and Submenu based on url(location) End*/
	
	$(window).scroll(function() {
	  var $header = $('.navbar-menu');
	  if ($(this).scrollTop() > 56) {
		if (!$header.hasClass('fixed-top')) $header.addClass("fixed-top");
	  } else {
		if ($header.hasClass('fixed-top')) $header.removeClass("fixed-top");
	  }
	});
	
	$('.carousel').carousel({
	  interval: false
	})
	
	//MENU-1 SCROLL
    $('.pagescroll').on('click', function(e) {
        var y = $(window).scrollTop();
        var t = "";
        if (y <= 230) {
            t = 170;
        } else {
            t = 85;
        }
        $('html, body').animate({
            scrollTop: $($.attr(this, 'href')).offset().top - t
        }, 2000);
        return false;
    });
	
    //RESPONSIVE MENU SHOW AND HIDE FUNCTION
    if (menu_li.length) {
        menu_li.on("click", function(event) {
			var disp = $(".navbar-toggler").css('display'); 
			if( !$(".navbar-toggler").hasClass('collapsed') ){			
				if(collapse.hasClass('show')){
					collapse.removeClass('show').slideUp( "slow");
				}
			}            
        });    
    }	
	
	 //NUMBER COUNTING
	var counter = $('.count-num');
	if (counter.length) { 
		counter.counterUp({
			delay: 10,
			time: 1000
		});
	}
    
				
	//POPUP FORM
		 var fadeandscale = $('#fadeandscale');
		if (fadeandscale.length) { 
				$('#fadeandscale').popup({
					pagecontainer: '.container',
					transition: 'all 0.3s'
				});
		}

		//Get URL parameter
		var getUrlParameter = function getUrlParameter(sParam) {
			var sPageURL = decodeURIComponent(window.location.search.substring(1)),
				sURLVariables = sPageURL.split('&'),
				sParameterName,
				i;
			for (i = 0; i < sURLVariables.length; i++) {
				sParameterName = sURLVariables[i].split('=');

				if (sParameterName[0] === sParam) {
					return sParameterName[1] === undefined ? true : sParameterName[1];
				}
			}
		};
	
	 //CONTACT FORM VALIDATION	
    if ($('.form-res').length) {
        $('.form-res').each(function() {
            $(this).validate({
                errorClass: 'error',
                submitHandler: function(form) {
                    $.ajax({
                        type: "POST",
                        url: "mail/mail.php",
                        data: $(form).serialize(),
                        success: function(data) {
                            if (data) {
								$(form)[0].reset();
                                $('.sucessMessage').html('Mail Sent Successfully!!!');
                                $('.sucessMessage').show();
                                $('.sucessMessage').delay(3000).fadeOut();
                            } else {
                                $('.failMessage').html(data);
                                $('.failMessage').show();
                                $('.failMessage').delay(3000).fadeOut();
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            $('.failMessage').html(textStatus);
                            $('.failMessage').show();
                            $('.failMessage').delay(3000).fadeOut();
                        }
                    });
                }
            });
        });
    }
	

    return false;
    // End of use strict
});