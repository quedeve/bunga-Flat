/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.victor.kampus.jdbc.bunga_flat;

/**
 *
 * @author victo
 */
import java.sql.*;
import java.util.Scanner;

public class BungaFlat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

         Pinjaman rent = new Pinjaman();
        Scanner sn = new Scanner(System.in);
        
        
        System.out.println("Masukan Nama Nasabah : ");
        String namaNasabah = sn.next();
        System.out.print("Masukan Besar Pinjaman : ");
        rent.setPinjam(sn.nextLong());
        System.out.print("Masukan Besar Bunga    : ");
        rent.setBunga(sn.nextInt());
        System.out.print("Masukan Banyak Angsuran: ");
        rent.setKali(sn.nextInt());

        rent.PrintScr(namaNasabah);

    }



}
