package com.example.SpringTest3.dao;

import com.example.SpringTest3.model.Hamburger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HamburgerDao {

    int insertHamburger(UUID id, Hamburger hamburger);

    default int insertHamburger(Hamburger hamburger){
        UUID id = UUID.randomUUID();
        return insertHamburger(id,hamburger);
    }

    int updateHamburguerById(UUID id, Hamburger hamburger);

    int deleteHamburgerById(UUID id);

    List<Hamburger> selectAllHamburgers();

    Optional<Hamburger> selectHamburgerById(UUID id);

}
