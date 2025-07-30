package ru.podolian.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.podolian.springcourse.dao.BookDAO;
import ru.podolian.springcourse.dao.PersonDAO;
import ru.podolian.springcourse.models.Book;
import ru.podolian.springcourse.models.Person;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.show(id);
        model.addAttribute("book", book);
        
        if(book.getPersonId() != null) {
            Person owner = personDAO.show(book.getPersonId());
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", personDAO.index());
        }
        return "books/show";
    }

    @PostMapping("/{bookId}/give")
    public String give(@PathVariable("bookId") int bookId,
                       @RequestParam("personId") int personId) {
        Person person = personDAO.show(personId);
        bookDAO.giveBook(bookId, person);
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/release")
    public String release(@PathVariable("bookId") int bookId) {
        bookDAO.releaseBook(bookId);
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int bookId) {
        bookDAO.delete(bookId);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute Book book) {
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int bookId) {
        model.addAttribute("book", bookDAO.show(bookId));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int bookId) {
        if (bindingResult.hasErrors()){
            return "books/edit";
        }

        bookDAO.update(bookId, book);
        return "redirect:/books";
    }
}
