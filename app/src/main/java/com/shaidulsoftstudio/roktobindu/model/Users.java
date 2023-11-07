package com.shaidulsoftstudio.roktobindu.model;

public class Users {

    private String Division,DonorSearchTag, District, Upzila, village, Mobileno,memberID,
            Usertype, bloodGroup, gender, email,donateNow,OkFavourite,
            fullname, password, Groupsearch, Districtsearch,TotalDonation,
            Upzillasearch, Unionsearch, Union, PatientName, HospitalName, BloodBagQuantity,
            uid, Profilepic, ActiveStatus, ActiveDate,ActiveTime, Querysearch, LastDonate,
            time, date, patientType, patientAge, patientBloodgroup, countryCodenumber, countryCodewithnumber;
    long timestamp,InactiveTime;
    boolean isfavourite;
    boolean isexpandable;
    private int TotalDonate,nextDonate;

    public Users() {
    }

    public Users(String division, String donorSearchTag, String district, String upzila, String village, String mobileno, String memberID, String usertype, String bloodGroup, String gender, String email, String donateNow, String okFavourite, String fullname, String password, String groupsearch, String districtsearch, String totalDonation, String upzillasearch, String unionsearch, String union, String patientName, String hospitalName, String bloodBagQuantity, String uid, String profilepic, String activeStatus, String activeDate, String activeTime, String querysearch, String lastDonate, String time, String date, String patientType, String patientAge, String patientBloodgroup, String countryCodenumber, String countryCodewithnumber, long timestamp, long inactiveTime, boolean isfavourite, boolean isexpandable, int totalDonate, int nextDonate) {
        Division = division;
        DonorSearchTag = donorSearchTag;
        District = district;
        Upzila = upzila;
        this.village = village;
        Mobileno = mobileno;
        this.memberID = memberID;
        Usertype = usertype;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.email = email;
        this.donateNow = donateNow;
        OkFavourite = okFavourite;
        this.fullname = fullname;
        this.password = password;
        Groupsearch = groupsearch;
        Districtsearch = districtsearch;
        TotalDonation = totalDonation;
        Upzillasearch = upzillasearch;
        Unionsearch = unionsearch;
        Union = union;
        PatientName = patientName;
        HospitalName = hospitalName;
        BloodBagQuantity = bloodBagQuantity;
        this.uid = uid;
        Profilepic = profilepic;
        ActiveStatus = activeStatus;
        ActiveDate = activeDate;
        ActiveTime = activeTime;
        Querysearch = querysearch;
        LastDonate = lastDonate;
        this.time = time;
        this.date = date;
        this.patientType = patientType;
        this.patientAge = patientAge;
        this.patientBloodgroup = patientBloodgroup;
        this.countryCodenumber = countryCodenumber;
        this.countryCodewithnumber = countryCodewithnumber;
        this.timestamp = timestamp;
        InactiveTime = inactiveTime;
        this.isfavourite = isfavourite;
        this.isexpandable = isexpandable;
        TotalDonate = totalDonate;
        this.nextDonate = nextDonate;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getDonorSearchTag() {
        return DonorSearchTag;
    }

    public void setDonorSearchTag(String donorSearchTag) {
        DonorSearchTag = donorSearchTag;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getUpzila() {
        return Upzila;
    }

    public void setUpzila(String upzila) {
        Upzila = upzila;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getMobileno() {
        return Mobileno;
    }

    public void setMobileno(String mobileno) {
        Mobileno = mobileno;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDonateNow() {
        return donateNow;
    }

    public void setDonateNow(String donateNow) {
        this.donateNow = donateNow;
    }

    public String getOkFavourite() {
        return OkFavourite;
    }

    public void setOkFavourite(String okFavourite) {
        OkFavourite = okFavourite;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupsearch() {
        return Groupsearch;
    }

    public void setGroupsearch(String groupsearch) {
        Groupsearch = groupsearch;
    }

    public String getDistrictsearch() {
        return Districtsearch;
    }

    public void setDistrictsearch(String districtsearch) {
        Districtsearch = districtsearch;
    }

    public String getTotalDonation() {
        return TotalDonation;
    }

    public void setTotalDonation(String totalDonation) {
        TotalDonation = totalDonation;
    }

    public String getUpzillasearch() {
        return Upzillasearch;
    }

    public void setUpzillasearch(String upzillasearch) {
        Upzillasearch = upzillasearch;
    }

    public String getUnionsearch() {
        return Unionsearch;
    }

    public void setUnionsearch(String unionsearch) {
        Unionsearch = unionsearch;
    }

    public String getUnion() {
        return Union;
    }

    public void setUnion(String union) {
        Union = union;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getBloodBagQuantity() {
        return BloodBagQuantity;
    }

    public void setBloodBagQuantity(String bloodBagQuantity) {
        BloodBagQuantity = bloodBagQuantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }

    public String getActiveStatus() {
        return ActiveStatus;
    }

    public void setActiveStatus(String activeStatus) {
        ActiveStatus = activeStatus;
    }

    public String getActiveDate() {
        return ActiveDate;
    }

    public void setActiveDate(String activeDate) {
        ActiveDate = activeDate;
    }

    public String getActiveTime() {
        return ActiveTime;
    }

    public void setActiveTime(String activeTime) {
        ActiveTime = activeTime;
    }

    public String getQuerysearch() {
        return Querysearch;
    }

    public void setQuerysearch(String querysearch) {
        Querysearch = querysearch;
    }

    public String getLastDonate() {
        return LastDonate;
    }

    public void setLastDonate(String lastDonate) {
        LastDonate = lastDonate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientBloodgroup() {
        return patientBloodgroup;
    }

    public void setPatientBloodgroup(String patientBloodgroup) {
        this.patientBloodgroup = patientBloodgroup;
    }

    public String getCountryCodenumber() {
        return countryCodenumber;
    }

    public void setCountryCodenumber(String countryCodenumber) {
        this.countryCodenumber = countryCodenumber;
    }

    public String getCountryCodewithnumber() {
        return countryCodewithnumber;
    }

    public void setCountryCodewithnumber(String countryCodewithnumber) {
        this.countryCodewithnumber = countryCodewithnumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getInactiveTime() {
        return InactiveTime;
    }

    public void setInactiveTime(long inactiveTime) {
        InactiveTime = inactiveTime;
    }

    public boolean isIsfavourite() {
        return isfavourite;
    }

    public void setIsfavourite(boolean isfavourite) {
        this.isfavourite = isfavourite;
    }

    public boolean isIsexpandable() {
        return isexpandable;
    }

    public void setIsexpandable(boolean isexpandable) {
        this.isexpandable = isexpandable;
    }

    public int getTotalDonate() {
        return TotalDonate;
    }

    public void setTotalDonate(int totalDonate) {
        TotalDonate = totalDonate;
    }

    public int getNextDonate() {
        return nextDonate;
    }

    public void setNextDonate(int nextDonate) {
        this.nextDonate = nextDonate;
    }
}