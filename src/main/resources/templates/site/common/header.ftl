<title>${site.name}</title>

<meta charset="utf-8">
<meta name="description" content="${site.description}">
<meta name="keywords" content="${(site.metaKeywords)!}">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="robots" content="index, follow"/>

<meta name="google-site-verification" content="${(site.googleSiteVerificationId)!}"/>
<meta name="msvalidate.01" content="${(site.bingSiteVerificationId)!}"/>

<link rel="canonical" href="${site.siteUrl}"/>
<link rel="shortcut icon" href="/static/favicon.png">

<#if post?has_content>
    <meta property=og:type content=article />
    <meta property=og:title content="${post.title}"/>
    <meta property=og:site_name content="${site.name}"/>
    <meta property=og:description content="${post.summary}"/>
    <meta property=og:url content="${site.siteUrl}/posts/${(post.id)!}/${(post.permalink)!}"/>
    <meta name=twitter:title content="${post.title}"/>
    <meta name=twitter:description content="${post.summary}"/>
    <meta name=twitter:url content="${site.siteUrl}/posts/${(post.id)!}/${(post.permalink)!}"/>
    <#if post.featureImageLink?has_content>
        <meta property=og:image content="${site.siteUrl}${(post.featureImageLink)!}"/>
        <meta name=twitter:image content="${site.siteUrl}${(post.featureImageLink)!}"/>
    </#if>
</#if>

<link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="/webjars/font-awesome/on-server/css/fontawesome-all.min.css" rel="stylesheet">

<link href="/static/css/base.css" rel="stylesheet">
<link href="/static/css/site.css" rel="stylesheet">