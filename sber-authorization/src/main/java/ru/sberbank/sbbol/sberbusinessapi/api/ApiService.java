package ru.sberbank.sbbol.sberbusinessapi.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.sberbank.sbbol.sberbusinessapi.model.ChangeClientSecretResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenBodyResponse;

interface ApiService {
    @FormUrlEncoded
    @POST("/ic/sso/api/oauth/token")
    Call<TokenBodyResponse> accessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("client_secret") String clientSecret,
            @Field("code_verifier") String codeVerifier
    );

    @FormUrlEncoded
    @POST("/ic/sso/api/oauth/token")
    Call<TokenBodyResponse> refreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("client_secret") String clientSecret,
            @Field("code_verifier") String codeVerifier
    );

    @FormUrlEncoded
    @POST("/ic/sso/api/v1/change-client-secret")
    Call<ChangeClientSecretResponse> changeClientSecret(
            @Field("access_token") String accessToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("new_client_secret") String newClientSecret
    );

    @FormUrlEncoded
    @POST("/ic/sso/api/v2/oauth/revoke")
    Call<Void> revokeToken(
            @Header("Authorization") String accessToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("token") String token,
            @Field("token_type_hint") String token_type_hint
    );

    @GET("/ic/sso/api/v2/oauth/user-info")
    Call<String> userInfo(@Header("Authorization") String accessToken);
}
