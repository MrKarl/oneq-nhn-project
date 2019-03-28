package com.toast.oneq.vo;

public class PaycoVo {
    private String accessToken;
    private String accessTokenSecret;
    private String refreshToken;
    private String tokenType;
    private String expiresIn;
    private String macKey;
    private String macAlgorithm;
    private String state;
    private String error;
    private String errorDescription;
    private ProfileBasic profileBasic;
    private Header header;

    public ProfileBasic getProfileBasic() {
        return profileBasic;
    }
    public void setProfileBasic(ProfileBasic profileBasic) {
        this.profileBasic = profileBasic;
    }
    public void setIdNo(String id) {
        if ( this.profileBasic == null ) {
            this.profileBasic = new ProfileBasic();
        }
        this.profileBasic.setIdNo(id);
    }
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getAccess_token() {
        return accessToken;
    }
    public void setAccess_token(String access_token) {
        this.accessToken = access_token;
    }
    public String getAccess_token_secret() {
        return accessTokenSecret;
    }
    public void setAccess_token_secret(String access_token_secret) {
        this.accessTokenSecret = access_token_secret;
    }
    public String getRefresh_token() {
        return refreshToken;
    }
    public void setRefresh_token(String refresh_token) {
        this.refreshToken = refresh_token;
    }
    public String getToken_type() {
        return tokenType;
    }
    public void setToken_type(String token_type) {
        this.tokenType = token_type;
    }
    public String getExpires_in() {
        return expiresIn;
    }
    public void setExpires_in(String expires_in) {
        this.expiresIn = expires_in;
    }
    public String getMac_key() {
        return macKey;
    }
    public void setMac_key(String mac_key) {
        this.macKey = mac_key;
    }
    public String getMac_algorithm() {
        return macAlgorithm;
    }
    public void setMac_algorithm(String mac_algorithm) {
        this.macAlgorithm = mac_algorithm;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getError_description() {
        return errorDescription;
    }
    public void setError_description(String error_description) {
        this.errorDescription = error_description;
    }
    public class ProfileBasic {
        private String idNo;
        private String id;
        private String mobileId;
        public String getIdNo() {
            return idNo;
        }
        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getMobileId() {
            return mobileId;
        }
        public void setMobileId(String mobileId) {
            this.mobileId = mobileId;
        }
    }
    public class Header {
        private int resultCode;
        private boolean successful;
        private String resultMessage;
        public int getResultCode() {
            return resultCode;
        }
        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }
        public boolean isSuccessful() {
            return successful;
        }
        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }
        public String getResultMessage() {
            return resultMessage;
        }
        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }
    }
}
