package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.exceptions.CpfUniqueViolationException;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client saveClient(Client client) {
        try {
            return clientRepository.save(client);

        }catch (DataIntegrityViolationException exception) {
            throw new CpfUniqueViolationException(String.format("CPF: '%s' já cadastrado!", client.getCpf()));
        }
    }
}
