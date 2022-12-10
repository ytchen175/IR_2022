import email.message
import smtplib
import sqlite3

conn = sqlite3.connect('IR_System.db')

c = conn.cursor()

keyword_subscription_template = """
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:v="urn:schemas-microsoft-com:vml">
  <body>
<pre>
  /\ \ \/__   \/\ /\/__   \   \_   \/__\ 
 /  \/ /  / /\/ / \ \ / /\/    / /\/ \// 
/ /\  /  / /  \ \_/ // /    /\/ /_/ _  \ 
\_\ \/   \/    \___/ \/     \____/\/ \_/
</pre>
 <p>---∴°﹒☆°．﹒°∴°﹒★°．﹒∵‧°∴°﹒☆°°∴°﹒﹒‧°∴°﹒☆°---</p>
  <br></br>
	<td align="left" >
	  <h1 style="color:#333333;font-size:18px;font-weight:bold;font-family:'PingFang TC','微軟正黑體','Microsoft JhengHei','Helvetica Neue',Helvetica,Arial,sans-serif;padding:0;margin:0;line-height:1.4">
		<br>Hi {},</br> 
		<br>您訂閱的關鍵字「{}」新增了一些搜尋結果唷！</br>
		<br>趕緊來看看吧😍</br>
	  </h1>
	</td>
  </body>
</html>
"""

get_keywords_and_email_by_username_query = """
SELECT 
    s.UserName, m.Email, s.SubscriptionKeyword
FROM
    Subscription as s
INNER JOIN
    Member as m
"""

c.execute(
    get_keywords_and_email_by_username_query
)

user_subscribed_keywords = c.fetchall() # [('IRSystem', 'ntut.ir.system@gmail.com', 'computer'), ('IRSystem', 'ntut.ir.system@gmail.com', 'art')]

# extract mail, user, keyword from db
user_keywords = dict()
user_email = dict()

for i in user_subscribed_keywords:
    user, mail, keyword = i[0], i[1], i[2]

    if user not in user_keywords:
        user_keywords.update({user: []})

    user_keywords[user].append(keyword) # {'IRSystem': ['computer', 'art']}

    user_email.update({user: mail}) # {'IRSystem': 'ntut.ir.system@gmail.com'}



with smtplib.SMTP(host="smtp.gmail.com", port="587") as smtp:  # 設定SMTP伺服器
    try:
        smtp.ehlo()  # 驗證SMTP伺服器
        smtp.starttls()  # 建立加密傳輸
        smtp.login("ntut.ir.system@gmail.com", "zrhtixerqbccthwx")  # 登入寄件者gmail

        for username, keywords_list in user_keywords.items():
            mail = user_email[username] # get user email
            keywords = ', '.join(keywords_list)

            content = email.message.EmailMessage()
            content["Subject"] = f"Keyword Subscription for 「{keywords}」 in NTUT IR System"  #郵件標題
            content["From"] = "ntut.ir.system@gmail.com"  #寄件者
            content["To"] = mail #收件者

            content.add_alternative(
                keyword_subscription_template.format(username, keywords), subtype = "html"
            )  # 郵件內容

            smtp.send_message(content)  # 寄送郵件

            print(f"Mail sent to {username}!")

    except Exception as e:
        print("Error message: ", e)
