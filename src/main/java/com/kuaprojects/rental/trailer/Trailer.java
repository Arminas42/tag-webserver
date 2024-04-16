package com.kuaprojects.rental.trailer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Trailer implements Serializable {

    public Trailer() {}
    public Trailer(String name, String type){
        this.name = name;
        this.type = TrailerType.valueOf(type);
        this.yearMade = LocalDate.now();
        this.isUsableWithCarLicense = true;
        this.length = 10;
        this.width = 10;
        this.status = TrailerStatus.AVAILABLE;
    }

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate yearMade;
    @Enumerated(EnumType.STRING)
    private TrailerType type;
    private String name;
    private boolean isUsableWithCarLicense;
    private int length;
    private int width;
    @Enumerated(EnumType.STRING)
    private TrailerStatus status;

}
