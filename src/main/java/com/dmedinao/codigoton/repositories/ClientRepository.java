package com.dmedinao.codigoton.repositories;

import com.dmedinao.codigoton.models.Client;
import com.dmedinao.codigoton.models.mapper.ClientRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Client> getByFilters(String filters) throws DataAccessException {
        String query = String.format(queryTemplate, filters);
        return jdbcTemplate.query(query, new ClientRowMapper());
    }
}
