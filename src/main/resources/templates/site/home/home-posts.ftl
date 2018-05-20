<!-- Posts -->
<#list postsPage.content as post>

    <div class="raw post">

        <a href="/posts/${(post.id)!}/${(post.permalink)!}" class="post-image"">
        <img class="img-fluid" src="${(post.featureImageLink)!}" alt="Card image cap">
        </a>

        <h2 class="post-title"><a href="/posts/${(post.id)!}/${(post.permalink)!}">${(post.title)!}</a></h2>

        <p class="post-text text-justify">${(post.summary)!}
            <a href="/posts/${(post.id)!}/${(post.permalink)!}" class="">&nbsp;Read More &rarr;</a>
        </p>

    </div>

</#list>

<!-- Pagination -->
<ul class="pagination justify-content-center mb-4">
    <li class="page-item">
        <a class="page-link" href="#">&larr; Older</a>
    </li>
    <li class="page-item disabled">
        <a class="page-link" href="#">Newer &rarr;</a>
    </li>
</ul>