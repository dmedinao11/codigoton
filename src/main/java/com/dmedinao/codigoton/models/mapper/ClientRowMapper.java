package com.dmedinao.codigoton.models.mapper;

import com.dmedinao.codigoton.models.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper usado al momento de resolver el result set proveniente de
 * la consulta
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

public class ClientRowMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Client
                .builder()
                .id(rs.getInt("id"))
                .code(rs.getString("code"))
                .male(rs.getBoolean("male"))
                .type(rs.getInt("type"))
                .location(rs.getString("location"))
                .company(rs.getString("company"))
                .encrypt(rs.getBoolean("encrypt"))
                .balance(rs.getDouble("balance"))
                .build();
    }
}
