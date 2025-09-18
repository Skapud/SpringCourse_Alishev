package ru.podolian.Project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podolian.Project2Boot.models.Book;
import ru.podolian.Project2Boot.models.Person;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);

    List<Book> findByOwner(Person owner);

    List<Book> findByTitleStartingWithIgnoreCase(String prefix);

    List<Book> findByOwnerId(int id);
}