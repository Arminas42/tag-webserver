package com.kuaprojects.rental.tag;

import com.kuaprojects.rental.trailer.Trailer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Tag {

    public Tag(String tagDeviceCode, Trailer trailer){
        this.tagDeviceCode = tagDeviceCode;
        this.trailer = trailer;
    }
    public Tag(String tagDeviceCode){
        this.tagDeviceCode = tagDeviceCode;
    }
    @GeneratedValue
    @Id
    private Long id;
    private String tagDeviceCode;
    @ManyToOne
    private Trailer trailer;
}
