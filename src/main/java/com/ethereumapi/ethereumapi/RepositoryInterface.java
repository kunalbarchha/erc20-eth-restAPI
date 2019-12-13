package com.ethereumapi.ethereumapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface RepositoryInterface extends CrudRepository<RepositoryService, String>{

    WalletService walletService=new WalletService();

    List<RepositoryService> findbyfromAddress();

}
