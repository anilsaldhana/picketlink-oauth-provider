$(document).ready(function()
{
   //Popover 
   $('#registerHere input').hover(function() {
      $(this).popover('show')
	});

    //Validation
    $("#registerHere").validate({
	  rules:{
            user_name:"required",
            name:"required",
            user_email:{required:true,email: true},
            password:{required:true,minlength: 6},
            password1:{required:true,equalTo: "#password"}
        },

        messages:{
            user_name:"Enter your username",
            user_email:{
                required:"Enter your email address",
                email:"Enter valid email address"},
                password:{
                    required:"Enter your password",
                    minlength:"Password must be minimum 6 characters"},
                    password1:{
                        required:"Enter confirm password",
                        equalTo:"Password and Confirm Password must match"},
                        gender:"Select Gender"
        },

        errorClass: "help-inline",
        errorElement: "span",
        highlight:function(element, errorClass, validClass) {
           $(element).parents('.control-group').addClass('error');
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
            $(element).parents('.control-group').addClass('success');
        }
	});
});