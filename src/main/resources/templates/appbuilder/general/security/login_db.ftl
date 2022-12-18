<#-- @ftlvariable name="klate" type="emesday.klate.freemarker.KlateTemplateModel" -->
<#-- @ftlvariable name="title" type="String" -->
<#-- @ftlvariable name="form" type="emesday.klate.form.LoginForm" -->

<#import "../../extends.ftl" as layout>

<@layout.extends "base.ftl">
    <@layout.replace "content">
        <div class="container">
            <div id="loginbox" style="margin-top:50px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-primary" >
                    <div class="panel-heading">
                        <div class="panel-title">${title}</div>
                    </div>
                    <div style="padding-top:30px" class="panel-body" >

                        <form class="form" action="" method="post" name="login">
                            ${form.hiddenTag()}
                            <div class="help-block">${klate._("Enter your login and password below")}:</div>
                            <div class="control-group{% if form.errors.openid is defined %} error{% endif %}">
                                <label class="control-label" for="username">${klate._("Username")}:</label>

                                <div class="controls">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-user"></i></span>
<#--                                        ${ form.username(size = 80, class = "form-control", autofocus = true) }-->
                                    </div>
<#--                                    <#list form.errors.get('openid', []) as error>-->
<#--                                        <span class="help-inline">[${error}]</span><br>-->
<#--                                    </#list>-->
                                    <label class="control-label" for="password">${klate._("Password")}:</label>

                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-key"></i></span>
<#--                                        ${ form.password(size = 80, class = "form-control") }-->
                                    </div>
<#--                                    <#list form.errors.get('openid', []) as error>-->
<#--                                    <span class="help-inline">[${error}]</span><br>-->
<#--                                    </#list>-->
                                </div>
                            </div>

                            <div class="control-group">
                                <div class="controls">
                                    <br>
                                    <div>
                                        <input class="btn btn-primary btn-block" type="submit" value="${klate._('Sign In')}">
                                        <#if klate.securityManager.authUserRegistration>
                                        <a href="${klate.securityManager.urlForRegisterUser}" class="btn btn-block btn-primary" data-toggle="tooltip" rel="tooltip"
                                           title="${klate._('If you are not already a user, please register')}">
                                            ${klate._('Register')}
                                        </a>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </@layout.replace>
</@layout.extends>
