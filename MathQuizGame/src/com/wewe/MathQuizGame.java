package com.wewe;

import java.util.Random;
import java.util.Scanner;

public class MathQuizGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int score = 0;
        int numQuestions = 5; // จำนวนคำถามที่ต้องตอบ

        System.out.println("ยินดีต้อนรับสู่เกมฝึกคณิตศาสตร์!");

        // สร้างเกม 5 รอบ
        for (int i = 0; i < numQuestions; i++) {
            // สุ่มตัวเลขและการคำนวณ
            int num1 = random.nextInt(10) + 1; // ตัวเลข 1-10
            int num2 = random.nextInt(10) + 1; // ตัวเลข 1-10
            int answer = 0;
            String operator = "+";

            // สุ่มการคำนวณ
            int operation = random.nextInt(4); // 0 = บวก, 1 = ลบ, 2 = คูณ, 3 = หาร
            if (operation == 0) {
                answer = num1 + num2;
                operator = "+";
            } else if (operation == 1) {
                answer = num1 - num2;
                operator = "-";
            } else if (operation == 2) {
                answer = num1 * num2;
                operator = "*";
            } else if (operation == 3) {
                answer = num1 / num2;
                operator = "/";
            }

            // ถามคำถาม
            System.out.print("คำถาม " + (i + 1) + ": " + num1 + " " + operator + " " + num2 + " = ");
            int userAnswer = scanner.nextInt();

            // ตรวจสอบคำตอบ
            if (userAnswer == answer) {
                System.out.println("ถูกต้อง!");
                score++;
            } else {
                System.out.println("ผิด! คำตอบที่ถูกต้องคือ: " + answer);
            }
        }

        // แสดงผลคะแนน
        System.out.println("คุณตอบถูก " + score + " จาก " + numQuestions + " คำถาม!");
    }
}


