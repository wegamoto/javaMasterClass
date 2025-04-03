package com.wewe;

import java.sql.*;

public class SQLiteExample {
    public static void main(String[] args) {
        // ระบุพาธไปยังฐานข้อมูล SQLite
        String url = "jdbc:sqlite:C:/databases/music/music.db";

        try {
            // โหลด SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");

            // เชื่อมต่อฐานข้อมูล
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite database!");

            // สร้างตาราง
            String createTableSQL = "CREATE TABLE IF NOT EXISTS songs ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "title TEXT NOT NULL, "
                    + "artist TEXT NOT NULL, "
                    + "duration INTEGER)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Table 'songs' created or already exists.");
            }

            // แทรกข้อมูล
            String insertSQL = "INSERT INTO songs (title, artist, duration) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "Lofi Chill");
                pstmt.setString(2, "DJ Relax");
                pstmt.setInt(3, 210);
                pstmt.executeUpdate();
                System.out.println("Song inserted!");
            }

            // อ่านข้อมูล
            String selectSQL = "SELECT * FROM songs";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {

                System.out.println("Song List:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            ", Title: " + rs.getString("title") +
                            ", Artist: " + rs.getString("artist") +
                            ", Duration: " + rs.getInt("duration") + " sec");
                }
            }

            // ปิดการเชื่อมต่อ
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

