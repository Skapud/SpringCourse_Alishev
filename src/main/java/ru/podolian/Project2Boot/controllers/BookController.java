package ru.podolian.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.podolian.Project2Boot.models.Book;
import ru.podolian.Project2Boot.models.Person;
import ru.podolian.Project2Boot.services.BookService;
import ru.podolian.Project2Boot.services.PeopleService;


import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) boolean sortByYear,
                        Model model) {

        if (page == null || booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByYear));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(id));

        Optional<Person> bookOwner = bookService.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int bookId, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(bookId, selectedPerson);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int bookId) {
        bookService.release(bookId);
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
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

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()){
            return "books/edit";
        }

        bookService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String newSearch(Model model) {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("request") String request, Model model) {
//        model.addAttribute("request", request);
        model.addAttribute("books", bookService.findByTitleStartingWithIgnoreCase(request));
        return "books/search";
    }
}
