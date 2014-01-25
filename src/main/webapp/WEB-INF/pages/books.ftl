<#import "/spring.ftl" as spring>

<#assign offset = booksCommand.scroll.limited.offset>
<#assign size = booksCommand.scroll.limited.size>
<#assign totalCount = booksCommand.scroll.totalCount>
<#assign limit = offset + size>
<#assign isEdit = true>

<!DOCTYPE html>
<html>
	<head>
		<title>Bookstore</title>
		<script type="text/javascript">

			function sendUpdate(bookId) {
				var version = document.getElementById('books' + bookId + '.version').value;
				var title = document.getElementById('books' + bookId + '.title').value;
				sendPost('update', {
					'updateBookId' : bookId,
					'updateBookVersion' : version,
					'updateBookTitle' : title
				})
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
				<form action="del" method="POST" id="formDel">
					<table>
						<th>&nbsp;</th>
						<#list booksCommand.books as book>
							<tr>
								<td>
									<@spring.formHiddenInput path="booksCommand.books[" + book_index + "].id"/>
									<@spring.formCheckbox path="booksCommand.books[" + book_index + "].checked"/>
								</td>
							</tr>
						</#list>
					</table>
					<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
					<@spring.formHiddenInput "booksCommand.scroll.current.size"/>
				</form>
			</td>
			<td>
				<table>
					<th>Id</th><th>Title</th>
					<#list booksCommand.books as book>
						<tr>
							<td>#${book.id}</td>
							<td>
							<#if isEdit>
								<@spring.formHiddenInput "booksCommand.books[" + book_index + "].version"/>
								<@spring.formInput "booksCommand.books[" + book_index + "].title"/>
								<input type="button" value="update" onClick="sendUpdate(${booksCommand.books[book_index].id})"/>
							<#else>
								${book.title}
							</#if>
							</td>
						</tr>
					</#list>
				</table>
			</td>
		</tr></table>
		<input type="button" value="delete selected" onClick="document.getElementById('formDel').submit()"/>
	</#if>
</#macro>

<#macro sectionAdd>
	<form action="add" method="POST">
		<h2>Add new book:</h2>
		Title: <@spring.formInput "booksCommand.newBook.title"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.size"/>
		<input type="submit" value="add"/>
	</form>
</#macro>
