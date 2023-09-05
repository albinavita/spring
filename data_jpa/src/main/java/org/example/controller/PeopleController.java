package org.example.controller;

import jakarta.validation.Valid;
import org.example.models.Person;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    //внедряем объект PersonDAO в контроллер через конструктор
    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    //возвращает список из людей
    @GetMapping()
    public String index(Model model) {
       //Получим всех людей из DAO и передадим
        //на отображение в представление
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
      //Получим одного человека по id из DAO и
      //передадим на отображение в представление
        model.addAttribute("person", peopleService.findOne(id));
        return "people/show";
    }
    /**
     * возвращает HTML форму для создания нов чел
     */

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
       return "people/new";
    }

    /**
     * Принимает на вход POST запрос
     * Будет брать данные с этого POST запроса
     * Добавляет нового чел в БД с помощью DAO
     */
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
       peopleService.save(person);
        //redirect: - это механизм перехода (на другую страницу) на далее указанную страницу
        return "redirect:/people";
    }

    /**
     * страница для редактирования человека
     */
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
        public String update(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String gelete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
