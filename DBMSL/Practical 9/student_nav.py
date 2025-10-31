import oracledb

oracledb.init_oracle_client(lib_dir=r"C:\app\kotka\product\21c\dbhomeXE\BIN")

conn = oracledb.connect(user="system", password="1606", dsn=None)
cursor = conn.cursor()
print("Connected to Oracle XE (Thick Mode)")

def add_student():
    sid = int(input("Enter Student ID: "))
    name = input("Enter Student Name: ")
    dept = input("Enter Department: ")
    cursor.execute("INSERT INTO Student (stud_id, stud_name, dept) VALUES (:1, :2, :3)", (sid, name, dept))
    conn.commit()
    print("Student Added Successfully")

def view_students():
    cursor.execute("SELECT * FROM Student ORDER BY stud_id")
    rows = cursor.fetchall()
    if not rows:
        print("⚠ No Records Found")
    else:
        print("\n--- Student Records ---")
        for r in rows:
            print(f"ID: {r[0]}, Name: {r[1]}, Dept: {r[2]}")

def update_student():
    sid = int(input("Enter Student ID to Update: "))
    new_name = input("Enter New Name: ")
    cursor.execute("UPDATE Student SET stud_name=:1 WHERE stud_id=:2", (new_name, sid))
    conn.commit()
    print("Student Updated Successfully")

def delete_student():
    sid = int(input("Enter Student ID to Delete: "))
    cursor.execute("DELETE FROM Student WHERE stud_id=:1", (sid,))
    conn.commit()
    print("Student Deleted Successfully")

# ---------- Menu ----------
while True:
    print("\n===== Student Database Menu =====")
    print("1. Add Student")
    print("2. View Students")
    print("3. Update Student")
    print("4. Delete Student")
    print("5. Exit")
    choice = input("Enter choice: ")

    if choice == "1":
        add_student()
    elif choice == "2":
        view_students()
    elif choice == "3":
        update_student()
    elif choice == "4":
        delete_student()
    elif choice == "5":
        print("Exiting Program...")
        break
    else:
        print("Invalid Choice. Try Again!")

# ---------- Close Connection ----------
cursor.close()
conn.close()
