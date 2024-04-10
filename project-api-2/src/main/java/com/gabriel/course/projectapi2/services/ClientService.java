package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.exceptions.CpfUniqueViolationException;
import com.gabriel.course.projectapi2.exceptions.EntityNotFoundException;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("ID:'%s' não encontrado!", id)));
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
