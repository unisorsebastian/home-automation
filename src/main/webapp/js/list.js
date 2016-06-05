var baseURL = "";
var temperaturesURL = "";
var requestErrorHandler = function() {
	// alert("error");
}
var validateRequest = function() {
	return true;
}
var requestOptions = {
	error : requestErrorHandler,
	validate : validateRequest
};
var requestSuccess = function(data) {
	var json = JSON.stringify(data);
	var jsonObject = JSON.parse(json);
	$("#dataHolder").html("");
	$.each(data, function(i, v) {
		var msg = "<p id=" + v['name'] + ">name:" + v['name'] + " temp:" + v['temperature'] + "&#x2103;" + "</p>";
		$("#dataHolder").append(msg);
	});

};
var multipleCalls = function() {
	requestOptions.url = temperaturesURL;
	nrg.ajaxCall(requestOptions, requestSuccess); // do some stuff...
};

$(document).ready(function() {
	temperaturesURL = baseURL + "/rest/data/temperatures";
	multipleCalls();

	var tid = setInterval(multipleCalls, 5000);
	function abortTimer() {
		clearInterval(tid);
	}

});