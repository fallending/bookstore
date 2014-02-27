function sendCreate() {
	var title = document.getElementById('newBook.title').value;
	sendPost('create', {
		'newBook.title' : title
	})
}

function sendUpdate(id, index) {
	var version = document.getElementById('books' + index + '.version').value;
	var title = document.getElementById('books' + index + '.title').value;
	sendPost('update', {
		'updatedBook.id' : id,
		'updatedBook.version' : version,
		'updatedBook.title' : title
	})
}

function sendDelete() {
	var params = {};
	var checkboxes = document.getElementsByClassName('deleteCheckbox');
	for (var index = 0; index < checkboxes.length; index++) {
		if (checkboxes[index].checked) {
			params[('books[' + index + '].id')] = checkboxes[index].getAttribute('bookId')
			params[('books[' + index + '].checked')] = 'on';
		}
	}
	sendPost('delete', params)
}

function sendGoToPage(newPageNumber) {
	sendPost('goToPage', {
		'pager.pageNumber' : newPageNumber
	})
}

function sendSetPageSize() {
	var newPageSize = document.getElementsByClassName('setPageSizeInput')[0].value;
	sendPost('setPageSize', {
		'pager.pageSize' : newPageSize
	})
}

function sendSort(column, direction) {
	sendPost('sort', {
		'pager.sorter.column' : column,
		'pager.sorter.direction' : direction
	})
}

function sendPost(action, actualParams) {
	var params = {};
	updateParams(params, ORIGINAL_PARAMS);
	updateParams(params, actualParams);

	var form = createFormFor(action)
	updateFormWithParams(form, params);

	document.body.appendChild(form);
	form.submit();
}

function createFormFor(action) {
	var form = document.createElement("form");
	form.setAttribute("method", "post");
	form.setAttribute("action", action);

	return form;
}

function updateParams(finalParams, partialParams) {
	for(var key in partialParams) {
		if(partialParams.hasOwnProperty(key)) {
			finalParams[key] = partialParams[key];
		}
	}
}

function updateFormWithParams(form, params) {
	for(var key in params) {
		if(params.hasOwnProperty(key)) {
			form.appendChild(createHiddenInput(key, params[key]));
		}
	}
}

function createHiddenInput(key, value) {
	var hiddenField = document.createElement("input");
	hiddenField.setAttribute("type", "hidden");
	hiddenField.setAttribute("name", key);
	hiddenField.setAttribute("value", value);

	return hiddenField;
}
