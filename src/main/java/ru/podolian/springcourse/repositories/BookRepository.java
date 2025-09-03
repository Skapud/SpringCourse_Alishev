package ru.podolian.springcourse.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podolian.springcourse.models.Book;
import ru.podolian.springcourse.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);

    List<Book> findByOwner(Person owner);

    List<Book> findByTitleStartingWith(String prefix);

    List<Book> findByOwnerId(int id);
}