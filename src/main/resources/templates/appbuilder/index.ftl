<#-- @ftlvariable name="klate" type="emesday.klate.KlateSharedTemplateModel" -->

<#import "extends.ftl" as layout>

<@layout.extends "base.ftl">
    <@layout.replace "content">
        <h2><center>${klate._('Welcome')}<center></h2>
        <div class="well">
        </div>
    </@layout.replace>
</@layout.extends>