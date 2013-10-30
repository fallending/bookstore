<#import "/spring.ftl" as spring>

<#assign offset = scrollParams.offset>
<#assign size = scrollParams.size>
<#assign totalCount = scrollParams.totalCount>

<html>
	<head>
		<title>Bookstore</title>
	</head>
	<body>
		<#if size <= 0>
			<h1>No books for given range to display.</h1>
		<#else>
			<#if size == 1>
				<h1>Book ${offset} of ${totalCount}:</h1>
			<#else>
				<#assign limit = offset + size - 1>
				<h1>Books ${offset}-${limit} of ${totalCount}:</h1>
			</#if>
			<table>
				<th>Title</th>
				<#list books as book>
					<tr>
						<td>${book.title}</td>
					</tr>
				</#list>
			</table>
		</#if>

		<form action="add" method="POST">
			<h2>Add new book:</h2>
			Title: <@spring.formInput "newBook.title"/>
			<@spring.formHiddenInput "scrollParams.offset"/>
			<@spring.formHiddenInput "scrollParams.size"/>
			<input type="submit" value="add"/>
		</form>
	</body>
</html>
