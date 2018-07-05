<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>Update site</title>
    <#include "header.ftl">
    <link href="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
</head>

<body>

    <#include "top-nav.ftl">

<div class="container">

    <form action="/admin/site" method="post">

        <div class="row mt-5 pt-5">

            <div class="col-md-9">

                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" href="#basicTab" role="tab" data-toggle="tab">Basic</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#metaTab" role="tab" data-toggle="tab">Meta</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#socialTab" role="tab" data-toggle="tab">Social</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#analyticsTab" role="tab" data-toggle="tab">Analytics</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#siteVerificationTab" role="tab" data-toggle="tab">
                            Site Verification
                        </a>
                    </li>
                </ul>

                <!-- Tab panes -->
                <div class="tab-content">

                    <div role="tabpanel" class="tab-pane fade active show" id="basicTab">

                        <div class="row">&nbsp;</div>

                        <div class="form-group">
                            <label for="name">Site Name</label>
                            <input name="name" type="text" class="form-control" id="name" value="${(site.name)!}">
                        </div>

                        <div class="form-group">
                            <label for="description">Site Description</label>
                            <input name="description" class="form-control" id="description"
                                   value="${(site.description)!}">
                        </div>

                        <div class="form-group">
                            <label for="siteUrl">Site URL</label>
                            <input name="siteUrl" type="text" class="form-control" id="siteUrl"
                                   value="${(site.siteUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="homeUrl">Home URL</label>
                            <input name="homeUrl" type="text" class="form-control" id="homeUrl"
                                   value="${(site.homeUrl)!}">
                        </div>

                    </div>

                    <div role="tabpanel" class="tab-pane fade" id="metaTab">

                        <div class="row">&nbsp;</div>

                        <div class="form-group">
                            <label for="metaKeywords">Keywords</label>
                            <input name="metaKeywords" type="text" class="form-control" id="keywords"
                                   value="${(site.metaKeywords)!}">
                            <small id="metaKeywordsHelp" class="form-text text-muted">Used in meta
                                tags.
                            </small>
                        </div>

                    </div>

                    <div role="tabpanel" class="tab-pane fade" id="socialTab">

                        <div class="row">&nbsp;</div>

                        <div class="form-group">
                            <label for="socialLinkedInUrl">LinkedIn URL</label>
                            <input name="socialLinkedInUrl" type="text" class="form-control"
                                   id="socialLinkedIn" value="${(site.socialLinkedInUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialGithubUrl">Github URL</label>
                            <input name="socialGithubUrl" type="text" class="form-control"
                                   id="socialGithub" value="${(site.socialGithubUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialFBUrl">FB URL</label>
                            <input name="socialFBUrl" type="text" class="form-control"
                                   id="socialFB" value="${(site.socialFBUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialTwitterUrl">Twitter URL</label>
                            <input name="socialTwitterUrl" type="text" class="form-control"
                                   id="socialTwitter" value="${(site.socialTwitterUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialGooglePlusUrl">Google Plus URL</label>
                            <input name="socialGooglePlusUrl" type="text" class="form-control"
                                   id="socialGooglePlus" value="${(site.socialGooglePlusUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialWikiPageUrl">Wiki URL</label>
                            <input name="socialWikiPageUrl" type="text" class="form-control"
                                   id="socialWikiPage" value="${(site.socialWikiPageUrl)!}">
                        </div>

                        <div class="form-group">
                            <label for="socialEmailMe">Email Me</label>
                            <input name="socialEmailMe" type="text" class="form-control"
                                   id="socialEmailMe" value="${(site.socialEmailMe)!}">
                        </div>

                    </div>

                    <div role="tabpanel" class="tab-pane fade" id="analyticsTab">

                        <div class="row">&nbsp;</div>

                        <div class="form-group">
                            <label for="googleAnalyticsAccountId">
                                Google Analytics Account Id
                            </label>
                            <input name="googleAnalyticsAccountId" type="text" class="form-control"
                                   id="googleAnalyticsAccountId"
                                   value="${(site.googleAnalyticsAccountId)!}">
                        </div>

                    </div>

                    <div role="tabpanel" class="tab-pane fade" id="siteVerificationTab">

                        <div class="row">&nbsp;</div>

                        <div class="form-group">
                            <label for="googleSiteVerificationId">
                                Google Site Verification Id
                            </label>
                            <input name="googleSiteVerificationId" type="text"
                                   class="form-control" id="googleSiteVerificationId"
                                   value="${(site.googleSiteVerificationId)!}">
                        </div>

                        <div class="form-group">
                            <label for="bingSiteVerificationId">
                                Bing Site Verification Id
                            </label>
                            <input name="bingSiteVerificationId" type="text"
                                   class="form-control" id="bingSiteVerificationId"
                                   value="${(site.bingSiteVerificationId)!}">
                        </div>

                    </div>

                </div>

            </div>

            <div class="col-md-3">

                <div class="form-group">
                    <button type="submit" class="btn btn-secondary btn-sm">Save</button>
                </div>

            </div>

        </div>

    </form>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>

</body>

</html>