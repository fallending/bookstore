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

	var SYNCHRONOUS = false;

	var createForm = function(props1, props2) {
		var mergeProperties = function(props1, props2) {
			var updateResultWith = function(props) {
				for (var name in props) {
					if (props.hasOwnProperty(name)) {
						result[name] = props[name];
					}
				}
			};

			var result = {};
			updateResultWith(props1);
			updateResultWith(props2);

			return result;
		};

		var mergedProps = mergeProperties(props1, props2);

		var formData = new FormData();
		for (var name in mergedProps) {
			if (mergedProps.hasOwnProperty(name)) {
				formData.append(name, mergedProps[name]);
			}
		}

		return formData;
	};

	var request = new XMLHttpRequest();
	request.open('POST', action, SYNCHRONOUS);
	request.send(createForm(originalParams, actualParams));

	document.open();
	document.write(request.responseText)
	document.close();

}
