<#import "/spring.ftl" as spring />
<html>
	<head>
		<title>Bookstore</title>
	</head>
	<body>
		<h1>Books ${offset}-${offset + size - 1} of ${totalCount}:</h1>
		<table>
			<th>Title</th>
			<#list books as book>
				<tr>
					<td>${book.title}</td>
				</tr>
			</#list>
		</table>
	</body>
</html>
