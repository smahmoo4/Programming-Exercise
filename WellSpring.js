window.onload = function() {
	var input = document.getElementById('input');
	var display = document.getElementById('display');

	input.addEventListener('change', function(e) {
		var file = input.files[0];
		var textType = /test.*/;

		if (file.type.match(textType)) {
			var reader = new FileReader();

			reader.onload = function(e) {
				display.innerText = reader.result;
			}

			reader.readAsText(file);	
		} 

		else {
			display.innerText = "File not supported!";
		}
	});
}
