# Food_Order_system
   This Is a minor project in java using JDBC(Database managment) ,socket programming(Data transfer) and Swing(For UI).
   Three java files Chefserver.java ,customer.java and MenuAndBill.java works simultaneously .
   #customer.java will show the initial options for customer to order food and to see bill.
   #MenuAndBill.java provides a backend support to transfer the order data to chefserver.java using socket programming.
   #Chefserver fetch the data and add it to jdbc database whic contains 3 tables
     i.e(Menu ,Orders , accepted_order).
    -> Each food item have a unique id , which is converted to string by combining id's of all order with table number.
    -> this string will br recieved by chefserver through socket prog. and decoded.
    -> Once customer finishes his order he ask for bill and it generates it immediately and at same time bill will be shown to chef side
    ->As soon as chef confirms the bill , order data gets deleted from accepted_order table.
    <img src="https://github.com/rohit23gd/Food_Order_system/blob/48c1ddb178d240bef499aa3c1f185d2690685e33/IMG-20210422-WA0004.jpg" width="800" height="300">
<img src="https://github.com/rohit23gd/Food_Order_system/blob/d79dd4bd2ceb622e78d6a8a93c2b68e62a028d2f/IMG-20210420-WA0022.jpg" width="800" height="300">
<img src="https://github.com/rohit23gd/Food_Order_system/blob/48c1ddb178d240bef499aa3c1f185d2690685e33/IMG-20210420-WA0018.jpg" width="800" height="300">

<img src="https://github.com/rohit23gd/Food_Order_system/blob/48c1ddb178d240bef499aa3c1f185d2690685e33/IMG-20210422-WA0002.jpg" width="800" height="300">
