<#import "/spring.ftl" as spring />
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
				<h1>Books ${offset}-${offset + size - 1} of ${totalCount}:</h1>
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
	</body>
</html>
