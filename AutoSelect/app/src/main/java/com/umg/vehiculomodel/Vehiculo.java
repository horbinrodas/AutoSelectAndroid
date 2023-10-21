package com.umg.vehiculomodel;

public class Vehiculo {
    int id;
    int modelo;
    String img_link;
    String marca;
    String linea;

    public Vehiculo(int id, int modelo, String img_link, String marca, String linea) {
        this.id = id;
        this.modelo = modelo;
        this.img_link = img_link;
        this.marca = marca;
        this.linea = linea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }
}
