package ru.podolian.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import ru.podolian.springcourse.models.Book;
import ru.podolian.springcourse.models.Person;

import java.util.List;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SpringResourceTemplateResolver springResourceTemplateResolver;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate, SpringResourceTemplateResolver springResourceTemplateResolver) {
        this.jdbcTemplate = jdbcTemplate;
        this.springResourceTemplateResolver = springResourceTemplateResolver;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> showPersonBooks(int personId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE Book.person_id = ?", new Object[]{personId}, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int bookId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?", new Object[]{bookId}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book (name, author, year) VALUES(?,?,?)", book.getName(),
                book.getAuthor(), book.getYear());
    }

    public void update(int bookId, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE book_id=?", updatedBook.getName(),
                updatedBook.getAuthor(), updatedBook.getYear(), bookId);
    }

    public void giveBook(int bookId, Person bookOwner) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE book_id=?", bookOwner.getPersonId(), bookId);
    }

    public void releaseBook(int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id=null WHERE book_id=?", bookId);
    }

    public void delete(int bookId) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?", bookId);
    }

}
