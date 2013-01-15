var getHost = function() {
	var port = '8080';
	var hname = window.location.hostname;
	
	if(hname.indexOf("rhcloud.com") == -1){
		return 'http://' + hname + ":" + port + "/picketlink-oauth-provider/jsp";	
	} else {
		return 'http://' + hname + "/picketlink-oauth-provider/jsp";	
	}
};

function storeToken(token) {
	try {
		localStorage.setItem("AUTH_TOKEN", token);
	} catch (e) {
		alert('Your browser does not support HTML5 localStorage. Try upgrading.');
	}
}

function getToken() {
	try {
		return localStorage.getItem("AUTH_TOKEN");
	} catch (e) {
		alert('Your browser does not support HTML5 localStorage. Try upgrading.');
	}
}

$.ajaxSetup({
	headers : {
		"Auth-Token" : getToken()
	},
	error : function(xhr, textStatus, errorThrown) {
		if (window.location.pathname.indexOf("picketlink-login.html", 0) == -1) {
			if (window.location.pathname.indexOf("error.html", 0) == -1) {
				if (xhr.status == 500 || xhr.status == 403) {
					window.location = getHost() + "/error.html";
					return;
				}
			}

			window.location = getHost() + "/picketlink-login.html";
		}
	}
});

$(document).ready(function() {
	if (!$('#login-btn')) {
		return;
	}
	
	$('#login-btn').click(function() {
		var jqxhr = $.ajax('/picketlink-oauth-provider-server/signin', {
			contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({userId:$('#username').val(),password:$('#password').val()}),
            type:'POST', 
            success:function (data) {
                if (data.loggedIn) {
                	storeToken(data.token);
                        window.location = getHost() + "/picketlink-apps-list.html";
                } else {
                	$('#login-msg').text("Authentication failed. Try again ...");
                }
            }
        });
		return false; // prevents submit of the form
	});
	
});

$(document).ready(function() {
	if (!$('#logout-btn')) {
		return;
	}
	
	$('#logout-btn').click(function() {
		var jqxhr = $.ajax('/picketlink-oauth-provider-server/logout', {
            data:{},
            type:'GET', 
            success:function (data) {
            	window.location = getHost() + "/picketlink.html";
            },error:function(data) {
                   alert('log out failed');
                }
        });
		return false; // prevents submit of the form
	});
});

$(document).ready(function() {
	if (!$('#username-msg')) {
		return;
	}

	if ($('#username-msg').length > 0) {
		var jqxhr = $.ajax('/picketlink-oauth-provider-server/userinfo', {
	        data:{},
	        type:'GET', 
	        success:function (data) { 
                   $('#username-msg').text(data.fullName);
	        }
	    });
	}
});

var popup = null;

function sendMainPage() {
	popup.close();
	window.location = getHost() + "/picketlink.html";
}


$(document).ready(function() {
	$('#signup-btn').click(function() {
		window.location = getHost() + "/register.html";
		return false; // prevents submit of the form
	});
});
