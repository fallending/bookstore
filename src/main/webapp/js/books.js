/*
 * Copyright (C) 2013-2014 Paweł Jojczyk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

function sendCreate() {
	var title = document.getElementById('newBook.title').value;
	var fileInput = document.getElementById('newBook.file');
	sendPost('create', {
		'title' : title,
		'file' : fileInput
	})
}

function sendUpdate(id, index) {
	var version = document.getElementById('books' + index + '.version').value;
	var title = document.getElementById('books' + index + '.title').value;
	sendPost('update', {
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
	for (var key in partialParams) {
		if (partialParams.hasOwnProperty(key)) {
			finalParams[key] = partialParams[key];
		}
	}
}

function updateFormWithParams(form, params) {
	for (var name in params) {
		if (params.hasOwnProperty(name)) {
			updateFormWithSingleParam(form, name, params[name]);
		}
	}
}

function updateFormWithSingleParam(form, name, param) {
	if (isInputTag(param)) {
		updateFormWithSingleInputParam(form, param);
	} else {
		updateFormWithSingleStringParam(form, name, param);
	}
}

function isInputTag(value) {
	return (value.tagName == 'INPUT');
}

function updateFormWithSingleInputParam(form, inputTag) {
	form.appendChild(inputTag);
	if ('files' in inputTag) {
		form.setAttribute('enctype', 'multipart/form-data');
	}
}

function updateFormWithSingleStringParam(form, name, value) {
	form.appendChild(createHiddenInput(name, value));
}

function createHiddenInput(name, value) {
	return createInput(name, value, "hidden");
}

function createInput(name, value, type) {
	var element = document.createElement("input");
	element.setAttribute("type", type);
	element.setAttribute("name", name);
	element.setAttribute("value", value);

	return element;
}
