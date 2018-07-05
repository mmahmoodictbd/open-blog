<header id="masthead" class="container" role="banner">

    <div class="home-link">
        <div class="site-title">
            <a href="${(site.homeUrl)!}" title="${(site.name)!}" rel="home">${(site.name)!}</a>
        </div>
        <div class="site-description">${(site.description)!}</div>

        <div class="social">

            <a href="/feed.atom" title="Subscribe to RSS"><i class="fas fa-rss" style="font-size:24px"></i></a>

            <#if site.socialLinkedInUrl?has_content>
                <a href="${(site.socialLinkedInUrl)!}" target="_blank"
                   title="Find me on LinkedIn"><i class="fab fa-linkedin" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialGithubUrl?has_content>
                <a href="${(site.socialGithubUrl)!}" target="_blank"
                   title="Follow me on Github"><i class="fab fa-github-alt" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialFBUrl?has_content>
                <a href="${(site.socialFBUrl)!}" target="_blank"
                   title="Follow me on Facebook"><i class="fab fa-facebook" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialTwitterUrl?has_content>
                <a href="${(site.socialTwitterUrl)!}" target="_blank"
                   title="Follow me on Twitter"><i class="fab fa-twitter" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialGooglePlusUrl?has_content>
                <a href="${(site.socialGooglePlusUrl)!}" target="_blank"
                   title="Follow me on Google Plus"><i class="fab fa-google-plus" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialWikiPageUrl?has_content>
                <a href="${(site.socialWikiPageUrl)!}" target="_blank"
                   title="Check out my tech notes"><i class="fab fa-wikipedia-w" style="font-size:24px"></i></a>
            </#if>

            <#if site.socialEmailMe?has_content>
                <a href="mailto:${(site.socialEmailMe)!}" rel="nofollow"
                   title="Email me!"><i class="far fa-envelope" style="font-size:24px"></i></a>
            </#if>

            <a href="/admin/dashboard" rel="nofollow"
               title="Admin Panel"><i class="far fa-user" style="font-size:24px"></i></a>


        </div>
    </div>

</header>
