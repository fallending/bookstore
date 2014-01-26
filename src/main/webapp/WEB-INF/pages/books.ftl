<#import "/spring.ftl" as spring>

<#assign offset = booksCommand.scroll.limited.offset>
<#assign size = booksCommand.scroll.limited.size>
<#assign totalCount = booksCommand.scroll.totalCount>
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

			function sendCreate() {
				var title = document.getElementById('newBook.title').value;
				sendPostWithDefaults('create', {
					'newBook.title' : title
				})
			}

			function sendUpdate(id, index) {
				var version = document.getElementById('books' + index + '.version').value;
				var title = document.getElementById('books' + index + '.title').value;
				sendPostWithDefaults('update', {
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
				sendPostWithDefaults('delete', params)
			}

			function sendPrev() {
				sendPostWithDefaults('prev', {})
			}

			function sendNext() {
				sendPostWithDefaults('next', {})
			}

			function sendSetPageSize() {
				var params = {};
				var newSize = document.getElementsByClassName('setPageSizeInput')[0].value;
				params['scroll.current.offset'] = ${booksCommand.scroll.current.offset};
				params['scroll.current.size'] = newSize;
				sendPost('setPageSize', params)
			}

			function sendPostWithDefaults(action, params) {
				sendPost(action, decorateWithDefaults(params));
			}

			function decorateWithDefaults(params) {
				params['scroll.current.offset'] = ${booksCommand.scroll.current.offset};
				params['scroll.current.size'] = ${booksCommand.scroll.current.size};
				return params;
			}

			function sendPost(action, params) {
				var form = createFormFor(action)

				for(var key in params) {
					if(params.hasOwnProperty(key)) {
						form.appendChild(createHiddenInput(key, params[key]));
					}
				}

				document.body.appendChild(form);
				form.submit();
			}

			function createFormFor(action) {
				var form = document.createElement("form");
				form.setAttribute("method", "post");
				form.setAttribute("action", action);

				return form;
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
		<@sectionScroll/>
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

<#macro sectionScroll>
	<table>
		<tr>
			<td align="left"><@formScrollPrev/></td>
			<td align="center"><@formScrollSetPageSize/></td>
			<td aling="right"><@formScrollNext/></td>
		</tr>
	</table>
</#macro>

<#macro formScrollPrev>
	<#if (booksCommand.scroll.current.offset <= 0) >
		<input type="button" value="&lt;" onClick="sendPrev()" disabled="true" />
	<#else>
		<input type="button" value="&lt;" onClick="sendPrev()" />
	</#if>
</#macro>

<#macro formScrollSetPageSize>
	<@spring.formInput "booksCommand.scroll.current.size" "class='setPageSizeInput'" />
	<input type="button" value="set page size" onClick="sendSetPageSize()" />
</#macro>

<#macro formScrollNext>
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
					<th></th><th>Id</th><th>Title</th>
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
