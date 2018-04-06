<!-- Posts -->
<#list posts as post>

    <div class="raw post">

        <a href="/post?id=1" class="post-image"">
        <img class="img-fluid" src="https://placeimg.com/700/300/tech" alt="Card image cap">
        </a>

        <h2 class="post-title"><a href="/post?id=1">One day in the forest</a></h2>

        <p class="post-text text-justify">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Reiciendis aliquid
            atque, nulla? Quos cum ex quis soluta, a laboriosam. Dicta expedita corporis animi vero voluptate
            voluptatibus possimus, veniam magni quis!
            <a href="/post?id=1" class="">&nbsp;Read More &rarr;</a>
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