package com.fish.drawme.server.startup;

import com.fish.drawme.server.controller.Controller;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static com.fish.drawme.common.Constants.SERVER_NAME_IN_REGISTRY;

public class Main {

    public static void main(String[] args) {
        try{
            new Main().startRegistry();
            Naming.rebind(SERVER_NAME_IN_REGISTRY, new Controller());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryIsRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
