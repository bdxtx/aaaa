package com.weiyu.sp.lsjy.bean;

public class CourseBean {
//    "id": "2020414115153463",
//            "title": "",
//            "subjectCode": 700,
//            "subjectName": "语文",
//            "schoolYear": "八年级",
//            "booksNumber": "上册",
//            "curriculum": "第八课",
//            "subject": "梦之泪伤",
//            "setNumber": "8",
//            "maxPrice": "333.00",
//            "minPrice": "44.00",
//            "readNumber": 0,
//            "url": "https://lsjy2020.oss-cn-hangzhou.aliyuncs.com/lsjy/2020-04-14/images/2020414115220653.png?Expires=33122855944&OSSAccessKeyId=LTAI4FcUVowUm97dFSsGd6qe&Signature=OoDPY9bI0ihDHx%2FsVMRCWenr1bE%3D",
//            "collection": false,
//            "exist": false
    private String id;
    private String title;
    private int subjectCode;
    private String subjectName;
    private String schoolYear;
    private String booksNumber;
    private String curriculum;
    private String subject;
    private String setNumber;
    private String maxPrice;
    private String minPrice;
    private String readNumber;
    private String url;
    private boolean collection;
    private boolean exist;
    private boolean vip;
    private String videoUrl;
    private String curriculumBrief;
    private boolean hasBeanClicked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getBooksNumber() {
        return booksNumber;
    }

    public void setBooksNumber(String booksNumber) {
        this.booksNumber = booksNumber;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(String readNumber) {
        this.readNumber = readNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCurriculumBrief() {
        return curriculumBrief;
    }

    public void setCurriculumBrief(String curriculumBrief) {
        this.curriculumBrief = curriculumBrief;
    }

    public boolean isHasBeanClicked() {
        return hasBeanClicked;
    }

    public void setHasBeanClicked(boolean hasBeanClicked) {
        this.hasBeanClicked = hasBeanClicked;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}
