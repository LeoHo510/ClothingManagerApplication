package com.example.appmanager.retrofit;

import com.example.appmanager.Model.CateModel;
import com.example.appmanager.Model.OrderModel;
import com.example.appmanager.Model.ProductModel;
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
    Observable<ProductModel> addProduct(
            @Field("name") String name,
            @Field("url_img") String url_img,
            @Field("price") String price,
            @Field("info") String info,
            @Field("inventory_quantity") int inventory_quantity,
            @Field("category") String category
    );

    @POST("getorder.php")
    @FormUrlEncoded
    Observable<OrderModel> getOrder(
            @Field("iduser") int iduser
    );

    @GET("getsales.php")
    Observable<SalesModel> getSales();

    @GET("getUser.php")
    Observable<UserModel> getUser();

    @GET("salesreport.php")
    Observable<SalesReportModel> salesReport();

    @GET("revenuereport.php")
    Observable<RevenueReportModel> revenueReport();
}
