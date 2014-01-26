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
			input.delCheckbox { }
		</style>
		<script type="text/javascript">

			function sendAdd() {
				var title = document.getElementById('newBook.title').value;
				sendPost('add', {
					'newBook.title' : title
				})
			}

			function sendUpdate(bookId, bookIndex) {
				var version = document.getElementById('books' + bookIndex + '.version').value;
				var title = document.getElementById('books' + bookIndex + '.title').value;
				sendPost('update', {
					'updateBookId' : bookId,
					'updateBookVersion' : version,
					'updateBookTitle' : title
				})
			}

			function sendDel() {
				var params = {};
				var checkboxes = document.getElementsByClassName('delCheckbox');
				for (var bookIndex = 0; bookIndex < checkboxes.length; bookIndex++) {
					if (checkboxes[bookIndex].checked) {
						params[('books[' + bookIndex + '].id')] = checkboxes[bookIndex].getAttribute('bookId')
						params[('books[' + bookIndex + '].checked')] = 'on';
					}
				}
				sendPost('del', params)
			}

			function sendPost(action, paramsMap) {
				var form = createFormFor(action)

				addDefaultParamsTo(form);

				for(var key in paramsMap) {
					if(paramsMap.hasOwnProperty(key)) {
						form.appendChild(createHiddenInput(key, paramsMap[key]));
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

			function addDefaultParamsTo(form) {
				form.appendChild(createHiddenInput('scroll.current.offset', ${booksCommand.scroll.current.offset}));
				form.appendChild(createHiddenInput('scroll.current.size', ${booksCommand.scroll.current.size}));
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
		<@sectionAdd/>
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
	<form action="prev" method="POST">
		<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.size"/>
		<#if (booksCommand.scroll.current.offset <= 0) >
			<input type="submit" value="&lt;" disabled="true" />
		<#else>
			<input type="submit" value="&lt;" />
		</#if>
	</form>
</#macro>

<#macro formScrollSetPageSize>
	<form action="setPageSize" method="POST">
		<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
		<@spring.formInput "booksCommand.scroll.current.size"/>
		<input type="submit" value="set page size" />
	</form>
</#macro>

<#macro formScrollNext>
	<form action="next" method="POST">
		<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.size"/>
		<#if (limit >= totalCount) >
			<input type="submit" value="&gt;" disabled="true" />
		<#else>
			<input type="submit" value="&gt;" />
		</#if>
	</form>
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
								<@spring.formCheckbox path="booksCommand.books[" + book_index + "].checked" attributes="class='delCheckbox' bookId='${booksCommand.books[book_index].id}'"/>
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
		<input type="button" value="delete selected" onClick="sendDel()"/>
	</#if>
</#macro>

<#macro sectionAdd>
	<h2>Add new book:</h2>
	Title: <@spring.formInput "booksCommand.newBook.title"/>
	<input type="button" value="add" onClick="sendAdd()"/>
</#macro>
