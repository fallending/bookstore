<#import "/spring.ftl" as spring>

<#assign offset = booksCommand.pager.limited.offset>
<#assign size = booksCommand.pager.limited.size>
<#assign totalCount = booksCommand.pager.totalCount>
<#assign limit = offset + size>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
		<title>Bookstore</title>
		<style type="text/css">
			input.setPageSizeInput { }
			input.deleteCheckbox { }
		</style>
		<script type="text/javascript">

			DEFAULT_PARAMS = {
				'pager.current.offset' : ${booksCommand.pager.current.offset},
				'pager.current.size' : ${booksCommand.pager.current.size},
				'pager.sorter.column' : '${booksCommand.pager.sorter.column}',
				'pager.sorter.direction' : '${booksCommand.pager.sorter.direction}'
			}

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

			function sendPrev() {
				sendPost('prev', {})
			}

			function sendNext() {
				sendPost('next', {})
			}

			function sendSetPageSize() {
				var newSize = document.getElementsByClassName('setPageSizeInput')[0].value;
				sendPost('setPageSize', {
					'pager.current.size' : newSize
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
				updateParams(params, DEFAULT_PARAMS);
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

		</script>
	</head>
	<body>
		<@sectionTitle/>
		<@sectionMessages/>
		<@sectionPager/>
		<@sectionDataTable/>
		<@sectionCreate/>
	</body>
</html>

<#macro sectionTitle>
<h1>
	<#if size <= 0>
		No books for given range to display.
	<#else>
		<#if size == 1>
			Book ${offset + 1} of ${totalCount}:
		<#else>
			Books ${offset + 1}-${limit} of ${totalCount}:
		</#if>
	</#if>
</h1>
</#macro>

<#macro sectionMessages>
	<#if (booksCommand.message)??>
		${booksCommand.message}<br/><br/>
	</#if>
</#macro>

<#macro sectionPager>
	<table>
		<tr>
			<td align="left"><@formPagerPrev/></td>
			<td align="center"><@formPagerSetPageSize/></td>
			<td aling="right"><@formPagerNext/></td>
		</tr>
	</table>
</#macro>

<#macro formPagerPrev>
	<#if (booksCommand.pager.current.offset <= 0) >
		<input type="button" value="&lt;" onClick="sendPrev()" disabled="true" />
	<#else>
		<input type="button" value="&lt;" onClick="sendPrev()" />
	</#if>
</#macro>

<#macro formPagerSetPageSize>
	<@spring.formInput "booksCommand.pager.current.size" "class='setPageSizeInput'" />
	<input type="button" value="set page size" onClick="sendSetPageSize()" />
</#macro>

<#macro formPagerNext>
	<#if (limit >= totalCount) >
		<input type="button" value="&gt;" onClick="sendNext()" disabled="true" />
	<#else>
		<input type="button" value="&gt;" onClick="sendNext()" />
	</#if>
</#macro>

<#macro sectionDataTable>
	<#if (size <= 0) >
		<#return>
	<#else>
		<table><tr>
			<td>
				<table>
					<tr>
						<th></th>
						<th>Id</th>
						<th>
							Title
							<input type="button" value="^" onClick="sendSort('BOOK_TITLE', 'ASC')"/>
							<input type="button" value="v" onClick="sendSort('BOOK_TITLE', 'DESC')"/>
						</th>
					</tr>
					<#list booksCommand.books as book>
						<tr>
							<td>
								<@spring.formCheckbox path="booksCommand.books[" + book_index + "].checked" attributes="class='deleteCheckbox' bookId='${booksCommand.books[book_index].id}'"/>
							</td>
							<td>#${book.id}</td>
							<td>
								<@spring.formHiddenInput "booksCommand.books[" + book_index + "].version"/>
								<@spring.formInput "booksCommand.books[" + book_index + "].title"/>
								<input type="button" value="update" onClick="sendUpdate(${booksCommand.books[book_index].id}, ${book_index})"/>
							</td>
						</tr>
					</#list>
				</table>
			</td>
		</tr></table>
		<input type="button" value="delete selected" onClick="sendDelete()"/>
	</#if>
</#macro>

<#macro sectionCreate>
	<h2>Create new book:</h2>
	Title: <@spring.formInput "booksCommand.newBook.title"/>
	<input type="button" value="create" onClick="sendCreate()"/>
</#macro>
