package com.spadsystem.model;

public class User {
    private String user_id, password, gender = "مرد", first_name = "", last_name = "", position = "", master_id = "16288831",
            state = "", preTel, city = "", address = "", service_table = "", fullAddress = "", telephone = "0", internalTel1 = "0",
            internalTel2 = "0", fax = "0", preIntTel = "0", mobile = "", type = "user", national_code = "", image;

    public String getFullAddress() {
        return setPersianText(fullAddress);
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = setPersianText(fullAddress);
    }

    public String getNational_code() {
        return national_code;
    }

    public void setNational_code(String national_code) {
        this.national_code = national_code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getService_table() {
        return setPersianText(service_table);
    }

    public void setService_table(String service_table) {
        this.service_table = service_table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreTel() {
        return preTel;
    }

    public void setPreTel(String preTel) {
        this.preTel = preTel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getInternalTel1() {
        return internalTel1;
    }

    public void setInternalTel1(String internalTel1) {
        this.internalTel1 = internalTel1;
    }

    public String getInternalTel2() {
        return internalTel2;
    }

    public void setInternalTel2(String internalTel2) {
        this.internalTel2 = internalTel2;
    }

    public String getPreIntTel() {
        return preIntTel;
    }

    public void setPreIntTel(String preIntTel) {
        this.preIntTel = preIntTel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return setPersianText(first_name);
    }

    public void setFirst_name(String first_name) {
        this.first_name = setPersianText(first_name);
    }

    public String getLast_name() {
        return setPersianText(last_name);
    }

    public void setLast_name(String last_name) {
        this.last_name = setPersianText(last_name);
    }

    public String getPosition() {
        return setPersianText(position);
    }

    public void setPosition(String position) {
        this.position = setPersianText(position);
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getState() {
        return setPersianText(state);
    }

    public void setState(String state) {
        this.state = setPersianText(state);
    }

    public String getCity() {
        return setPersianText(city);
    }

    public void setCity(String city) {
        this.city = setPersianText(city);
    }

    public String getAddress() {
        return setPersianText(address);
    }

    public void setAddress(String address) {
        this.address = setPersianText(address);
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public static String setPersianText(String str) {
        if (str != null && str.length() > 0) {
            while (str.indexOf('ي') != -1) {
                str = str.replace('ي', 'ی');
            }
            while (str.indexOf('ك') != -1) {
                str = str.replace('ك', 'ک');
            }
        }
        return str;
    }
}
