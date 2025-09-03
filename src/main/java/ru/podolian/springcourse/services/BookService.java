package ru.podolian.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.podolian.springcourse.models.Book;
import ru.podolian.springcourse.models.Person;
import ru.podolian.springcourse.repositories.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> findByTitleStartingWith(String prefix) {
        return bookRepository.findByTitleStartingWith(prefix);
    }

//    public List<Book> getBooksByPersonId(int id) {
//        List<Book> books = bookRepository.findByOwnerId(id);
//
//        for (Book book : books) {
//            book.setExpired(expiredCheck(book.getPicked()));
//        }
//
//        return books;
//    }

    public Optional<Person> getBookOwner(int id) {
        return bookRepository.findById(id).map(Book::getOwner);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        bookRepository.save(updatedBook);
    }

    @Transactional
    public void assign(int bookId, Person selectedPerson) {
        bookRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setPicked(LocalDateTime.now());
                }
        );
//        Book book = findOne(bookId);
//        Person person = peopleRepository.findById(personId)
//                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
//        book.setOwner(person);
//        book.setPicked(LocalDateTime.now());

//        bookRepository.save(book); не нужен, т.к. мы загрузили его через findOne()
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setPicked(null);
                });
//        bookRepository.save(book); не нужен, т.к. мы загрузили его через findOne()
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

//    public boolean expiredCheck(LocalDateTime pickUpDate) {
//        return pickUpDate.plusDays(10).isBefore(LocalDateTime.now());
//    }
}
