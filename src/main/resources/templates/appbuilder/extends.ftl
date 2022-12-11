<#if !blocks??>
    <#assign blocks = {} />
</#if>

<#macro extends ftl>
    <#nested />
    <#include ftl />
</#macro>

<#macro replace name>
    <#local value>
        <#nested />
    </#local>
    <#assign blocks += {name: value} />
</#macro>

<#macro block name>
    <#if blocks[name]??>
        <!-- provided ${name} -->
        ${blocks[name]}
    <#else>
        <!-- default ${name} -->
        <#nested />
    </#if>
</#macro>
