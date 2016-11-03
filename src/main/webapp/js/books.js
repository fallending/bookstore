function sendCreate() {
	var title = document.getElementById('newBook.title').value;
	var file = document.getElementById('newBook.file');
	sendPost('create', ORIGINAL_PARAMS, {
		'title' : title,
		'file' : file
	})
}

function sendUpdate(id, index) {
	var version = document.getElementById('books' + index + '.version').value;
	var title = document.getElementById('books' + index + '.title').value;
	sendPost('update', ORIGINAL_PARAMS, {
		'id' : id,
		'version' : version,
		'title' : title
	})
}

function sendDelete() {
	var params = {};
	var checkboxes = document.getElementsByClassName('deleteCheckbox');
	var idIndex = 0;
	for (var checkboxIndex = 0; checkboxIndex < checkboxes.length; checkboxIndex++) {
		if (checkboxes[checkboxIndex].checked) {
			params['ids[' + idIndex + ']'] = checkboxes[checkboxIndex].getAttribute('bookId');
			idIndex = idIndex + 1;
		}
	}
	sendPost('delete', ORIGINAL_PARAMS, params)
}

function sendGoToPage(newPageNumber) {
	sendPost('goToPage', ORIGINAL_PARAMS, {
		'pager.pageNumber' : newPageNumber
	})
}

function sendSetPageSize() {
	var newPageSize = document.getElementsByClassName('setPageSizeInput')[0].value;
	sendPost('setPageSize', ORIGINAL_PARAMS, {
		'pager.pageSize' : newPageSize
	})
}

function sendSort(column, direction) {
	sendPost('sort', ORIGINAL_PARAMS, {
		'pager.sorter.column' : column,
		'pager.sorter.direction' : direction
	})
}
