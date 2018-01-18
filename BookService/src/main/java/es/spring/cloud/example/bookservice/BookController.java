package es.spring.cloud.example.bookservice;

import java.util.List;

import javax.ws.rs.FormParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	BookJpaRepository jpaRepository;
	
	
    @GetMapping("")
    public List<Book> findAllBooks() {
        return Lists.newArrayList(jpaRepository.findAll());
    }

    @GetMapping("/{bookId}")
    public Book findBook(@PathVariable Long bookId) {
        return jpaRepository.findOne(bookId);
    }
    
    @PostMapping("/add")
    public Book addBook(@FormParam("title") String title, @FormParam("author") String author) {
        return jpaRepository.save(new Book(author, title));
    }
    
    @PostMapping("/save")
    public Book saveBook(@RequestBody Book book) {
        return jpaRepository.save(book);
    }
    
}