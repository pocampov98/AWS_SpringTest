package com.example.SpringTest3.api;

import com.example.SpringTest3.model.Hamburger;
import com.example.SpringTest3.service.HamburgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/hamburger")
@RestController
public class HamburgerController {

    private final HamburgerService hamburgerService;

    @Autowired
    public HamburgerController(HamburgerService hamburgerService) {
        this.hamburgerService = hamburgerService;
    }

    @PostMapping
    public void insertHamburger(@RequestBody Hamburger hamburger){
        hamburgerService.insertHamburger(hamburger);
    }

    @PutMapping(path ="{id}")
    public int updateHamburguerById(@PathVariable("id") UUID id, @RequestBody Hamburger hamburger) {
        return hamburgerService.updateHamburguerById(id,hamburger);
    }

    @DeleteMapping(path ="{id}")
    public int deleteHamburgerById(@PathVariable("id") UUID id) {
        return hamburgerService.deleteHamburgerById(id);
    }

    @GetMapping
    public List<Hamburger> selectAllHamburgers() {
        return hamburgerService.selectAllHamburgers();
    }

    @GetMapping(path ="{id}")
    public Optional<Hamburger> selectHamburgerById(@PathVariable("id") UUID id) {
        return selectHamburgerById(id);
    }
}
