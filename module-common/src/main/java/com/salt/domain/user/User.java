package com.salt.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String nickName;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    private int amountCharged;  // 청구된 금액

    @Column
    private int amountPaid; // 결제된 금액

    @Column
    private LocalDate dueDate;  // 결제 마감일

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public User setStatusByUnPaid() {
        if(this.isUnpaid()) {
            this.status = UserStatus.INACTIVE;
        }
        return this;
    }

    public boolean isUnpaid() {
        return this.amountCharged <= this.amountPaid;
    }

    @Builder
    public User() {}

    @Builder
    public User(String name, String email, String nickName, int amountCharged, int amountPaid, LocalDate dueDate) {
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.status = UserStatus.ACTIVE;
        this.amountCharged = amountCharged;
        this.amountPaid = amountPaid;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}