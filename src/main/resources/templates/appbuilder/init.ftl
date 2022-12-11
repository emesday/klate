<#--{% import 'appbuilder/baselib.html' as baselib with context %}-->
<#---->
<#if appbuilder??>
  <#assign app_name = appbuilder.app_name />
<#else>
  <#assign app_name = "Awesome" />
</#if>
<#import "extends.ftl" as layout>

<!DOCTYPE html>
<html>
  <head>
    <title><@layout.block "page_title">${app_name}</@layout.block></title>
    <@layout.block "head_meta">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <meta name="description" content="">
      <meta name="author" content="">
    </@layout.block>

    <@layout.block "head_css">
      <link href="${static('css/bootstrap.min.css')}" rel="stylesheet">
      <link href="${static('css/font-awesome.min.css')}" rel="stylesheet">
      <#if (appbuilder.app_theme)?has_content>
        <link href="${static('css/themes/'+ appbuilder.app_theme )}" rel="stylesheet">
      </#if>
      <link href="${static('datepicker/bootstrap-datepicker.css')}" rel="stylesheet">
      <link href="${static('select2/select2.css')}" rel="stylesheet">
      <link href="${static('css/flags/flags16.css')}" rel="stylesheet">
      <link href="${static('css/ab.css')}" rel="stylesheet">
    </@layout.block>

    <@layout.block "head_js">
      <script src="${static('js/jquery-latest.js')}"></script>
      <script src="${static('js/ab_filters.js')}"></script>
      <script src="${static('js/ab_actions.js')}"></script>
    </@layout.block>
  </head>
  <body >
    <@layout.block "body" />

    <@layout.block "tail_js">
      <script src="${static('js/bootstrap.min.js')}"></script>
      <script src="${static('datepicker/bootstrap-datepicker.js')}"></script>
      <script src="${static('select2/select2.js')}"></script>
      <script src="${static('js/ab.js')}"></script>
    </@layout.block>
    <@layout.block "add_tail_js" />
    <@layout.block "tail" />
  </body>
</html>
