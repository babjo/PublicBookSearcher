package com.seoul.publicbooksearcher.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    public static final Long SEOUL_LIB_ID = 110000L;
    public static final Long DOBONG_LIB_ID = 111011L;
    public static final Long DONGDAEMUN_LIB_ID = 111012L;
    public static final Long DONGJAK_LIB_ID = 111013L;
    public static final Long GAEPO_LIB_ID = 111006L;
    public static final Long GANGDONG_LIB_ID = 111004L;
    public static final Long GANGNAM_LIB_ID = 111003L;
    public static final Long GANGSEO_LIB_ID = 111005L;
    public static final Long GOCHEOK_LIB_ID = 111008L;
    public static final Long GODEOK_LEARNING_CENTER_ID = 111007L;
    public static final Long GURO_LIB_ID = 111009L;
    public static final Long JONGRO_LIB_ID = 111021L;
    public static final Long JUNGDOK_LIB_ID = 111020L;
    public static final Long MAPO_AHYUN_BRANCH_ID = 111031L;
    public static final Long MAPO_LEARNING_CENTER_ID = 111014L;
    public static final Long NAMSAN_LIB_ID = 111010L;
    public static final Long NOWON_LEARNING_CENTER_ID = 111022L;
    public static final Long SEODAEMUN_LIB_ID = 111016L;
    public static final Long SEOULCHILD_LIB_ID = 111017L;
    public static final Long SONGPA_LIB_ID = 111030L;
    public static final Long YANGCHEON_LIB_ID = 111015L;
    public static final Long YEONGDEUNGO_LEARNING_CENTER = 111018L;
    public static final Long YONGSAN_LIB_ID = 111019L;

    private Long id;
    private String name;
    private List<Book> books;
    private Location location;
    private int color;

    public Library(Long id, List<Book> books) {
        this.id = id;
        this.books = books;
    }

    public Library(Long id, String name, double latitude, double longitude, int color) {
        this.id = id;
        this.name = name;
        this.location = new Location(latitude, longitude);
        this.books = new ArrayList();
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void add(Book book) {
        this.books.add(book);
    }

    public void clear() {
        this.books.clear();
    }

    public double distance(Location location){
        return this.location.distance(location);
    }

    public int getColor() {
        return color;
    }

    public Long getId() {
        return id;
    }
}
