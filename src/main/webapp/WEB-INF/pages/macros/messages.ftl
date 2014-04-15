<#--
 Copyright (C) 2013-2014 Paweł Jojczyk

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
-->

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
