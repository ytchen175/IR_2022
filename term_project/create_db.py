import sqlite3

conn = sqlite3.connect('IR_System.db')

c = conn.cursor()

# # create table
# c.execute('''CREATE TABLE Member
#        (
#        ID INTEGER PRIMARY KEY,
#        UserName TEXT,
#        Password TEXT NOT NULL,
#        Email TEXT NOT NULL
#        );''')

# c.execute('''CREATE TABLE Subscription
#        (
#        ID INTEGER PRIMARY KEY,
#        UserName TEXT,
#        SubscriptionKeyword TEXT
#        );''')

# # insert data
# c.execute("INSERT INTO Member (UserName, Password, Email) \
#       VALUES ('IRSystem', 'NTUT_IR', 'ntut.ir.system@gmail.com')")

# # if member doesn't use keywords subscription services, then don't insert data into Subscription table 
# username_keywords = [
#     ('IRSystem', 'computer'),
#     ('IRSystem', 'art')
# ]
# c.executemany('INSERT INTO Subscription(UserName, SubscriptionKeyword) VALUES (?,?)', username_keywords)

# # print rows
# for row in c.execute("SELECT SubscriptionKeyword FROM Subscription where UserName = 'IRSystem'"):
#     print(row)

conn.commit()
conn.close()
