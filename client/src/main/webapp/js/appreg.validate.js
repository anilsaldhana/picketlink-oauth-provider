$(document).ready(function()
{
// Popover 
$('#app_registerHere input').hover(function()
{
$(this).popover('show')
});

// Validation
$("#app_registerHere").validate({
rules:{
app_name:"required",
app_url:{required:true,app_name: true},
pwd:{required:true,minlength: 6},
cpwd:{required:true,equalTo: "#pwd"},
gender:"required"
},

messages:{
app_name:"Enter your application name",
app_url:{
required:"Enter your Application URL",
app_name:"Enter valid Application Name"},
pwd:{
required:"Enter your password",
minlength:"Password must be minimum 6 characters"},
cpwd:{
required:"Enter confirm password",
equalTo:"Password and Confirm Password must match"},
gender:"Select Gender"
},

errorClass: "help-inline",
errorElement: "span",
highlight:function(element, errorClass, validClass)
{
$(element).parents('.control-group').addClass('error');
},
unhighlight: function(element, errorClass, validClass)
{
$(element).parents('.control-group').removeClass('error');
$(element).parents('.control-group').addClass('success');
}
});
});
