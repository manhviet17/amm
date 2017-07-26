<!DOCTYPE HTML>
<html>
<head>
<title>VoiceSearch Application</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>
	function madeAjaxCall() {
		var text = $('#text').val();
		$.ajax({
			url : "./service/nlu?text=" + text + "&uid=pc1"
		}).then(function(data) {
			var text = JSON.stringify(data, null, 2);
			if (data.contents != null) {
				$("#number").html(data.contents.length);
			} else {
				$("#number").html(0);
			}
			$("#result").val(text);
		});
	}
	$(document).ready(function() {
		$('#text').keypress(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.which);
			if (keycode == '13') {
				madeAjaxCall();
			}
		});

		// 		 document.querySelector('#form').addEventListener('submit', function(e) {
		// 			   e.preventDefault()
		// 			   e.stopPropagation()
		// 			   console.log(this['input-field'].value)
		// 			 })
	});
</script>
</head>
<body>
	<!-- 	<form action="" id="form"> -->
	Input Text:
	<input id="text" type="text" size="50">
	<input id="button" onclick="madeAjaxCall()" type="submit"
		value="Search">
	<!-- 	</form> -->

	<br /> Result:
	<span id="number"></span>
	<br />
	<textarea cols="150" rows="50" id="result">
	</textarea>
</body>
</html>