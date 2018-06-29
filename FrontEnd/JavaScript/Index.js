/**
 * When visitor hits the submit button, we send a request to the 
 * https://fonoapi.freshpixl.com with corresponding parameters. 
 * Based on the response from the server we either show Error
 * message (if the status of the response was error), or we populate
 * the content DIV (if the response contained useful data)
 */
var token = "f5d1e7d3bd4922486d51f98800aa53473e2d3021e8a0c658";
$("#submit").on('click', function() {
	$("#content").empty();
	$.ajax({
		url: "https://fonoapi.freshpixl.com/v1/getdevice?token="+token+"&device="+$('#device_txt').val()
	    ,
	    success: function(result){
	    	if("status" in result && result["status"] === "error"){
	    		$("#search_div_err").html(result["message"]);
	    	}else{
	    		$("#search_div_err").html("");
		    	$("#content").append($("<div></div>").attr('id','accordion'));
		    	$.each(result,function(i,obj) {
		    		  $("#accordion").append($("<h3></h3>").text(obj.DeviceName));
		    		  content = '';
		    		  content += '<table class="table" style="width:100%">';
					  content += '<tr><th>Info</th><th>Description</th></tr>';
		    		  for(var key in obj){
		    			  content += "<tr><td>" + key + "</td><td>" + obj[key] + "</td>";
		    		  }	
		    		  content += "</table>";
		    		  $("#accordion").append($("<div></div>").html(content));
		      	   }
		    	);
		    	$("#accordion" ).accordion({
		    		active:false,
		    		icons: {"header": "ui-icon-custome-triangle-e", "activeHeader": "ui-icon-custome-triangle-s" },
		    		collapsible: true,
		    		heightStyle: "content"
		    	});
	    	}
	    },
	    error: function(){
	    	$("#search_div_err").html("Unknown Error, Try Again.");
	    }	
	});
	
});