package com.dmedinao.codigoton.models.mapper;

import com.dmedinao.codigoton.models.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
