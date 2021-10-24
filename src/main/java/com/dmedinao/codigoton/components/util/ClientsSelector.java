package com.dmedinao.codigoton.components.util;

import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ClientsSelector {
    private final int MAX_CLIENTS = 8;
    private final int MIN_CLIENTS = 4;
    private PriorityQueue<ClientDto> availableClients = new PriorityQueue<>(new ClientsComparator());
    private final Map<String, Queue<ClientDto>> notSelectedClients = new HashMap<>();
    private final Map<String, Boolean> selectedCompanies = new HashMap<>();
    private PriorityQueue<ClientDto> selectedClients = new PriorityQueue<>(MAX_CLIENTS, new ClientsComparator());
    private final String male = "M";
    private final String female = "F";
    private int genderDifference = 0;
    private boolean mostAreMale = false;
    private boolean selectedClientsAreValid = true;


    public List<String> selectClients(List<ClientDto> clientsList) throws InsufficientClientsException {
        availableClients.addAll(clientsList);
        populateSelectedClientsQueue();
        updateValidity();

        if (selectedClientsAreValid) {
            return generateOutput();
        }

        LinkedList<String> companiesToRestore = removeGenderDifference(false);
        restoreAvailableCompanies(companiesToRestore);
        filterAvailableClientsByGender();
        populateSelectedClientsQueue();
        updateValidity();
        removeGenderDifference(true);
        updateValidity();

        if (selectedClientsAreValid) {
            return generateOutput();
        }

        throw new InsufficientClientsException("Insufficient clients to select");
    }


    private void populateSelectedClientsQueue() {
        while (selectedClients.size() < MAX_CLIENTS) {
            if (availableClients.isEmpty()) {
                break;
            }
            ClientDto currentClient = availableClients.peek();
            String currentClientCompanyAndGender = getCompanyAndGender(currentClient);
            boolean companyAlreadyHasClient = selectedCompanies.containsKey(currentClient.getCompany());

            if (companyAlreadyHasClient) {
                notSelectedClients.computeIfAbsent(currentClientCompanyAndGender, k -> new LinkedList<>());
                notSelectedClients.get(currentClientCompanyAndGender).add(availableClients.remove());
            } else {
                selectedCompanies.put(currentClient.getCompany(), true);
                selectedClients.add(availableClients.remove());
            }
        }
    }

    private void updateValidity() {
        // updateGenderCounts();
        int selectedMaleCount = (int) selectedClients.stream().filter(ClientDto::getMale).count();
        int selectedFemaleCount = (int) selectedClients.stream().filter(clientDto -> !clientDto.getMale()).count();
        // updateGenderDifference();
        genderDifference = Math.abs(selectedFemaleCount - selectedMaleCount);
        mostAreMale = selectedMaleCount > selectedFemaleCount;
        // updateValidity();
        boolean gendersAreEqual = selectedMaleCount == selectedFemaleCount;
        boolean hasSufficientClients = selectedClients.size() > MIN_CLIENTS;
        selectedClientsAreValid = gendersAreEqual && hasSufficientClients;
    }

    private String getCompanyAndGender(ClientDto client) {
        String gender = client.getMale() ? male : female;
        return client.getCompany().concat(gender);
    }

    private LinkedList<String> removeGenderDifference(boolean fully) {
        if (genderDifference == 0) return null;

        int numOfClientsToRemove = fully ? genderDifference : genderDifference / 2;

        List<ClientDto> selectedClientsList = new ArrayList<>(selectedClients);
        LinkedList<String> companiesToRestore = new LinkedList<>();

        for (int i = selectedClientsList.size() - 1; numOfClientsToRemove > 0; i--) {
            ClientDto currentClient = selectedClientsList.get(i);
            boolean hasToDeleteItem = currentClient.getMale() == mostAreMale;

            if (hasToDeleteItem) {
                selectedClientsList.remove(currentClient);
                companiesToRestore.add(currentClient.getCompany());
                numOfClientsToRemove--;
            }
        }

        this.selectedClients = new PriorityQueue<>(MAX_CLIENTS, new ClientsComparator());
        this.selectedClients.addAll(selectedClientsList);
        return companiesToRestore;

    }

    private void filterAvailableClientsByGender() {
        List<ClientDto> filteredClientsList = availableClients
                .stream()
                .filter(clientDto -> clientDto.getMale() != mostAreMale)
                .collect(Collectors.toList());

        availableClients = new PriorityQueue<>(new ClientsComparator());
        availableClients.addAll(filteredClientsList);
    }

    private void restoreAvailableCompanies(LinkedList<String> toRestore) {

        if(toRestore == null) return;

        String genderToRestore = mostAreMale ? female : male;

        toRestore.forEach(company -> {
            String companyKey = company.concat(genderToRestore);

            if(notSelectedClients.containsKey(companyKey)){
                this.availableClients.addAll(notSelectedClients.get(companyKey));
            }

            selectedCompanies.put(company, false);
        });
    }
    
    private List<String> generateOutput(){
        return selectedClients.stream().map(ClientDto::getCode).collect(Collectors.toList());
    }
}
