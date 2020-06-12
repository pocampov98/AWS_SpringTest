package com.example.SpringTest3.service;

import com.example.SpringTest3.dao.HamburgerDao;
import com.example.SpringTest3.model.Hamburger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HamburgerService {

    private final HamburgerDao hamburgerDao;

    @Autowired
    public HamburgerService(@Qualifier("dynamodbDao") HamburgerDao hamburgerDao) {
        this.hamburgerDao = hamburgerDao;
    }

    public int insertHamburger(Hamburger hamburger){
        return hamburgerDao.insertHamburger(hamburger);
    }

    public int updateHamburguerById(UUID id, Hamburger hamburger) {
        return hamburgerDao.updateHamburguerById(id,hamburger);
    }

    public int deleteHamburgerById(UUID id) {
        return hamburgerDao.deleteHamburgerById(id);
    }

    public List<Hamburger> selectAllHamburgers() {
        return hamburgerDao.selectAllHamburgers();
    }

    public Optional<Hamburger> selectHamburgerById(UUID id) {
        return selectHamburgerById(id);
    }
}
