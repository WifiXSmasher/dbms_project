package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dao.ClientDAO;  
import com.example.demo.entity.Client;  

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientDAO clientDAO;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllClients(Model model) {
        List<Client> clients = clientDAO.getAllClients();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "addclient";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addClient(@ModelAttribute Client client) {
        clientDAO.addClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditClientForm(@PathVariable int id, Model model) {
        Client client = clientDAO.getClientById(id);
        model.addAttribute("client", client);
        return "editclient";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editClient(@ModelAttribute Client client) {
        clientDAO.updateClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteClient(@PathVariable int id) {
        clientDAO.deleteClient(id);
        return "redirect:/clients";
    }

    
}
