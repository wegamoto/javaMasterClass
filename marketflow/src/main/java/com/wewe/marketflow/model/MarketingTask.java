package com.wewe.marketflow.model;

@Entity
public class MarketingTask {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(length = 1000)
    private String detail;

    private LocalDate dueDate;
    private String status; // TODO, IN_PROGRESS, DONE

    @ManyToOne
    private User assignedTo;

    @ManyToOne
    private Campaign campaign;
}

