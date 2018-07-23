<br>
<div id="disqus_thread"></div>
<script>

    var disqus_config = function () {
    this.page.url = "${site.siteUrl}" + "/" + "${post.permalink}";
    this.page.identifier = ${post.id};
    };

    (function() { // DON'T EDIT BELOW THIS LINE
        var d = document, s = d.createElement('script');
        s.src = 'https://${site.disqusUniqueUrl}/embed.js';
        s.setAttribute('data-timestamp', +new Date());
        (d.head || d.body).appendChild(s);
    })();
</script>
<noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
