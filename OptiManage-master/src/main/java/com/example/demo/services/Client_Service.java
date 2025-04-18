package com.example.demo.services;

import com.example.demo.dao.ClientDAO;
import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Client_Service {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private PasswordEncoder passwordEncoder; // Removed initialization here

    public Client_Service(ClientDAO clientDAO, PasswordEncoder passwordEncoder) {
        this.clientDAO = clientDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Client registerClient(ClientDTO clientDTO) { // Removed static modifier
        try {
            // Hash password
            String hashedPassword = passwordEncoder.encode(clientDTO.getPassword());
            clientDTO.setPassword(hashedPassword);

            // Convert ClientDTO to Client entity and save
            Client client = new Client();
            client.setFirstName(clientDTO.getFirstName());
            client.setMiddleName(clientDTO.getMiddleName());
            client.setLastName(clientDTO.getLastName());
            client.setContactNumber(clientDTO.getContactNumber());
            client.setEmailAddress(clientDTO.getEmailAddress());
            client.setClientType(clientDTO.getClientType());
            client.setPassword(hashedPassword);
            client.setUsername(clientDTO.getUsername());

            // Save the client entity to the database
            clientDAO.save(client);

            return client;
        } catch (Exception e) {
            // Log the error and handle appropriately
            System.err.println("Error during client registration: " + e.getMessage());
            throw e; // Or handle appropriately
        }
    }

    public Client getClient(String username) {
        Client client = clientDAO.findByUsername(username); // Assuming findByUsername is implemented in ClientDAO

        if (client == null) {
            // If the client is not found, throw an exception
            System.out.println("##client is null " );
            throw new UsernameNotFoundException("Client not found with username: " + username);
        }

        // Return the found client
        return client;
    }
}
