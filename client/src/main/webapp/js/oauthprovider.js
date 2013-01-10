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
                	alert(data.registered);
                        window.location = getHost() + "/picketlink-apps-list.html";
                } else { 
            	    alert(':failed');
                }
            }
        });
		return false; // prevents submit of the form
	});
	
});
