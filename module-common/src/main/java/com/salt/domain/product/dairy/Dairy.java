package com.salt.domain.product.dairy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Dairy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private int amount;

    @Column
    private LocalDateTime createdAt; //생성날짜

    @Column
    private LocalDateTime updatedAt; // Job 수행날짜

    @Column
    private LocalDateTime deleteAt;

    @Column
    @Enumerated(EnumType.STRING)
    private DairyStatus status;

    public Dairy setStatusByDate(){
        if(this.checking()){
            this.status = DairyStatus.DEAD;
        }
        return this;
    }

    public boolean checking(){ return this.updatedAt.isAfter(this.deleteAt); }

    @Builder
    public Dairy(){}

    @Builder
    public Dairy(int id, String name, String category, int amount, DairyStatus status){
        this.id=id;
        this.name=name;
        this.category=category;
        this.amount=amount;
        this.status= DairyStatus.FRESH;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleteAt = createdAt.plusMonths(3);
    }



}
