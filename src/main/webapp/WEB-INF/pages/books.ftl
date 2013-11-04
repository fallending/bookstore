<#import "/spring.ftl" as spring>

<#assign offset = booksCommand.limitedScrollParams.offset>
<#assign size = booksCommand.limitedScrollParams.size>
<#assign totalCount = booksCommand.totalCount>
<#assign limit = offset + size>

<html>
	<head>
		<title>Bookstore</title>
	</head>
	<body>
		originalScrollParams = ${booksCommand.originalScrollParams}<br/>
		totalCount = ${booksCommand.totalCount}<br/>
		limitedScrollParams = ${booksCommand.limitedScrollParams}<br/>
		<@sectionTitle/>
		<@sectionScroll/>
		<@sectionTable/>
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
		<@spring.formHiddenInput "booksCommand.originalScrollParams.offset"/>
		<@spring.formHiddenInput "booksCommand.originalScrollParams.size"/>
		<@spring.formHiddenInput "booksCommand.pageSize"/>
		<#if (booksCommand.originalScrollParams.offset <= 0) >
			<input type="submit" value="&lt;" disabled="true" />
		<#else>
			<input type="submit" value="&lt;" />
		</#if>
	</form>
</#macro>

<#macro formScrollSetPageSize>
	<form action="setPageSize" method="POST">
		<@spring.formHiddenInput "booksCommand.originalScrollParams.offset"/>
		<@spring.formHiddenInput "booksCommand.originalScrollParams.size"/>
		<@spring.formInput "booksCommand.pageSize"/>
		<input type="submit" value="set page size" />
	</form>
</#macro>

<#macro formScrollNext>
	<form action="next" method="POST">
		<@spring.formHiddenInput "booksCommand.originalScrollParams.offset"/>
		<@spring.formHiddenInput "booksCommand.originalScrollParams.size"/>
		<@spring.formHiddenInput "booksCommand.pageSize"/>
		<#if (limit >= totalCount) >
			<input type="submit" value="&gt;" disabled="true" />
		<#else>
			<input type="submit" value="&gt;" />
		</#if>
	</form>
</#macro>

<#macro sectionTable>
	<#if (size > 0) >
	<table>
		<th>Id</th><th>Title</th>
		<#list booksCommand.books as book>
			<tr>
				<td>#${book.id}</td>
				<td>${book.title}</td>
			</tr>
		</#list>
	</table>
	</#if>
</#macro>

<#macro sectionAdd>
	<form action="add" method="POST">
		<h2>Add new book:</h2>
		Title: <@spring.formInput "booksCommand.newBook.title"/>
		<@spring.formHiddenInput "booksCommand.originalScrollParams.offset"/>
		<@spring.formHiddenInput "booksCommand.originalScrollParams.size"/>
		<@spring.formHiddenInput "booksCommand.pageSize"/>
		<input type="submit" value="add"/>
	</form>
</#macro>
