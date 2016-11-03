function sendPost(action, originalParams, actualParams) {

	var createFormFor = function(action) {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", action);

		return form;
	};

	var updateParams = function(finalParams, partialParams) {
		for (var key in partialParams) {
			if (partialParams.hasOwnProperty(key)) {
				finalParams[key] = partialParams[key];
			}
		}
	};

	var updateFormWithParams = function(form, params) {
		for (var name in params) {
			if (params.hasOwnProperty(name)) {
				updateFormWithSingleParam(form, name, params[name]);
			}
		}
	};

	var updateFormWithSingleParam = function(form, name, param) {
		if (isInputTag(param)) {
			updateFormWithSingleInputParam(form, param);
		} else {
			updateFormWithSingleStringParam(form, name, param);
		}
	};

	var isInputTag = function(value) {
		return (value.tagName == 'INPUT');
	};

	var updateFormWithSingleInputParam = function(form, inputTag) {
		inputTag.parentNode.insertBefore(inputTag.cloneNode(false), inputTag);
		form.appendChild(inputTag);
		if (inputTag.type.toLowerCase() == 'file') {
			form.setAttribute('enctype', 'multipart/form-data');
			form.setAttribute('encoding', 'multipart/form-data');
		}
	};

	var updateFormWithSingleStringParam = function(form, name, value) {
		form.appendChild(createHiddenInput(name, value));
	};

	var createHiddenInput = function(name, value) {
		return createInput(name, value, "hidden");
	};

	var createInput = function(name, value, type) {
		var element = document.createElement("input");
		element.setAttribute("type", type);
		element.setAttribute("name", name);
		element.setAttribute("value", value);

		return element;
	};

	var appendToDocument = function(form) {
		var hiddenIFrame = document.createElement("iframe");
		hiddenIFrame.setAttribute("width", "0");
		hiddenIFrame.setAttribute("height", "0");
		hiddenIFrame.setAttribute("frameBorder", "0");
		hiddenIFrame.setAttribute("scrolling", "no");
		hiddenIFrame.setAttribute("seamless", "seamless");
		hiddenIFrame.appendChild(form);
		document.body.appendChild(hiddenIFrame);
	};

	var params = {};
	updateParams(params, originalParams);
	updateParams(params, actualParams);

	var form = createFormFor(action);
	updateFormWithParams(form, params);

	appendToDocument(form);

	form.submit();
}
