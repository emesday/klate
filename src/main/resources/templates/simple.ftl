<#-- @ftlvariable name="item" type="emesday.plugins.View" -->
<#-- @ftlvariable name="menu" type="emesday.plugins.Menu" -->
<#macro menuItem item>
    <a tabindex="-1" href="${item.getUrl()}">
        <#if item.icon?has_content>
            <i class="fa fa-fw ${item.icon}"></i>&nbsp;
        </#if>
        ${item.gettext()}
    </a>
</#macro>

<!DOCTYPE html>
<html lang="en">
<head>
<#--    <title>${item.title}</title>-->
</head>
<body>

<#list menu.items as item>
    <#if item.isVisible()>
        <#if item.hasChildren()>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">
                    <#if item.icon?has_content>
                        <i class="fa ${item.icon}"></i>&nbsp;
                    </#if>
                    ${item.gettext()}<b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <#list item.children as child>
                        <#if child.label == "-">
                            <#if !child?is_last>
                                <li class="divider"></li>
                            </#if>
                        <#elseif child.isVisible()>
                            <@menuItem child />
                        </#if>
                    </#list>
                </ul>
            </li>
        <#else>
            <@menuItem item />
        </#if>
    </#if>
</#list>

</body>
</html>