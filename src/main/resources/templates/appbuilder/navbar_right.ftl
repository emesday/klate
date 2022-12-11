<#--{% macro locale_menu(languages) %}-->
<#--{% set locale = session['locale'] %}-->
<#if locale??>
    <#assign locale = 'en' />
</#if>
{% if languages.keys()|length > 1 %}
<li class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">
       <div class="f16"><i class="flag {{languages[locale].get('flag')}}"></i><b class="caret"></b>
       </div>
    </a>
    <ul class="dropdown-menu">
    <li class="dropdown">
        {% for lang in languages %}
            {% if lang != locale %}
                <a tabindex="-1" href="{{appbuilder.get_url_for_locale(lang)}}">
                  <div class="f16"><i class="flag {{languages[lang].get('flag')}}"></i> - {{languages[lang].get('name')}}
                </div></a>
            {% endif %}
        {% endfor %}
        </li>
        </ul>
</li>
{% endif %}
{% endmacro %}

<#--
{{ locale_menu(languages) }}
{% if not current_user.is_anonymous %}
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
           <span class="fa fa-user"></span> {{g.user.get_full_name()}}<b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
            <li><a href="{{appbuilder.get_url_for_userinfo}}"><span class="fa fa-fw fa-user"></span>{{_("Profile")}}</a></li>
            <li><a href="{{appbuilder.get_url_for_logout}}"><span class="fa fa-fw fa-sign-out"></span>{{_("Logout")}}</a></li>
        </ul>
    </li>
{% else %}
    <li><a href="{{appbuilder.get_url_for_login}}">
    <i class="fa fa-fw fa-sign-in"></i>{{_("Login")}}</a></li>
{% endif %}
-->