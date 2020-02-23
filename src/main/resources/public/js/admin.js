function init() {
	$(".user_row").each(function() {
		tdRole = $(this)[0].cells["role"];
		currentRole = tdRole.children["role"].value;
		select = tdRole.children["select"];
		//NOTE: fix some "feature" of Thymeleaf element conditions: th:selected="${role == user.role}"
		select.children[currentRole].selected = true;
		
		tdServiceType = $(this)[0].cells["serviceType"];
		currentServiceType = tdServiceType.children["serviceType"].value;
		select = tdServiceType.children["select"];
		//NOTE: fix some "feature" of Thymeleaf element conditions: th:selected="${role == user.role}"
		if(currentServiceType == "") {
			select.children[0].selected = true;
		} else {
			select.children[currentServiceType].selected = true;
		}
		if(currentRole != "ROLE_MASTER") {
			$(select).hide();
		}
	});
}

function sendRowDataToServer(aRow, requestMethod) {
	var currentEmail = aRow.cells["email"].textContent; 
	
	var userDTO = {
	    email: currentEmail,
	    firstName: aRow.cells["firstName"].textContent,
	    lastName: aRow.cells["lastName"].textContent,
	    telNumber: aRow.cells["telNumber"].textContent,
		role: aRow.cells["role"].children["select"].value,
		serviceType: aRow.cells["serviceType"].children["select"].value
    }
	
    if(requestMethod == "PUT") {
		$.ajax({
	        url: "/api/users",
	        type: requestMethod,
	        dataType: "json",
	        contentType: "application/json",
	        success: function (data) {
	            showStatus("success", $("#i18n_success").val());
	        },
	        error: function (data) {
	            showStatus("error", $("#error_tryLater").val());
	        },
	        data: JSON.stringify(userDTO)
	    });
    }

	return true;
}

function setRole(cont) {
	currentRow = cont.parentNode.parentNode;
	
	tdRole = currentRow.cells["role"];
	selectRole = tdRole.children["role"];
	
	newRole = $(cont).val();
	$(selectRole).val(newRole); //this is because it updates after the function is finished
	
	tdServiceType = currentRow.cells["serviceType"];
	selectServiceType = tdServiceType.children["select"];
	if(newRole == "ROLE_MASTER") {
		$(selectServiceType).show();
	} else {
		$(selectServiceType).val("");
		$(selectServiceType).hide();
	}
	
	//react if it was successful
	if(!sendRowDataToServer(currentRow, "PUT")) {
		alert("data not sent to server");
		//NOTE: in real project we would have to process this case, now this is "out of scope"
	}
}

function setServiceType(cont) {
	currentRow = cont.parentNode.parentNode;
	
	tdServiceType = currentRow.cells["serviceType"];
	select = tdServiceType.children["select"];
	
	newServiceType = $(cont).val();
	$(select).val(newServiceType); //this is because it updates after the function is finished
	
	//react if it was successful
	if(!sendRowDataToServer(currentRow, "PUT")) {
		alert("data not sent to server");
		//NOTE: in real project we would have to process this case, now this is "out of scope"
	}
}

function del(cont) {
	if(!confirm("Are you sure you want to delete this user?")) {
        return;
    }	
	
	currentRow = cont.parentNode.parentNode;
	
	//react if it was successful
	if(!sendRowDataToServer(currentRow, "DELETE")) {
		alert("data not sent to server");
		//NOTE: in real project we would have to process this case, now this is "out of scope"
	} else {
		$(currentRow).remove();
	}
}

