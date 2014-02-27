<#import "/spring.ftl" as spring>

<#assign pageNumber = booksCommand.pager.pageNumber>
<#assign pageSize = booksCommand.pager.pageSize>
<#assign pagesCount = booksCommand.pager.pagesCount>
<#assign totalCount = booksCommand.pager.totalCount>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
		<title>Bookstore</title>
		<style type="text/css">
			input.deleteCheckbox { }
		</style>
		<script type="text/javascript">
			ORIGINAL_PARAMS = {
				'pager.pageNumber' : ${booksCommand.pager.pageNumber},
				'pager.pageSize' : ${booksCommand.pager.pageSize},
				'pager.sorter.column' : '${booksCommand.pager.sorter.column}',
				'pager.sorter.direction' : '${booksCommand.pager.sorter.direction}'
			}
		</script>
		<script type="text/javascript" src="/js/books.js"></script>
	</head>
	<body>
		<@sectionTitle/>
		<@sectionMessages/>
		<#if (pagesCount > 0) >
			<@sectionPager/>
			<@sectionDataTable/>
		</#if>
		<@sectionCreate/>
	</body>
</html>

<#macro sectionTitle>
<h1>
	<#if totalCount <= 0>
		No books for given range to display.
	<#else>
		Page ${pageNumber} of ${pagesCount}:
	</#if>
</h1>
</#macro>

<#macro sectionMessages>
	<#list booksCommand.messages.infos as message>
		INFO: ${message}<br/>
	</#list>
	<#list booksCommand.messages.warns as message>
		WARN: ${message}<br/>
	</#list>
	<#list booksCommand.messages.errors as message>
		ERROR: ${message}<br/>
	</#list>
</#macro>

<#macro sectionPager>
	<table>
		<tr>
			<td align="left">Page size: <@formPagerSetPageSize/></td>
			<td align="center">Go to page:</td>
			<td align="left"><@formPagerPrev/></td>
			<td align="center"><@formPagerGoToPage/></td>
			<td aling="right"><@formPagerNext/></td>
		</tr>
	</table>
</#macro>

<#macro formPagerPrev>
	<#if (pageNumber <= 1) >
		<input type="button" value="&#x25C0;" disabled="true" />
	<#else>
		<input type="button" value="&#x25C0;" onClick="sendGoToPage(${pageNumber - 1})" />
	</#if>
</#macro>

<#macro formPagerSetPageSize>
	<#assign possibleSizes = {"1":1, "2":2, "5":5, "10":10, "15":15, "25":25, "50":50, "100":100}>
	<@spring.formSingleSelect "booksCommand.pager.pageSize" possibleSizes "onChange='sendSetPageSize()'" />
</#macro>

<#macro formPagerNext>
	<#if (pageNumber >= pagesCount) >
		<input type="button" value="&#x25B6;" disabled="true" />
	<#else>
		<input type="button" value="&#x25B6;" onClick="sendGoToPage(${pageNumber + 1})" />
	</#if>
</#macro>

<#macro formPagerGoToPage>
	<#assign min = pageNumber - 2 />
	<#assign max = pageNumber + 2 />

	<#if (min < 1) >
		<#assign max = max + (-min + 1) />
		<#assign min = 1 />
	</#if>

	<#if (max > pagesCount) >
		<#assign min = min - (max - pagesCount) />
		<#assign max = pagesCount />
	</#if>

	<#if (min < 1 || max < min ) >
		<#assign min = 1 />
	</#if>

	<#if (min > 1) >
		<#assign min = min + 1 />
		<#assign leftDots = true />
	</#if>

	<#if (max < pagesCount) >
		<#assign max = max - 1 />
		<#assign rightDots = true />
	</#if>

	<#if (leftDots??) >...</#if>
	<#list min..max as p>
		<#if (p = pageNumber) >
			<input type="button" value="${p}" disabled="true" />
		<#else>
			<input type="button" value="${p}" onClick="sendGoToPage(${p})" />
		</#if>
	</#list>
	<#if (rightDots??) >...</#if>
</#macro>

<#macro sectionDataTable>
	<table><tr>
		<td>
			<table>
				<tr>
					<th></th>
					<th>Id</th>
					<th>
						<@sectionDataTableSorter 'BOOK_TITLE' 'Title'/>
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
							<@spring.showErrors "<br>" />
							<input type="button" value="update" onClick="sendUpdate(${booksCommand.books[book_index].id}, ${book_index})"/>
						</td>
					</tr>
				</#list>
			</table>
		</td>
	</tr></table>
	<input type="button" value="delete selected" onClick="sendDelete()"/>
</#macro>

<#macro sectionDataTableSorter columnName columnTitle>
	<@sectionDataTableSorterDirection columnName columnTitle 'DESC' '&#x25BC'/>
	${columnTitle}
	<@sectionDataTableSorterDirection columnName columnTitle 'ASC' '&#x25B2'/>
</#macro>

<#macro sectionDataTableSorterDirection columnName columnTitle direction marker>
	<#if (booksCommand.pager.sorter.direction = '${direction}')>
		<input type="button" value="${marker}" disabled="true"/>
	<#else>
		<input type="button" value="${marker}" onClick="sendSort('${columnName}', '${direction}')"/>
	</#if>
</#macro>

<#macro sectionCreate>
	<h2>Create new book:</h2>
	Title: <@spring.formInput "booksCommand.newBook.title"/>
	<input type="button" value="create" onClick="sendCreate()"/>
</#macro>
