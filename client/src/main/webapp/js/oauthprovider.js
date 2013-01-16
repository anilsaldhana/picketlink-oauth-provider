$(document).ready(function() {
	if (!$('#create-app')) {
		return;
	}
	
	$('#create-app').click(function() {
		var jqxhr = $.ajax('/picketlink-oauth-provider-server/appregister', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({appName:$('#app_name').val(),appURL:$('#app_url').val()}),
            type:'POST', 
            success:function (data) {
                if (data.registered) {
                    window.location = getHost() + "/picketlink-apps-list.html";
                } else { 
            	    alert('Registration failed');
                }
            }
        });
		return false; // prevents submit of the form
	});
	
});

//Populate apps table
$(document).ready(function() {
	if (!$('#apps-list-table')) {
		return;
	}
	
	if ($('#apps-list-table').length > 0) {
		var userid;
		
		var jqxhr0 = $.ajax('/picketlink-oauth-provider-server/userinfo', {
	        data:{},
	        type:'GET', 
	        success:function (data) { 
	        	userid = data.userId;
	        	
	        	jqxhr = $.ajax('/picketlink-oauth-provider-server/applist', {
	    			contentType: "application/json",
	                dataType:'json',
	    	        data:JSON.stringify({userId:userid}),
	    	        type:'POST', 
	    	        success:function (data) {
	                	$(data.applications).each(function(key, value){
	                      var clickURL = "<a onclick=\"appDetail(\'" + value.name + "\')\">";
	                      
	                	  var td = "<td><div>" + clickURL + value.name + "<a></div></td>"; 
	                	  
	                      $('#apps-list-table').append('<tr>' + td +'<td> '+value.url+' </td></tr>');
	               	    })
	    	        }
	    	    });
	        }
	    });
	}
});


function appDetail(app) {
	try {
		localStorage.setItem("APP", app);
        window.location = getHost() + "/picketlink-app-detail.html";
	} catch (e) {
		alert('Your browser does not support HTML5 localStorage. Try upgrading.');
	}
}

//Populate app detail table
$(document).ready(function() {
	if (!$('#app-detail-table')) {
		return;
	}
	
	if ($('#app-detail-table').length > 0) {
		var app;
		try {
			app = localStorage.getItem("APP");
			localStorage.removeItem("APP");
		} catch (e) {
			alert('Your browser does not support HTML5 localStorage. Try upgrading.');
		}
		var jqxhr0 = $.ajax('/picketlink-oauth-provider-server/appdetail?app='+ app, {
	        data:{},
	        type:'GET', 
	        success:function (data) {  
	               var str = "<tr>   <td>Application Name</td>   <td>" + data.name + "</td>  </tr>  " +
                             "<tr>   <td>Application URL</td>    <td>" + data.url + "</td>  </tr> " +  
                             "<tr>   <td>Client ID</td>  <td>"+ data.clientID+"</td>  </tr>" +  
                             "<tr>   <td>Client Secret</td>  <td> " + data.clientSecret +"</td>  </tr>";
	                	  
	               $('#app-detail-table').append(str);
	    	 }
	    });  
	}
});

//Account Registration
$(document).ready(function() {
	if (!$('#register-here')) {
		return;
	}
	
	$('#register-btn').click(function() {		
		var userName = $('#user_name').val();
		
		//First check if username already registered
		var jqxhr0 = $.ajax('/picketlink-oauth-provider-server/accregister?id='+userName, {
	        data:{},
	        type:'GET', 
	        success:function (data) {
                if (data.registered) {
                	//UserName already registered
                	alert("User Already Registered");
                }else {
                	//UserName not already registered
                	var jqxhr = $.ajax('/picketlink-oauth-provider-server/accregister', {
            			contentType: "application/json",
                        dataType:'json',
                        data:JSON.stringify({userName:$('#user_name').val(),password:$('#password').val(),address:$('#address').val(),
                        	firstName:$('#firstname').val(),lastName:$('#lastname').val(),email:$('#email').val(),
                        	postalCode:$('#postalcode').val(),city:$('#city').val(),state:$('#state').val(),country:$('#country').val()}),
                        type:'POST', 
                        success:function (data) {
                            if (data.registered) {
                                window.location = getHost() + "/picketlink-login.html";
                            } else { 
                        	    alert('Registration failed');
                            }
                        }
                    });
                } 
	        }
	    });
		return false; // prevents submit of the form
	});
});