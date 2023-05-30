package com.example.wanandroidmvp.api

import com.example.wanandroidmvp.mvp.model.bean.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //获取轮播图
    @GET("banner/json")
    fun getBanners(): Observable<HttpResult<List<Banner>>>

    //获取置顶文章列表
    @GET("article/top/json")
    fun getTopArticles(): Observable<HttpResult<MutableList<Article>>>

    //获取文章列表
    @GET("article/list/{pageNum}/json")
    fun getArticles(@Path("pageNum") pageNum: Int): Observable<HttpResult<ArticleResponseBody>>

    //获取知识体系
    @GET("tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    //知识体系下的文章
    @GET("article/list/{page}/json")
    fun getKnowledgeList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Observable<HttpResult<ArticleResponseBody>>

    //导航数据
    @GET("navi/json")
    fun getNavigationList(): Observable<HttpResult<List<NavigationBean>>>

    //收藏文章
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    //取消收藏文章
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    //登录
    @POST("user/login")
    @FormUrlEncoded
    fun loginWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<HttpResult<LoginData>>

    //注册
    @POST("user/register")
    @FormUrlEncoded
    fun registerWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String,
    ): Observable<HttpResult<LoginData>>

    //退出登录
    @GET("user/logout/json")
    fun logout(): Observable<HttpResult<Any>>

    //获取个人信息，需登录后访问
    @GET("/lg/coin/userinfo/json")
    fun getUserInfo(): Observable<HttpResult<UserInfoBody>>

    //获取公众号列表
    @GET("/wxarticle/chapters/json")
    fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>>

    //广场列表数据
    @GET("user_article/list/{page}/json")
    fun getSquareList(@Path("page") page: Int): Observable<HttpResult<ArticleResponseBody>>

    //获取项目
    @GET("project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>

    //获取项目列表
    @GET("project/list/{page}/json")
    fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Observable<HttpResult<ArticleResponseBody>>

    //获取收藏列表
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Observable<HttpResult<BaseListResponseBody<CollectionArticle>>>

    //收藏列表中取消收藏文章
    @POST("lg/uncollect/{id}/json")
    fun removeCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): Observable<HttpResult<Any>>

    //搜索

    //关键词搜索
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun queryBySearchKey(
        @Path("page") page: Int,
        @Field("k") key: String
    ): Observable<HttpResult<ArticleResponseBody>>


    //分享文章
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    fun shareArticle(@FieldMap map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    //搜索热词
    @GET("hotkey/json")
    fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>>

    //获取个人积分列表
    @GET("/lg/coin/list/{page}/json")
    fun getUserScoreList(@Path("page") page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>>

    //获取积分排名
    @GET("/coin/rank/{page}/json")
    fun getRankList(@Path("page") page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>>

    //获取TODO列表数据
    @POST("/lg/todo/list/{type}/json")
    fun getTodoList(@Path("type") type: Int): Observable<HttpResult<AllTodoResponseBody>>

    //获取未完成Todo列表
    @POST("/lg/todo/listnotdo/{type}/json/{page}")
    fun getNoTodoList(
        @Path("page") page: Int,
        @Path("type") type: Int
    ): Observable<HttpResult<TodoResponseBody>>

    //获取已完成的列表
    @POST("/lg/todo/listdone/{type}/json/{page}")
    fun getDoneList(
        @Path("page") page: Int,
        @Path("type") type: Int
    ): Observable<HttpResult<TodoResponseBody>>

    //仅更新完成状态TODO
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun updateTodoById(
        @Path("id") id: Int,
        @Field("status") status: Int
    ): Observable<HttpResult<Any>>

    //删除一条Todo
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodoById(@Path("id") id: Int): Observable<HttpResult<Any>>

    //新增一条Todo
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@FieldMap map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    //更新一条Todo内容
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(
        @Path("id") id: Int,
        @FieldMap map: MutableMap<String, Any>
    ): Observable<HttpResult<Any>>

}