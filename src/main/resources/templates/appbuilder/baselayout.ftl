<#import "extends.ftl" as layout>

<#--{% extends 'appbuilder/init.html' %}-->
<#--{% import 'appbuilder/baselib.html' as baselib %}-->

<@layout.extends "init.ftl">
    <@layout.replace "body">
        <#include 'general/confirm.html' />
        <#include 'general/alert.html' />
        <@layout.block "navbar">
            <header class="top" role="header">
                <#include "navbar.ftl" />
            </header>
        </@layout.block>
        <div class="container">
            <div class="row">
                <@layout.block "messages">
                    <#include 'flash.ftl' />
                </@layout.block>
                <@layout.block "content" />
            </div>
        </div>

        <@layout.block "footer">
            <footer>
                <div class="img-rounded nav-fixed-bottom">
                    <div class="container">
                        <#include "footer.ftl" />
                    </div>
                </div>
            </footer>
        </@layout.block>
    </@layout.replace>
</@layout.extends>
