<!-- Sidebar Widgets Column -->

<#if categories?has_content>

    <div class="container categories d-none d-sm-block" style="line-height: 1.7em;">

        <h5 class="">Categories</h5>

        <ul class="list-inline mb-0">
            <#list categories as category>
                <li class="list-inline-item">
                    <a href="?category=${(category.name)!}">${(category.name)!}</a>
                </li>
            </#list>
        </ul>

    </div>

</#if>

<div class="row">&nbsp;</div>

<#if tags?has_content>

    <div class="container categories d-none d-sm-block" style="line-height: 1.7em;">

        <h5 class="">Tags</h5>

        <ul class="list-inline mb-0">
            <#list tags as tag>
                <li class="list-inline-item">
                    <a href="?tag=${(tag.name)!}">${(tag.name)!}</a>
                </li>
            </#list>
        </ul>

    </div>

</#if>