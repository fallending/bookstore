<#import "/spring.ftl" as spring>

<#assign pageNumber = booksCommand.pager.pageNumber>
<#assign pageSize = booksCommand.pager.pageSize>
<#assign pagesCount = booksCommand.pager.pagesCount>
<#assign totalCount = booksCommand.pager.totalCount>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
	<head>
		<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
		<title>Bookstore</title>
		<link rel="stylesheet" type="text/css" href="/css/books.css"/>
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
		<div class="all">
			<@sectionTitle/>
			<div class="sectionMain">
				<@sectionMessages/>
				<#if (pagesCount > 0) >
					<@sectionPager/>
					<@sectionDataTable/>
				<#else>
					No books to display.
				</#if>
				<@sectionCreate/>
			</div>
		</div>
	</body>
</html>

<#macro sectionTitle>
	<h1 class="sectionTitle">
		Bookstore
	</h1>
</#macro>

<#macro sectionMessages>
	<@sectionSingleKindMessages booksCommand.messages.infos 'sectionMessagesInfo' 'info'/>
	<@sectionSingleKindMessages booksCommand.messages.warns 'sectionMessagesWarn' 'warn'/>
	<@sectionSingleKindMessages booksCommand.messages.errors 'sectionMessagesError' 'error'/>
</#macro>

<#macro sectionSingleKindMessages collection class image>
	<#if (collection?size > 0) >
		<div class="${class}">
			<table><tr>
				<td align="center" valign="center" width="50"><img src="/img/${image}.png"/></td>
				<td><ul>
					<#list collection as message>
						<li>${message}</li>
					</#list>
				</ul></td>
			</tr></table>
		</div>
	</#if>
</#macro>

<#macro sectionPager>
	<div class="sectionPager">
		<table>
			<tr>
				<td align="left">Page size: <@formPagerSetPageSize/></td>
				<td aling="left">
					Total pages:
					<input type="text" value="${pagesCount}" disabled="true" class="pagesCount"/>
				</td>
				<td align="center">Go to page:</td>
				<td align="left"><@formPagerPrev/></td>
				<td align="center"><@formPagerGoToPage/></td>
				<td aling="right"><@formPagerNext/></td>
			</tr>
		</table>
	</div>
</#macro>

<#macro formPagerPrev>
	<#if (pageNumber <= 1) >
		<input type="button" value="&#x25C0;" class="arrowsButtons" disabled="true" />
	<#else>
		<input type="button" value="&#x25C0;" class="arrowsButtons" onClick="sendGoToPage(${pageNumber - 1})" />
	</#if>
</#macro>

<#macro formPagerSetPageSize>
	<#assign possibleSizes = {"1":1, "2":2, "3":3, "5":5, "10":10, "15":15, "25":25, "50":50, "100":100}>
	<@spring.formSingleSelect "booksCommand.pager.pageSize" possibleSizes "class='setPageSizeInput' onChange='sendSetPageSize()'" />
</#macro>

<#macro formPagerNext>
	<#if (pageNumber >= pagesCount) >
		<input type="button" value="&#x25B6;" class="arrowsButtons" disabled="true" />
	<#else>
		<input type="button" value="&#x25B6;" class="arrowsButtons" onClick="sendGoToPage(${pageNumber + 1})" />
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

	<table cellspacing="0" cellpadding="0"><tr>
	<#if (leftDots??) ><td class="pagerScrollCell">...</td></#if>
	<#list min..max as p>
		<#if (p = pageNumber) >
			<td class="pagerScrollCell">
				<input type="button" value="${p}" class="pageNumberButton" disabled="true" />
			</td>
		<#else>
			<td class="pagerScrollCell">
				<input type="button" value="${p}" class="pageNumberButton" onClick="sendGoToPage(${p})" />
			</td>
		</#if>
	</#list>
	<#if (rightDots??) ><td class="pagerScrollCell">...</td></#if>
	</tr></table>
</#macro>

<#macro sectionDataTable>
	<div class="sectionDataTable">
		<table>
			<tr>
				<th class="deleteCheckboxHeader"></th>
				<th class="idHeader">Id</th>
				<th class="titleHeader">
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
						<@spring.formInput "booksCommand.books[" + book_index + "].title" "class='updateInput'"/>
						<input type="button" value="update" onClick="sendUpdate(${booksCommand.books[book_index].id}, ${book_index})"/>
					</td>
				</tr>
			</#list>
		</table>
	</div>
	<div class="sectionDataTableButtons">
		<input type="button" value="delete selected" class="deleteButton" onClick="sendDelete()"/>
	</div>
</#macro>

<#macro sectionDataTableSorter columnName columnTitle>
	<@sectionDataTableSorterDirection columnName columnTitle 'DESC' '&#x25BC'/>
	${columnTitle}
	<@sectionDataTableSorterDirection columnName columnTitle 'ASC' '&#x25B2'/>
</#macro>

<#macro sectionDataTableSorterDirection columnName columnTitle direction marker>
	<#if (booksCommand.pager.sorter.direction = '${direction}')>
		<input type="button" value="${marker}" class="arrowsButtons" disabled="true"/>
	<#else>
		<input type="button" value="${marker}" class="arrowsButtons" onClick="sendSort('${columnName}', '${direction}')"/>
	</#if>
</#macro>

<#macro sectionCreate>
	<div class="sectionCreate">
		<h3>Create new book:</h3>
		Title: <@spring.formInput "booksCommand.newBook.title" "class='createInput'"/>
		<input type="button" value="create" class="createButton" onClick="sendCreate()"/>
	</div>
</#macro>
