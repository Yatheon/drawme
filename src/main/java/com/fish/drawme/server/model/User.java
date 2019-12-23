package com.fish.drawme.server.model;

import com.fish.drawme.common.Client;

public class User {

    private Client client;

    public User(Client client){
        this.client = client;
    }
    public Client getClient() {
        return client;
    }
}
