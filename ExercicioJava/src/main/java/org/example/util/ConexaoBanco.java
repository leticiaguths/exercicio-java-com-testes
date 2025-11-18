package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBanco {

        private static final String URL = "jdbc:mysql://localhost:3308/mysql_testes_db?useSSL=false&serverTimezone=UTC";
        private static final String USER  = "root";
        private static final String SENHA = "testesPW";

        public static Connection conectar() throws Exception {
            return DriverManager.getConnection(URL, USER, SENHA);
        }
}