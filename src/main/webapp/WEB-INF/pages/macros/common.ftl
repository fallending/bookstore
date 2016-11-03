<#macro sectionEncodingHeaders>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</#macro>

<#macro sectionUserBar>
	<div class="sectionUserBar">
		<#if Session['SPRING_SECURITY_CONTEXT']??>
			<a href="<@spring.url '/auth/logout'/>">Logout</a>
			"${Session['SPRING_SECURITY_CONTEXT'].authentication.name}"
		<#else>
			Not logged in.
		</#if>
	</div>
</#macro>

<#macro sectionTitle>
	<h1 class="sectionTitle">Bookstore</h1>
</#macro>

<#macro sectionFooter>
	<div class="sectionFooter">2013-2014 Pawe&#x0142 Jojczyk, GNU GPL v3.0</div>
</#macro>
