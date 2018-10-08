package com.vlivin.moneytransfer.resources;

import com.vlivin.moneytransfer.model.Transfer;
import com.vlivin.moneytransfer.persistence.PersistenceUnit;
import com.vlivin.moneytransfer.services.TransferService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Vladimir Livin
 */
@Path("transfer")
@Singleton
public class TransferResource {
    @Inject
    TransferService transferService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Transfer transfer(Transfer transfer) {
        return transferService.transfer(transfer);
    }
}
