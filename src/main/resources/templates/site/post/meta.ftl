<div class="d-inline">
    <i class="far fa-clock fa-fw" style="font-size:16px"></i>&nbsp;${post.createdAt?datetime?string('MMM dd yyyy')}
</div>

&nbsp;&nbsp;

<#if post.categories?has_content>
    <div class="d-inline">
        <i class="far fa-folder-open fa-fw" style="font-size:16px"></i>
        <#list post.categories?split(",") as category>
            <a href="../?category=${(category)!}">${(category)!}</a>
        </#list>
    </div>
</#if>

&nbsp;&nbsp;

<#if post.tags?has_content>
    <div class="d-inline">
        <i class="fas fa-tags fa-fw" style="font-size:16px"></i>
        <#list post.tags?split(",") as tag>
            <a href="../?category=${(tag)!}">${(tag)!}</a>
        </#list>
    </div>
</#if>