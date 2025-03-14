package com.example.appmanager.retrofit;

import com.example.appmanager.Model.CateModel;
import com.example.appmanager.Model.MessageModel;
import com.example.appmanager.Model.OrderModel;
import com.example.appmanager.Model.ProductModel;
import com.example.appmanager.Model.ResultModel;
import com.example.appmanager.Model.RevenueReportModel;
import com.example.appmanager.Model.SalesModel;
import com.example.appmanager.Model.SalesReportModel;
import com.example.appmanager.Model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiClothing {
    @GET("getCate.php")
    Observable<CateModel> getCate();

    @POST("getproduct.php")
    @FormUrlEncoded
    Observable<ProductModel> getProduct(
            @Field("status") int status
    );

    @POST("addproduct.php")
    @FormUrlEncoded
    Observable<ResultModel> addProduct(
            @Field("name") String name,
            @Field("url_img") String url_img,
            @Field("price") String price,
            @Field("info") String info,
            @Field("inventory_quantity") int inventory_quantity,
            @Field("category") String category
    );

    @POST("signup.php")
    @FormUrlEncoded
    Observable<UserModel> signUp(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("address") String address,
            @Field("phonenumber") String phonenumber,
            @Field("email") String email,
            @Field("password") String password,
            @Field("uid") String uid
    );

    @POST("getorder.php")
    @FormUrlEncoded
    Observable<OrderModel> getOrder(
            @Field("iduser") int iduser
    );

    @POST("updateStatusOrder.php")
    @FormUrlEncoded
    Observable<MessageModel> updateStatusOrder(
            @Field("id") int id,
            @Field("status") String status
    );

    @GET("getsales.php")
    Observable<SalesModel> getSales();

    @GET("getUser.php")
    Observable<UserModel> getUser();

    @GET("salesreport.php")
    Observable<SalesReportModel> salesReport();

    @GET("revenuereport.php")
    Observable<RevenueReportModel> revenueReport();

    @FormUrlEncoded
    @POST("deleteproduct.php")
    Observable<ResultModel> deleteProduct(
            @Field("id") int id
    );

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> getToken(
            @Field("status") int status,
            @Field("iduser") int iduser
    );

    @POST("signin.php")
    @FormUrlEncoded
    Observable<UserModel> signIn(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("updateproduct.php")
    @FormUrlEncoded
    Observable<ResultModel> updateProduct(
            @Field("id") int id,
            @Field("name") String name,
            @Field("url_img") String url_img,
            @Field("price") String price,
            @Field("info") String info,
            @Field("inventory_quantity") int inventory_quantity,
            @Field("category") String category
    );

    @POST("send_email.php")
    @FormUrlEncoded
    Observable<MessageModel> send_email_order_status(
            @Field("email") String email,
            @Field("subject") String subject,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("deletesale.php")
    Observable<ResultModel> deleteSale(
            @Field("id") int id
    );

    @POST("addsale.php")
    @FormUrlEncoded
    Observable<ResultModel> addsale(
            @Field("url") String url,
            @Field("info") String info
    );

    @POST("updateSale.php")
    @FormUrlEncoded
    Observable<ResultModel> updateSale(
            @Field("id") int id,
            @Field("url") String url,
            @Field("info") String info
    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("iduser") int iduser,
            @Field("token") String token
    );

    @POST("updatepass.php")
    @FormUrlEncoded
    Observable<MessageModel> updatePass(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("checkrule.php")
    @FormUrlEncoded
    Observable<MessageModel> checkRule(
            @Field("email") String email,
            @Field("password") String password
    );
}
