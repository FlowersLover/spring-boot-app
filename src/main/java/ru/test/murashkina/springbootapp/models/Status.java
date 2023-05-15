package ru.test.murashkina.springbootapp.models;

public enum Status {
    Active,
    Blocked;

    public Boolean isBlocked() {
        if (this.name() == "ACTIVE") {
            return false;
        }
        return true;
    }
}
