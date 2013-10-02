<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
	<h1>Books [${offset} - ${offset + size - 1}]:</h1>
	<table>
		<th>Title</th>
		<c:forEach items="${books}" var="book">
			<tr>
				<td><c:out value="${book.title}"/></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
