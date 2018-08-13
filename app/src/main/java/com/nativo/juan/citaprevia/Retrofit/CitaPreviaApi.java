package com.nativo.juan.citaprevia.Retrofit;

import com.nativo.juan.citaprevia.Res.apiRespuestaCitas;
import com.nativo.juan.citaprevia.model.Afiliados;
import com.nativo.juan.citaprevia.model.ApiMessageResponse;
import com.nativo.juan.citaprevia.model.CitaDisplayList;
import com.nativo.juan.citaprevia.Res.DoctorsAvailabilityRes;
import com.nativo.juan.citaprevia.model.Issue;
import com.nativo.juan.citaprevia.model.IssueType;
import com.nativo.juan.citaprevia.Res.MedicalsCenterRes;
import com.nativo.juan.citaprevia.model.JSONResponse;
import com.nativo.juan.citaprevia.model.PostAppointmentsBody;

import com.nativo.juan.citaprevia.model.LoginBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by juan on 3/31/18.
 */



public interface CitaPreviaApi {

    //public static final String BASE_URL = "http://10.0.0.1/blog/api.saludmock.com/v1/";
    public static final String BASE_URL = "https://agile-beyond-67966.herokuapp.com/api/";

    //URL BASE para realizar las conexiones con las urls simples
   // public static final String BASE_URL = "http://192.168.0.105:8080/api/";


    String HEADER_AUTHORIZATION = "Authorization";

    //endpoint para el login retornando la clase afiliado y como paramentros la clase
    //LoginBody, de la funcion login
    @POST("affiliates/login")
    Call<Afiliados> login(@Body LoginBody loginBody);

    //endpoint para obtener las citas del servidor, tomando los resultados de el modelo,
    //apiRespuestaCitas, el cual hace referencia al modelo citaDisplayList
    @GET("appointments")
    Call<apiRespuestaCitas> getCitas(@Header("Authorization")String token,
                                     @QueryMap Map<String, Object> parameters);


    @Headers("Content-Type: application/json")
    @POST("appointments")
    Call<ApiMessageResponse> createAppointment(@Header("Authorization") String token,
                                               @Body PostAppointmentsBody body);

    @Headers("Content-Type: application/json")
    @PATCH("appointments/{id}")
    Call<ApiMessageResponse> cancelarCitas(@Path("id") String citaId,
                                           @Header("Authorization") String token,
                                           @Body HashMap<String, String> statusMap);

    @GET("medical-centers")
    Call<MedicalsCenterRes> getMedicalCenters(@Header("Authorization") String token);

    @GET("doctors")
    Call<DoctorsAvailabilityRes> getDoctorsSchedules(@Header("Authorization") String token,
                                                     @QueryMap Map<String, Object> parameters);

    @GET("issue-types")
    Call<List<IssueType>> getIssueTypes(@Header(HEADER_AUTHORIZATION) String token);

    @Multipart
    @POST("issues")
    Call<Issue> createIssue(@Header(HEADER_AUTHORIZATION) String token,
                            @Part("issue_type") RequestBody type,
                            @Part("description") RequestBody description,
                            @Part MultipartBody.Part image);

    @GET("android/jsonandroid")
    Call<JSONResponse> getJSON();

}
