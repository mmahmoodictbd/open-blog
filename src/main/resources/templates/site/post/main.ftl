<!DOCTYPE html>
<html lang="en">

<head>
    <#include "../common/header.ftl">
    <title>Unload Brain</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="/webjars/prism/1.12.2/themes/prism.css" rel="stylesheet">
</head>

<body>

    <#include "../common/site-banner.ftl">

<div class="main-container container">

    <div class="row">

        <div class="col-md-9">

            <div class="mb-5">
                <h1>Bootstrap 4 navbar to the left edge?</h1>
                    <#include "meta.ftl">
            </div>

            <img src="https://placeimg.com/700/300/tech" class="img-fluid" alt="Responsive image">
            <br><br>
            <p class="text-justify">It is a long established fact that a reader will be distracted by the readable
                content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a
                more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it
                look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as
                their default model content, and a search for 'lorem ipsum' will uncover many web sites still in their
                infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose
                (injected humour and the like).
            </p>

            <p class="text-justify">Contrary to popular belief, Lorem Ipsum is not simply random content. It has roots
                in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock,
                a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words,
                consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical
                literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of
                "de Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book
                is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem
                Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32.</p>

            <p>
            <pre class="line-numbers">
                <code class="language-java">

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("/users.txt");
    byte[] contents = Files.readAllBytes(path);

    for (byte b : contents) {
      System.out.print((char) b);
    }

  }
}

                </code>
                </pre>
            </p>

            <p class="text-justify">The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those
                interested. Sections 1.10.32 and 1.10.33 from "de Finibus Bonorum et Malorum" by Cicero are also
                reproduced in their exact original form, accompanied by English versions from the 1914 translation by H.
                Rackham.</p>

        </div>


        <div class="col-md-3">

        </div>

    </div>
</div>

<br><br>

    <#include "../common/footer.ftl">
    <#include "../common/scripts.ftl">

</body>

</html>