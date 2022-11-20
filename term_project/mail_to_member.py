import email.message
import smtplib

keyword_subscription_template = """
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:v="urn:schemas-microsoft-com:vml">
  <body>
<pre>
  /\ \ \/__   \/\ /\/__   \   \_   \/__\ 
 /  \/ /  / /\/ / \ \ / /\/    / /\/ \// 
/ /\  /  / /  \ \_/ // /    /\/ /_/ _  \ 
\_\ \/   \/    \___/ \/     \____/\/ \_/
</pre>
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

mail_list = ["xxx@gmail.com", "ooo@gmail.com", "ntut.ir.system@gmail.com"]
user_list = ['xxx', 'ooo', 'IR']
keyword_list = ['capoo', 'kirby', 'pikachu']

with smtplib.SMTP(host="smtp.gmail.com", port="587") as smtp:  # 設定SMTP伺服器
    try:
        smtp.ehlo()  # 驗證SMTP伺服器
        smtp.starttls()  # 建立加密傳輸
        smtp.login("ntut.ir.system@gmail.com", "zrhtixerqbccthwx")  # 登入寄件者gmail

        for mail, username, keyword in zip(mail_list, user_list, keyword_list):
            content = email.message.EmailMessage()
            content["Subject"] = f"Keyword Subscription for {keyword} in NTUT IR System"  #郵件標題
            content["From"] = "ntut.ir.system@gmail.com"  #寄件者
            content["To"] = mail #收件者

            content.add_alternative(
                keyword_subscription_template.format(username, keyword), subtype="html"
            )  #郵件內容

            smtp.send_message(content)  # 寄送郵件

            print(f"Mail sent to {username}!")

    except Exception as e:
        print("Error message: ", e)
