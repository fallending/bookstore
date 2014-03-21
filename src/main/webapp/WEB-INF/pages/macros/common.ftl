<#macro sectionUserBar>
	<div class="sectionUserBar">
		<#if Session['SPRING_SECURITY_CONTEXT']??>
			<a href="/security/logout">
				Logout "${Session['SPRING_SECURITY_CONTEXT'].authentication.name}"
			</a>
		<#else>
			Not logged in.
		</#if>
	</div>
</#macro>

<#macro sectionTitle>
	<h1 class="sectionTitle">Bookstore</h1>
</#macro>
