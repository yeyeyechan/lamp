package com.springboot.lamp.dto;

public class MemberDto {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    private String email;
    private String organization;

    @Override
    public String toString(){
        return "memberdto  " + name + " " +email + " " + organization;
    }
}
