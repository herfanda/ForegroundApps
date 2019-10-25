package cartenz.yunus.foregroundapps.networks.responses;

public enum ResponseStatus {

    // success response
//    ADVOCATE_AND_SERIAL_NUMBER_VALID(200, "OK", "OK"),
    // valid advocate and serial number
//    CLAIM_SUCCEEDED(201, "Claim Succeeded", "Proses klaim berhasil"),
//    API_REQUEST_SUCCEEDED(202, "Api Request is already succeeded", "API Request berhasil"),
//    ACTIVATION_SUCCEEDED(203, "Activation Succeeded", "Proses aktivasi berhasil"),
//    PENDING_APPROVAL_FOR_CLIENT_PAIRING(221, "Advocate is waiting to be approved by client", "Pendaftaran sedang diproses"),
//    APPROVED_FOR_CLIENT_PAIRING(222, "Approved for client pairing", "Proses pendaftaran berhasil"),
//    BALANCE_ELIGIBLE_FOR_DISBURSEMENT (223, "Eligible for disbursement", "Saldo mencukupi untuk pencairan"),
//    BALANCE_NOT_ELIGIBLE_FOR_DISBURSEMENT (224, "Not eligible for disbursement", "Saldo belum mencukupi untuk pencairan"),

    // umb claim failed response
    // range from 400 - 419
//    INVALID_SERIAL_NUMBER(400, "Invalid Serial Number", "Kode voucher tidak valid"),
    // invalid serial number
//    NO_ADVOCATE_FOUND(401, "No advocate found", "Advocate user tidak ditemukan"),
    // invalid advocate
//    NO_BATCH_FOUND(402, "No product batch found", "Kode batch produk tidak ditemukan"),
    // invalid batch
//    INVALID_CLIENT_ADVOCATE_RELATIONSHIP(403, "This advocate is not under this client", "Advocate user dan client tidak sesuai"),
    // invalid client-advocate
//    ADVOCATE_IS_BLACKLISTED(404, "Advocate is blacklisted", "Advocate user telah diblokir"),
//    CLAIM_FAILED(405, "Mohon maaf, sistem kami sedang mengalami gangguan. Silahkan coba beberapa menit lagi", "Sistem mengalami gangguan. Silahkan coba beberapa saat lagi."),
    // claim failed, system error
//    SECOND_ATTEMPT_CLAIM(406, "You have already claimed this serial number", "Kode voucher telah Anda klaim sebelumnya"),
    // second claim by the same advocate
//    SOMEONE_ALREADY_CLAIM(407, "This serial number has already been claimed by someone", "Kode voucher telah diklaim"),
    // second claim by different advocate
//    MORE_THAN_TWICE_CLAIM_ATTEMPT(408, "This serial number has been claimed more than twice", "Kode voucher telah diklaim lebih dari dua kali"),
    NO_CALL_CONFLICTED(409, "Mohon maaf, Sales order no call telah terbentuk", "Sales order no call telah terbentuk"), // more than twice claim attempt
    //    CLAIM_FAILED_BY_FILTER_OR_LIMIT(410, "Failed to claim due to limit or filter", "Kode voucher tidak dapat Anda klaim"),
    // advocate activation failed response
    // range from 420 - 449
//    NO_ADVOCATE_ID_FOUND(421, "No Advocate ID found", "Advocate User tidak ditemukan"),
//    ADVOCATE_ID_ALREADY_ACTIVATED(422, "Advocate ID is already activated", "Advocate user telah diaktivasi"),
//    ADVOCATE_ID_ALREADY_ACTIVATED_BY_OTHER_ADVOCATE(423, "Advocate ID is already activated by other advocate", "Advocate user telah diaktivasi oleh advocate lain"),
//    INVALID_UMB_CODE_FORMAT(424, "Invalid format in umb code", "Kode UMB tidak valid"),
//    INVALID_CLIENT_ID(425, "No Client ID found", "Client ID tidak valid"),
    BAD_REQUEST(431, "Your request is invalid", "Permintaan tidak dapat diproses"),
    // api failed response
    // range from 450 - 479
//    NOT_FOUND_ACTION_PARAM(451, "Action is not found in request parameters ", "Request gagal, silahkan coba beberapa saat lagi. Error code: 451"),
//    INVALID_ACTION_VALUE(452, "Invalid action value in request parameters", "Request gagal, silahkan coba beberapa saat lagi. Error code: 452"),
//    NOT_FOUND_USER_ID_PARAM(453, "User Id is not found in request parameters", "Request gagal, silahkan coba beberapa saat lagi. Error code: 453"),
//    NOT_FOUND_USER_ID_IN_SYSTEM(454, "Not found user id in system", " Nomor telpon belum terdaftar di dalam sistem"),
//    NOT_FOUND_SIGNATURE_PARAM(455, "Not found signature param in system", "Request gagal, silahkan coba beberapa saat lagi. Error code: 455"),
//    INVALID_SIGNATURE_VALUE(456, "Signature doesn't match ", "Request gagal, silahkan coba beberapa saat lagi. Error code: 456"),
//    INVALID_USER_ID_VALUE(457, "Invalid value of encrypted User Id", "Request gagal, silahkan coba beberapa saat lagi. Error code: 457"),
//    USER_ID_IS_ALREADY_ASSIGNED(458, "User Id is already assign by others", "Nomor telpon sudah diaktivasi"),
//    NOT_FOUND_TOKEN_PARAM(459, "Not found token param", "Request gagal, silahkan coba beberapa saat lagi. Error code: 459"),
    IMEI_IS_FULL(460, "Login Failed, Please Ensure your Phone's IMEI has been registered", "Login Gagal, Pastikan IMEI HP sudah Terdaftar"),
    INVALID_USERNAME_PASSWORD(461, "Invalid username/password", "Login Gagal, pastikan username/password sudah benar"),
    INVALID_COMPANY(462, "Invalid company", "Login gagal, pastikan company sudah benar"),
//    INVALID_USERNAME_AND_OR_PASSWORD(463, "Invalid username and/or password", "Username dan password tidak sesuai"),
//    NOT_FOUND_SERIAL_NUMBER_PARAM(464, "Not found serial number parameter", "Request gagal, silahkan coba beberapa saat lagi. Error code: 464"),
//    INVALID_LATITUDE_LONGITUDE_VALUE(465, "Invalid latitude and/or longitude value", "Request gagal, silahkan coba beberapa saat lagi. Error code: 465"),
//    ADVOCATE_IS_NOT_REGISTERED(471, "Advocate with this phone number is not registered on the system", "Nomor telepon belum terdaftar di sistem"),
//    ADVOCATE_IS_REGISTERED_AND_NOT_ACTIVE(472, "Advocate with this phone number is registered but not active", "Nomor telepon sudah terdaftar namun belum aktif"),
//    ADVOCATE_IS_REGISTERED_AND_ACTIVE(473, "Advocate with this phone is registered and active", "Nomor telepon sudah terdaftar dan sudah aktif"),
//    CLIENT_PAIRING_FAILED(481, "Failed in client pairing process", "Gagal mendaftarkan ke produk"),
//    NOT_FOUND_CLIENT_ID(482, "Not found client id in parameters", "Request gagal, silahkan coba beberapa saat lagi. Error code: 482"),
//    NOT_FOUND_DISBURSMENT_CODE (483, "Not found disbursement code in parameters", "Kode pencairan tidak ditemukan pada request parameter. Error code: 483"),
//    INVALID_DISBURSEMENT_CODE (484, "Disbursement code is invalid", "Kode pencairan tidak ditemukan di sistem"),
//    DISBURSEMENT_CODE_NOT_MATCH (485, "Disbursement code doesn't match", "Kode pencairan tidak cocok"),
//    DISBURSEMENT_IS_NOT_ELIGIBLE_TO_BE_DISBURSED (486, "Disbursement is not eligible to be disbursed", "Kode pencairan tidak bisa dicairkan/telah dicairkan sebelumnya"),
//    ADVOCATE_IS_NOT_DISBURSEMENT_RECIPIENT (487, "Advocate id is not recipient of disbursement", "Pencairan tidak dapat dilakukan oleh Anda."),
//    INVALID_DISBURSEMENT_TOKEN (488, "Invalid disbursement token", "Kode otentikasi tidak cocok"),
//    EXPIRED_DISBURSEMENT_TOKEN (489, "Disbursement token is already expired", "Kode otentikasi telah kadaluarsa. Silahkan request ulang pencairan"),
//    INVALID_ADVOCATE_PARENT (490, "Invalid advocate parent value. Parent is not assigned yet.", "Silakan pilih toko induk terlebih dahulu"),
//    ADVOCATE_PARENT_ALREADY_ASSIGNED(491, "Advocate parent is already assigned.", "Data Toko induk telah dimasukkan sebelumnya."),
//    NOT_FOUND_ADVOCATE_PARENT (492, "Not found advocate parent", "Tidak ditemukan toko induk di area ini."),
//    NOT_FOUND_RECIPIENT_ADVOCATE_ID	(493, "Not found recipient advocate id", "Tidak ditemukan recipient advocate id dalam parameter"),
//    INVALID_RECIPIENT_ADVOCATE		(494, "Invalid value of recipient advocate id", "Tidak ditemukan recipient advocate id pada database"),

//    INTERNAL_SERVER_ERROR(500, "Internal server error", "Sistem mengalami gangguan. Silahkan coba beberapa saat lagi"),
//    ACTIVATION_ERROR(501, "There is an error while activating advocate. Please try again later", "Sistem mengalami gangguan silahkan coba beberapa saat lagi"),
//    PRIVATE_KEY_IS_NOT_SET(553, "Private key/salt is not set", "Sistem mengalami gangguan. Silahkan coba beberapa saat lagi"),

    SPECIAL_1(700, "For unidentified error only, mostly bug or improper exception catching", "Request gagal, silahkan coba beberapa saat lagi. Error 700"),
    SPECIAL_2(701, "When the result from server is an empty string", "Mohon cek kembali koneksi internet Anda. Error 701"),
    SPECIAL_3(702, "Unable to parse input", "Terdapat kesalahan pada aplikasi atau input anda. Error 702");

    private int internalStatusCode;
    private String description;
    private String prettyMessage;

    ResponseStatus(int statusCode, String description, String prettyMessage) {
        this.internalStatusCode = statusCode;
        this.description = description;
        this.prettyMessage = prettyMessage;
    }

    public int getStatusCode() {
        return internalStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public int getInternalStatusCode() {
        return internalStatusCode;
    }

    public String getPrettyMessage() {
        return prettyMessage;
    }

    public static ResponseStatus fromString(int statusCode) {
        for (ResponseStatus rs : ResponseStatus.values()) {
            if (rs.getStatusCode() == statusCode) {
                return rs;
            }
        }
        return null;
    }

    public static String getPrettyMessage(int statusCode) {
        for (ResponseStatus rs : ResponseStatus.values()) {
            if (rs.getStatusCode() == statusCode) {
                return rs.getPrettyMessage();
            }
        }
        return null;
    }

    public static int getStatusCode(String prettyMessage) {
        for (ResponseStatus r : ResponseStatus.values()) {
            if (r.getPrettyMessage().equals(prettyMessage)) {
                return r.getInternalStatusCode();
            }
        }

        return 0;
    }

}
