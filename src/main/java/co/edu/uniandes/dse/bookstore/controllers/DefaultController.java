package co.edu.uniandes.dse.bookstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("*")
public class DefaultController {
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public String index() {
        return "REST API for BookStore is deployed successfully";
    }
}
