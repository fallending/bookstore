<#import "/spring.ftl" as spring>

<#assign offset = booksCommand.scroll.limited.offset>
<#assign size = booksCommand.scroll.limited.size>
<#assign totalCount = booksCommand.scroll.totalCount>
<#assign limit = offset + size>

<html>
	<head>
		<title>Bookstore</title>
	</head>
	<body>
		<@sectionTitle/>
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
	<#if (size > 0) >
	<form action="del" method="POST">
		<table>
			<th></th><th>Id</th><th>Title</th>
			<#list booksCommand.books as book>
				<tr>
					<td>
						<@spring.formHiddenInput path="booksCommand.books[" + book_index + "].id"/>
						<@spring.formCheckbox path="booksCommand.books[" + book_index + "].checked"/>
					</td>
					<td>#${book.id}</td>
					<td>${book.title}</td>
				</tr>
			</#list>
		</table>
		<br/>
		<input type="submit" value="delete selected"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.offset"/>
		<@spring.formHiddenInput "booksCommand.scroll.current.size"/>
	</form>
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
