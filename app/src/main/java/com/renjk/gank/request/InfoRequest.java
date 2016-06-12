package com.renjk.gank.request;

import com.renjk.gank.bean.AndroidInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by admin on 2016/6/6.
 */
public interface InfoRequest {
    @GET("{tab}/{size}/{pageIndex}")
    Call<AndroidInfo> getAndroidResult(@Path("tab") String tab,@Path("size") String size,@Path("pageIndex") String pageIndex);

//    @GET("/group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
//
//    @POST("/users/new")
//    Call<User> createUser(@Body User user);

    //RxJava
   /* @GET("/user")
    public Observable<User> getUser(@Query("userId") String userId);*/
}
