package org.aquare.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class JMPage {

    public static void main(String[] args) throws IOException {

        final Path inputDir = Paths.get("C:\\Users\\averri\\Desktop\\Fotos Java Magazine");

        //String name = "JM-011.jpg";

        Function<String, String> fileNameToId = name -> name.substring(3, 6);

        Function<String, String> template = name ->
                "<td><img src=\"" + name + "\"/><br/>Edição "
                        + fileNameToId.apply(name) + "</td>";

        final AtomicInteger n = new AtomicInteger(0);

        final StringBuilder sb = new StringBuilder();

        final int qtyPerRow = 6;

        Files.list(inputDir)
                .filter(f -> f.getFileName().toString().endsWith(".jpg"))
                .forEach(f -> {
                    if (n.get() % qtyPerRow == 0) sb.append("<tr>").append("\n");
                    sb.append(template.apply(f.getFileName().toString()));
                    sb.append("\n");
                    if (n.get() % qtyPerRow == qtyPerRow - 1) {
                        sb.append("</tr>");
                        sb.append("\n");
                    }
                    n.incrementAndGet();
                });

        sb.append("</tr>");
        sb.append("\n");

        System.out.println(sb.toString());
        System.out.println("Quantity: " + n);

    }

}
