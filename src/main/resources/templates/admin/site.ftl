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
                </ul>

                <!-- Tab panes -->
                <div class="tab-content">

                    <div role="tabpanel" class="tab-pane fade active show" id="basicTab">

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

                        <div class="form-group">
                            <label for="additionalProperties['keywords']">Keywords</label>
                            <input name="additionalProperties['keywords']" type="text" class="form-control"
                                   id="keywords"
                                   value="${(site.additionalProperties['keywords'])!}">
                            <small id="additionalPropertiesKeywordsHelp" class="form-text text-muted">Used in meta
                                tags.
                            </small>
                        </div>

                    </div>

                    <div role="tabpanel" class="tab-pane fade" id="socialTab">

                        <div class="form-group">
                            <label for="additionalProperties['socialLinkedIn']">LinkedIn URL</label>
                            <input name="additionalProperties['socialLinkedIn']" type="text" class="form-control"
                                   id="socialLinkedIn" value="${(site.additionalProperties['socialLinkedIn'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialGithub']">Github URL</label>
                            <input name="additionalProperties['socialGithub']" type="text" class="form-control"
                                   id="socialGithub" value="${(site.additionalProperties['socialGithub'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialFB']">FB URL</label>
                            <input name="additionalProperties['socialFB']" type="text" class="form-control"
                                   id="socialFB" value="${(site.additionalProperties['socialFB'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialTwitter']">Twitter URL</label>
                            <input name="additionalProperties['socialTwitter']" type="text" class="form-control"
                                   id="socialTwitter" value="${(site.additionalProperties['socialTwitter'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialGooglePlus']">Google Plus URL</label>
                            <input name="additionalProperties['socialGooglePlus']" type="text" class="form-control"
                                   id="socialGooglePlus" value="${(site.additionalProperties['socialGooglePlus'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialWikiPage']">Wiki URL</label>
                            <input name="additionalProperties['socialWikiPage']" type="text" class="form-control"
                                   id="socialWikiPage" value="${(site.additionalProperties['socialWikiPage'])!}">
                        </div>

                        <div class="form-group">
                            <label for="additionalProperties['socialEmailMe']">Email Me</label>
                            <input name="additionalProperties['socialEmailMe']" type="text" class="form-control"
                                   id="socialEmailMe" value="${(site.additionalProperties['socialEmailMe'])!}">
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