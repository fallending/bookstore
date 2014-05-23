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
		form.appendChild(inputTag);
		if ('files' in inputTag) {
			form.setAttribute('enctype', 'multipart/form-data');
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

	var params = {};
	updateParams(params, originalParams);
	updateParams(params, actualParams);

	var form = createFormFor(action);
	updateFormWithParams(form, params);

	document.body.appendChild(form);
	form.submit();
}
