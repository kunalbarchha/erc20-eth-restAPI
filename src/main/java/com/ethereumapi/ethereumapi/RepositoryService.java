package com.ethereumapi.ethereumapi;

import com.ethereumapi.ethereumapi.Entity.Entity;
import lombok.extern.slf4j.Slf4j;


import java.math.BigInteger;
import java.sql.*;

@Slf4j
public class RepositoryService{

        Connection connection;
        public void Connection() throws Exception{

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ethereum", "root", "Kunal@9120");

        }

        public void insert(String walletname, String walletAddress, String privateKey, String publicKey) throws Exception {

            Connection();
            Statement statement=connection.createStatement();
            String sqlInsert="INSERT INTO walletdetails (id, walletname, walletaddress, privatekey, publickey) VALUES ( null ,'"+walletname+"','"+walletAddress+"','"+privateKey+"','"+publicKey+"')";
            statement.executeUpdate(sqlInsert);
            connection.close();

        }
        public String walletDetails(String walletname) throws Exception {

            String walletAddress=null;
            Entity entity=new Entity();
            Connection();
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT walletaddress FROM walletdetails WHERE walletname='"+walletname+"'");

            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                walletAddress=resultSet.getString("walletaddress");
                log.info("Wallet Address : " +walletAddress);
            }
            return walletAddress;
        }
        public void updateRecord(String fromWallet, String toWallet, BigInteger amount, String txHash){


        }
}

