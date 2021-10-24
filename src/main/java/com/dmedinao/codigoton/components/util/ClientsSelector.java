package com.dmedinao.codigoton.components.util;

import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que principal que se encarga de seleccionar los clientes para una mesa
 * correspondiente
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

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


    /**
     * Método que recibe el dto. Con la información de los clientes
     * y retorna una lista ordenada con los códigos de los usuarios seleccionados
     * <p>
     * En caso de seleccionar usuarios insuficientes lanza una excepción
     *
     * @param clientsList lista de clientes a evaluar
     * @return lista ordenada con los códigos de usuario seleccionados
     * @throws InsufficientClientsException de no seleccionar suficientes clientes
     **/
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

    /**
     * Método toma los clientes disponibles en la cola availableClients
     * y los encola en la lista de selectedClients
     * <p>
     * También registra las compañías seleccionadas y los clientes que no se seleccionaron
     * debido a que ya había un cliente de su empresa
     **/
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

    /**
     * Método que válida que los clientes seleccionados sean válidos en cuanto
     * a la cantidad de personas de cada género sea equivalente
     * <p>
     * También revisa que la cola de clientes seleccionados tenga el tamaño mínimo
     * requerido
     * <p>
     * Y por último determina si la mayoría de personas pertenecen a un género o a otro
     **/
    private void updateValidity() {
        int selectedMaleCount = (int) selectedClients.stream().filter(ClientDto::getMale).count();
        int selectedFemaleCount = (int) selectedClients.stream().filter(clientDto -> !clientDto.getMale()).count();

        genderDifference = Math.abs(selectedFemaleCount - selectedMaleCount);
        mostAreMale = selectedMaleCount > selectedFemaleCount;

        boolean gendersAreEqual = selectedMaleCount == selectedFemaleCount;
        boolean hasSufficientClients = selectedClients.size() >= MIN_CLIENTS;
        selectedClientsAreValid = gendersAreEqual && hasSufficientClients;
    }

    /**
     * Método que genera la llave para el mapa que registra una cola de clientes
     * que pertenecen a una compañía y un género y no fueron inicialmente seleccionados
     * de acuerdo a cada cliente
     *
     * @param client cliente para calcular su llave
     * @return la llave de usuario así companyG donde company es el identificador y G el género por ejemplo: 10M
     **/
    private String getCompanyAndGender(ClientDto client) {
        String gender = client.getMale() ? male : female;
        return client.getCompany().concat(gender);
    }

    /**
     * Método que elimina de la cola de clientes seleccionados por el método populateSelectedClientsQueue
     * aquellos que hagan mayoría en el género que tenga mayoría en la cola
     *
     * @param fully indica con true si se eliminan todos los clientes del género que sobran o solo la mitad de ellos para volver a encolar clientes
     * @return una lista de llaves companyG de los usuarios que fueron extraídos y su compañía ya no tiene clientes
     **/
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

    /**
     * Método que filtra en la cola de clientes disponibles a aquellos clientes que pertenezcan al género mayoritario
     * en la lista de seleccionados
     **/
    private void filterAvailableClientsByGender() {
        List<ClientDto> filteredClientsList = availableClients
                .stream()
                .filter(clientDto -> clientDto.getMale() != mostAreMale)
                .collect(Collectors.toList());

        availableClients = new PriorityQueue<>(new ClientsComparator());
        availableClients.addAll(filteredClientsList);
    }

    /**
     * Método que toma una lista con los códigos de las compañías que quedaron sin clientes en la mesa
     * y los regresa a la lista de clientes disponibles si estos pertenecen al género no mayoritario
     *
     * @param toRestore lista de códigos de las compañías a devolver
     **/
    private void restoreAvailableCompanies(LinkedList<String> toRestore) {

        if (toRestore == null) return;

        String genderToRestore = mostAreMale ? female : male;

        toRestore.forEach(company -> {
            String companyKey = company.concat(genderToRestore);

            if (notSelectedClients.containsKey(companyKey)) {
                this.availableClients.addAll(notSelectedClients.get(companyKey));
            }

            selectedCompanies.put(company, false);
        });
    }

    /**
     * Método que genera la lista de códigos de clientes seleccionados
     *
     * @return lista de códigos de los clientes que fueron seleccionados para llenar la mesa
     **/
    private List<String> generateOutput() {
        return selectedClients.stream().map(ClientDto::getCode).collect(Collectors.toList());
    }
}
