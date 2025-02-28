import java.util.ArrayList;
import java.util.Scanner;

class ToDoList {
    public static void main(String[] args) {
        ArrayList<String> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("เลือกคำสั่ง: 1) เพิ่มงาน 2) ลบงาน 3) แสดงรายการ 4) ออกจากระบบ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // อ่านค่า Enter

            if (choice == 1) {
                System.out.print("ป้อนชื่องาน: ");
                String task = scanner.nextLine();
                tasks.add(task);
            } else if (choice == 2) {
                System.out.print("ป้อนหมายเลขงานที่ต้องการลบ: ");
                int index = scanner.nextInt();
                if (index >= 0 && index < tasks.size()) {
                    tasks.remove(index);
                } else {
                    System.out.println("ไม่มีงานในลำดับนี้");
                }
            } else if (choice == 3) {
                System.out.println("รายการงาน:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(i + ". " + tasks.get(i));
                }
            } else if (choice == 4) {
                break;
            }
        }
        scanner.close();
    }
}

