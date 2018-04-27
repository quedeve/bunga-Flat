/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.victor.kampus.jdbc.bunga_flat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victo
 */
public class Pinjaman {

    public static Connection getKoneksiKeDB() throws SQLException {
        String URL = "jdbc:sqlserver://localhost;databaseName=DB_Bunga;instanceName=SQLEXPRESS_2017";
        String Username = "sa";
        String Password = "localhost";

        return DriverManager.getConnection(URL, Username, Password);
    }

    float Pinjam;
    float Angsuran, AngsuranBulan, AngsuranTotal, bungaBulan;
    float Kali, Bunga, i;
    float Sisa;

    public void setPinjam(long Pinjam) {
        this.Pinjam = Pinjam;
        Sisa = Pinjam;
    }

    public void setBunga(int Bunga) {
        this.Bunga = Bunga;

    }

    public void setKali(int Kali) {
        this.Kali = Kali;
    }

    public void membuatNol() {
        AngsuranTotal = 0;
    }

    public void bungaPerbulan() {

        bungaBulan = (float) Pinjam * (Bunga / 100) / Kali;

    }

    public void angsuranPokokPerbulan() {
        AngsuranBulan = Pinjam / Kali;
    }

    public void jumlahAngsuran() {
        Angsuran = AngsuranBulan + bungaBulan;
    }

    public void totalAngsuran() {
        AngsuranTotal = AngsuranTotal + Angsuran;
    }

    public void inSisa() {
        Sisa = (long) ((float) Sisa - AngsuranBulan);

    }

    public void PrintScr(String namaNasabah) {

        membuatNol();
        angsuranPokokPerbulan();
        Connection connection = null;
        String sql;
        PreparedStatement prestatement;
        int increment = 1;
        try {
            System.out.println("state 1");
            connection = getKoneksiKeDB();
            //Mematikan fitur autocommit
            connection.setAutoCommit(false);
            BigDecimal pinjaman = new BigDecimal(Pinjam);
            sql = "SELECT MAX(ID) FROM Nasabah";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT MAX(ID) AS ID FROM Nasabah");
            System.out.println("a");
            while (resultSet.next()) {

                increment = increment + resultSet.getInt("ID");
                if (increment == 0) {
                    increment = 1;

                }
            }

            connection.commit();
            statement.close();
            connection.close();
            connection = getKoneksiKeDB();
            connection.setAutoCommit(false);
            sql = "INSERT into DB_Bunga.dbo.Nasabah(ID, NAMA_NASABAH, Pinjaman, Bulan) values (?,?,?,?)";
            prestatement = connection.prepareStatement(sql);
            prestatement.setInt(1, increment);
            prestatement.setString(2, namaNasabah);
            prestatement.setBigDecimal(3, pinjaman);
            prestatement.setInt(4, (int) Kali);
//            for (String value : kategori) {
//                prestatement.setString(1, value);
//                prestatement.addBatch();
//            }
//            prestatement.executeBatch();
//            prestatement.clearBatch();
            prestatement.executeUpdate();

            connection.commit();

            prestatement.close();
            connection.close();

            System.out.println("Masuk");
            for (i = 1; i <= Kali; i++) {
                if (i == 1) {

                } else {
                    inSisa();
                }

                bungaPerbulan();
                jumlahAngsuran();
                totalAngsuran();

//            System.out.println("|      " + i + " |   Rp. " + (long) Sisa + "  |   Rp. " + AngsuranBulan + "                 | "
//                    + "Rp.  " + bungaBulan + "             |   Rp." + Angsuran + " |");
//
//            System.out.println("=============================================================================================================");
                BigDecimal pokok;
                BigDecimal bunga;
                BigDecimal jumlahAngsuran;
                BigDecimal saldoAngsuran;
                pokok = BigDecimal.valueOf(AngsuranBulan);
                bunga = BigDecimal.valueOf(bungaBulan);
                jumlahAngsuran = BigDecimal.valueOf(Angsuran);
                saldoAngsuran = BigDecimal.valueOf(Sisa);

                connection = getKoneksiKeDB();
                connection.setAutoCommit(false);
                sql = "INSERT INTO PinjamanDetil(POKOK, BUNGA, JUMLAH_ANGSURAN, SALDO_ANGSURAN, INFO, ID_NASABAH) VALUES(?,?,?,?,?,?)";
                prestatement = connection.prepareStatement(sql);
                prestatement.setBigDecimal(1, pokok);
                prestatement.setBigDecimal(2, bunga);
                prestatement.setBigDecimal(3, jumlahAngsuran);
                prestatement.setBigDecimal(4, saldoAngsuran);
                prestatement.setString(5, "Angsuran ke-" + i);
                prestatement.setInt(6, increment);
                prestatement.executeUpdate();
                connection.commit();

                prestatement.close();
                connection.close();
                System.out.println("Data Telah Masuk");

//        System.out.println("|      Jumlah |     |   Rp. " + (long) Pinjam + "                 | "
//                + "Rp.  " + +(long) (AngsuranTotal - Pinjam) + "             |   Rp." + (long) AngsuranTotal + " |");
//        System.out.println("=============================================================================================================");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Rollback Berhasil");
                } catch (SQLException sqle2) {
                    System.out.println("Tidak dapat melakukan rollback");
                    sqle2.printStackTrace();
                }

            }
        }
//        System.out.println("\n"
//                + "======================================================================================================================");
//        System.out.println("| Angsuran ke- | Saldo Angsuran     | Besar Angsuran          |     Bunga                 |    Jumlah Angsuran  |");
//        System.out.println("=================================================================================================================");

    }
}
