package com.example.appmanager.retrofit;

import com.example.appmanager.Model.CateModel;
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

    @FormUrlEncoded
    @POST("deleteproduct.php")
    Observable<ResultModel> deleteProduct(
            @Field("id") int id
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
}
