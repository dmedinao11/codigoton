package com.dmedinao.codigoton.repositories;

import com.dmedinao.codigoton.models.Client;
import com.dmedinao.codigoton.models.mapper.ClientRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JDBC para la tabla cliente
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/
@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String queryTemplate =
            "SELECT " +
                    "c.*, SUM(a.balance) AS balance " +
                    "FROM " +
                    "account a " +
                    "JOIN " +
                    "client c ON c.id = a.client_id " +
                    "WHERE " +
                    "%s " +
                    "GROUP BY c.id " +
                    "ORDER BY balance DESC , c.code ASC;";


    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Método del repositorio que obtiene los datos de los clientes
     * y su balance, de acuerdo a los filtros correctamente escritos en SQL
     * del parámetro
     *
     * @param filters Cadena de filtros escritos en sql. Ejemplo 'c.type = %s'
     * @return la lista de clientes aceptados por los filtros provenientes de la base de datos
     **/
    public List<Client> getByFilters(String filters) throws DataAccessException {
        String query = String.format(queryTemplate, filters);
        return jdbcTemplate.query(query, new ClientRowMapper());
    }
}
