package es.spring.batch.example;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class BookItemProcessor implements ItemProcessor<Book, String> {

    private static final Logger log = LoggerFactory.getLogger(BookItemProcessor.class);

    @Override
    public String process(final Book book) throws Exception {
        final JSONObject transformedBook = new JSONObject() ;
        transformedBook.put("author", book.getAuthor());
        transformedBook.put("title", book.getTitle());

        log.info("Converting (" + book + ") into (" + transformedBook + ")");

        return transformedBook.toString();
    }

}
