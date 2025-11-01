package dev.ng5m;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AccessToken {

    @SerializedName("access_token")
    public String token;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public long expiresIn;
    public @Nullable String scope;
    @SerializedName("refresh_token")
    public @Nullable String refreshToken;


    public String toTokenString() {
        return tokenType + " " + token;
    }


}
