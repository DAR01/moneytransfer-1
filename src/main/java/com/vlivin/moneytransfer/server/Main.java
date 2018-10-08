package com.vlivin.moneytransfer.server;

import com.vlivin.moneytransfer.persistence.PersistenceUnit;
import com.vlivin.moneytransfer.resources.AccountsResource;
import com.vlivin.moneytransfer.services.AccountService;
import com.vlivin.moneytransfer.services.TransferService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    private static final String APP_NAME = "moneytransfer";
    public static final String BASE_URI = "http://localhost:8080/" + APP_NAME + "/";

    private static final String BASE_PACKAGE = "com.vlivin.moneytransfer";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.vlivin package
        final ResourceConfig rc = new ResourceConfig()
                .packages(BASE_PACKAGE)
                .registerClasses(AccountsResource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bindAsContract(TransferService.class);
                        bindAsContract(PersistenceUnit.class).in(Singleton.class);
                        bindAsContract(AccountService.class).in(Singleton.class);
                    }
                })
                .register(JacksonFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
        PersistenceUnit.getInstance().closeEntityManagerFactory();
    }
}

