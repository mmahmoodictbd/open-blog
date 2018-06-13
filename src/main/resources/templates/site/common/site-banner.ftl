<header id="masthead" class="container" role="banner">

    <div class="home-link">
        <div class="site-title">
            <a href="${(site.homeUrl)!}" title="${(site.name)!}" rel="home">${(site.name)!}</a>
        </div>
        <div class="site-description">${(site.description)!}</div>

        <div class="social">

            <a href="/feed.atom" title="Subscribe to RSS"><i class="fas fa-rss" style="font-size:24px"></i></a>

            <#if site.additionalProperties['socialLinkedIn']?has_content>
                <a href="${(site.additionalProperties['socialLinkedIn'])!}" target="_blank"
                   title="Find me on LinkedIn"><i class="fab fa-linkedin" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialGithub']?has_content>
                <a href="${(site.additionalProperties['socialGithub'])!}" target="_blank"
                   title="Follow me on Github"><i class="fab fa-github-alt" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialFB']?has_content>
                <a href="${(site.additionalProperties['socialFB'])!}" target="_blank"
                   title="Follow me on Facebook"><i class="fab fa-facebook" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialTwitter']?has_content>
                <a href="${(site.additionalProperties['socialTwitter'])!}" target="_blank"
                   title="Follow me on Twitter"><i class="fab fa-twitter" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialGooglePlus']?has_content>
                <a href="${(site.additionalProperties['socialGooglePlus'])!}" target="_blank"
                   title="Follow me on Google Plus"><i class="fab fa-google-plus" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialWikiPage']?has_content>
                <a href="${(site.additionalProperties['socialWikiPage'])!}" target="_blank"
                   title="Check out my tech notes"><i class="fab fa-wikipedia-w" style="font-size:24px"></i></a>
            </#if>

            <#if site.additionalProperties['socialEmailMe']?has_content>
                <a href="mailto:${(site.additionalProperties['socialEmailMe'])!}" rel="nofollow"
                   title="Email me!"><i class="far fa-envelope" style="font-size:24px"></i></a>
            </#if>

            <a href="/admin/dashboard" rel="nofollow"
               title="Admin Panel"><i class="far fa-user" style="font-size:24px"></i></a>


        </div>
    </div>

</header>
