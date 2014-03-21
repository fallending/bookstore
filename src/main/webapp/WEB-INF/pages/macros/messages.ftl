<#macro sectionSingleKindMessages collection class image>
	<#if (collection?size > 0) >
		<div class="${class}">
			<table><tr>
				<td align="center" valign="center" width="50"><img src="/img/public/${image}.png"/></td>
				<td><ul>
					<#list collection as message>
						<li>${message}</li>
					</#list>
				</ul></td>
			</tr></table>
		</div>
	</#if>
</#macro>
