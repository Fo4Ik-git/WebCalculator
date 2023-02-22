package com.fo4ik.momcalculator.models;

import jakarta.persistence.*;


@Entity
public class Elements {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Float value, cost, variable;
    public Elements(String name, Float value, Float cost) {
        this.name = name;
        this.value = value;
        this.cost = cost;
        this.variable = 0f;
    }

    public Elements() {
    }

    public Float getVariable() {
        return variable;
    }

    public void setVariable(Float variable) {
        this.variable = variable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "name='" + name + '\n' +
                ", value=" + value + "\n" +
                ", cost=" + cost + "\n";
    }
}
