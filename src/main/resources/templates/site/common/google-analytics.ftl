<#if site.googleAnalyticsAccountId?has_content>

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=${site.googleAnalyticsAccountId}"></script>
<script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }

    gtag('js', new Date());

    gtag('config', '${site.googleAnalyticsAccountId}');
</script>

</#if>