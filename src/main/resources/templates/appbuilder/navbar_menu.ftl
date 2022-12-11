<#macro menu_item item>
    <a tabindex="-1" href="${item.get_url()}">
        <#if (item.icon)??>
            <i class="fa fa-fw ${item.icon}"></i>&nbsp;
        </#if>
        ${_(item.label)}
    </a>
</#macro>

<#list menu.get_list() as item1 >
    {% if item1 | is_menu_visible %}
        {% if item1.childs %}
            <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">
            {% if item1.icon %}
                <i class="fa {{item1.icon}}"></i>&nbsp;
            {% endif %}
            {{_(item1.label)}}<b class="caret"></b></a>
            <ul class="dropdown-menu">
            {% for item2 in item1.childs %}
                {% if item2 %}
                    {% if item2.name == '-' %}
                        {% if not loop.last %}
                          <li class="divider"></li>
                        {% endif %}
                    {% elif item2 | is_menu_visible %}
                        <li>{{ menu_item(item2) }}</li>
                    {% endif %}
                {% endif %}
            {% endfor %}
            </ul></li>
        {% else %}
            <li>
                {{ menu_item(item1) }}
            </li>
        {% endif %}
    {% endif %}
</#list>