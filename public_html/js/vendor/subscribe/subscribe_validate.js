$(document).on('ready', function() {
	function validMail(email) 
	{
		 var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/; 
		 return expr.test(email);
	}		
	$("#mce-EMAIL").keypress(function() {
           $('#mce-EMAIL').removeClass('place_error errorClass');
		   $("#mce-EMAIL").css("border-color", "");
    });	
	$("#mce-WEBSITE").keypress(function() {
           $('#mce-EMAIL').removeClass('place_error errorClass');
		   $("#mce-EMAIL").css("border-color", "");
    });
	
	$("#mc-embedded-subscribe-form").on("submit", function(event) { 
		event.preventDefault();	
		var $from = $(this);
		var FormID = $from.attr("id");		
		var actionUrl = $from.attr('action');
		var mcUrl = actionUrl.replace('/post?', '/post-json?').concat('&c=?');		
		
		
		var email = $("#mce-EMAIL").val();
		
		if( email == '' ){				
			 $("#mce-EMAIL").attr('placeholder' , 'Please Enter Your Email');
			 $('#mce-EMAIL').addClass('place_error errorClass');				
			 return false;
		}			
		else if(email != ''){	
		
			if(!validMail(email)){
				$("#mce-EMAIL").val('');
				$("#mce-EMAIL").attr('placeholder' , 'Invalid Email Address');
				$("#mce-EMAIL").focus()	
				$('#mce-EMAIL').addClass('place_error errorClass'); 
				return false;
			}				
		 }
			
			$.ajax({
				type: "POST",      
				url: mcUrl,      
				data: $from.serialize(),
				cache : false,
				dataType: 'json',
				contentType: "application/json; charset=utf-8",
				success: function (data) {
					//var ms = data['msg'];				
				   if (data['result'] != "success") {
						$('.FailureMsg').html(data['msg']);
						$('.FailureMsg').show();
						$('.FailureMsg').delay(3000).fadeOut();
				   } else {					
					   $('#'+FormID)[0].reset();  
					   $('.SuccessMsg').html('Email Subscribe Successfully !!!');
					   $('.SuccessMsg').show();
					   $('.SuccessMsg').delay(3000).fadeOut();
				   }				  
				},
				error: function(err) 
				{ 				   
					$('.FailureMsg').html('Could not connect to the registration server. Please try again later!');
					$('.FailureMsg').show();
					$('.FailureMsg').delay(3000).fadeOut();
				}
			});	  
	});				
	
});	
